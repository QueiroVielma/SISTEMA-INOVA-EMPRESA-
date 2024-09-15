package inovaEmpresa.idea;

import inovaEmpresa.builder.IdeaBuilder;
import inovaEmpresa.dto.idea.CreateIdea;
import inovaEmpresa.entities.Idea;
import inovaEmpresa.entities.User;
import inovaEmpresa.repositories.IdeaRepository;
import inovaEmpresa.repositories.UserRepository;
import inovaEmpresa.services.IdeaService;
import inovaEmpresa.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class IdeaServiceTest {

    @InjectMocks
    private IdeaService ideaService;

    @Mock
    private IdeaRepository ideaRepository;

    @Mock
    private UserRepository userRepository;

    private List<Idea> generateMockIdeas(int count) {
        return IntStream.range(1, count + 1)
            .mapToObj(i -> new IdeaBuilder()
            .withId((long) i)
            .withName("Test idea " + i)
            .withImpact(i % 2 == 0 ? "High" : "Medium")
            .withEstimatedCost(1000.0 * i)
            .withDescription("Description test " + i)
            .build())
            .collect(Collectors.toList());
    }

    @Test
    void testGetTop10Ideas() {
        List<Idea> mockIdeas = generateMockIdeas(11);

        when(ideaRepository.findTop10Ideas()).thenReturn(mockIdeas);

        List<Idea> topIdeas = ideaService.getTop10Ideas();
        assertNotNull(topIdeas);
        assertEquals(10, topIdeas.size());
        assertEquals("Test idea 1", topIdeas.get(0).getName());
        assertEquals("Test idea 10", topIdeas.get(9).getName());
    }

    @Test
    void testCreateIdea() {
        CreateIdea data = new CreateIdea();
        data.setUserId(1L);
        data.setName("Test idea");
        data.setImpact("High");
        data.setEstimatedCost(1000.0);
        data.setDescription("Description test");
        data.setScore(5.0);

        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(ideaRepository.findByUserId(1L)).thenReturn(Optional.empty());

        Idea savedIdea = new Idea();
        savedIdea.setName("Test idea");
        savedIdea.setImpact("High");
        savedIdea.setEstimatedCost(1000.0);
        savedIdea.setDescription("Description test");
        savedIdea.setScore(5.0);
        savedIdea.setUser(user);
        when(ideaRepository.save(any(Idea.class))).thenReturn(savedIdea);

        Idea result = ideaService.store(data);

        assertNotNull(result);
        assertEquals("Test idea", result.getName());
        assertEquals("High", result.getImpact());
        assertEquals(1000.0, result.getEstimatedCost());
        assertEquals("Description test", result.getDescription());
        assertEquals(5.0, result.getScore());
        assertEquals(user, result.getUser());

        verify(ideaRepository, times(1)).save(any(Idea.class));
    }

    @Test
    void testIdeaExists() {
        CreateIdea data = new CreateIdea();
        data.setUserId(1L);
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(ideaRepository.findByUserId(1L)).thenReturn(Optional.of(new Idea()));

        assertThrows(IllegalStateException.class, () -> {
            ideaService.store(data);
        });
    }

    @Test
    public void testIdeaUserNotFound() {
        CreateIdea data = new CreateIdea();
        data.setUserId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            ideaService.store(data);
        });
    }

}
