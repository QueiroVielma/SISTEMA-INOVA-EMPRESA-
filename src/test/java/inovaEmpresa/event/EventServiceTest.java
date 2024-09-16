package inovaEmpresa.event;

import inovaEmpresa.dto.event.AssignEvaluators;
import inovaEmpresa.dto.event.AssignIdeas;
import inovaEmpresa.dto.event.CreateEvent;
import inovaEmpresa.entities.Event;
import inovaEmpresa.entities.Idea;
import inovaEmpresa.entities.User;
import inovaEmpresa.enums.UserType;
import inovaEmpresa.policies.EventPolicy;
import inovaEmpresa.repositories.EventRepository;
import inovaEmpresa.repositories.UserRepository;
import inovaEmpresa.services.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EventServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;


    @Mock
    private EventPolicy eventPolicy;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStoreSuccess() {
        User userLogged = new User();
        userLogged.setId(1L);
        userLogged.setType(UserType.ADMIN.getValue());

        CreateEvent createEvent = new CreateEvent();
        createEvent.setUserId(1L);
        createEvent.setName("Test Event");
        createEvent.setDescription("Event Description");
        createEvent.setStartDate(LocalDate.parse("2023-09-01"));
        createEvent.setEndDate(LocalDate.parse("2023-09-10"));
        createEvent.setJuryEvaluationDate(LocalDate.parse("2023-09-15"));
        createEvent.setPopularEvaluationDate(LocalDate.parse("2023-09-20"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(userLogged));
        when(eventPolicy.canUpdateEvent(userLogged)).thenReturn(true);
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Event result = eventService.store(createEvent);
        assertNotNull(result);
        assertEquals("Test Event", result.getName());
        assertEquals("Event Description", result.getDescription());
        assertEquals(LocalDate.parse("2023-09-01"), result.getStartDate());
        assertEquals(LocalDate.parse("2023-09-10"), result.getEndDate());
        assertEquals(LocalDate.parse("2023-09-15"), result.getJuryEvaluationDate());
        assertEquals(LocalDate.parse("2023-09-20"), result.getDatePopularEvaluation());
        verify(userRepository, times(1)).findById(1L);
        verify(eventPolicy, times(1)).canUpdateEvent(userLogged);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void testStoreUserNotAuthorized() {
        User userLogged = new User();
        userLogged.setId(1L);
        userLogged.setType(UserType.COLLABORATOR.getValue());

        CreateEvent createEvent = new CreateEvent();
        createEvent.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userLogged));
        when(eventPolicy.canUpdateEvent(userLogged)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            eventService.store(createEvent);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Usuario não é um admin", exception.getReason());

        verify(userRepository, times(1)).findById(1L);
        verify(eventPolicy, times(1)).canUpdateEvent(userLogged);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void testUserNotFound() {
        CreateEvent createEvent = new CreateEvent();
        createEvent.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            eventService.store(createEvent);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Logged user not found", exception.getReason());
        verify(userRepository, times(1)).findById(1L);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void testAssignEventsSuccess() {
        AssignEvaluators assignEvaluators = new AssignEvaluators();
        assignEvaluators.setEventId(1L);
        assignEvaluators.setEvaluatorsId(Arrays.asList(2L, 3L));
        Event event = new Event();
        event.setId(1L);
        User evaluator1 = new User();
        evaluator1.setId(2L);
        User evaluator2 = new User();
        evaluator2.setId(3L);

        List<User> evaluators = Arrays.asList(evaluator1, evaluator2);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(userRepository.findAllById(assignEvaluators.getEvaluatorsId())).thenReturn(evaluators);
        eventService.assignEvents(assignEvaluators);

        assertEquals(evaluators, event.getEvaluators());
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void testAssignEventsEventNotFound() {
        AssignEvaluators assignEvaluators = new AssignEvaluators();
        assignEvaluators.setEventId(1L);
        assignEvaluators.setEvaluatorsId(Arrays.asList(2L, 3L));

        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                eventService.assignEvents(assignEvaluators)
        );
        assertEquals("Event not found", exception.getMessage());
    }

    @Test
    void testAssignEventsEvaluatorNotFound() {
        AssignEvaluators assignEvaluators = new AssignEvaluators();
        assignEvaluators.setEventId(1L);
        assignEvaluators.setEvaluatorsId(Arrays.asList(2L, 3L));
        Event event = new Event();
        event.setId(1L);
        User evaluator1 = new User();
        evaluator1.setId(2L);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(userRepository.findAllById(assignEvaluators.getEvaluatorsId())).thenReturn(Arrays.asList(evaluator1));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                eventService.assignEvents(assignEvaluators)
        );

        assertEquals("One or more evaluators not found", exception.getMessage());
    }

    @Test
    public void testDitributionIdeas() {
        // Dados de teste
        User user1 = new User();
        user1.setId(1L);
        user1.setIdeas(new ArrayList<>());

        User user2 = new User();
        user2.setId(2L);
        user2.setIdeas(new ArrayList<>());

        Idea idea1 = new Idea();
        Idea idea2 = new Idea();
        Idea idea3 = new Idea();
        List<Idea> ideas = Arrays.asList(idea1, idea2, idea3);

        Event event = new Event();
        event.setId(1L);
        event.setIdeas(ideas);
        event.setEvaluators(Arrays.asList(user1, user2));

        AssignIdeas data = new AssignIdeas();
        data.setId(1L);

        // Configuração do mock
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        // Chamada do método a ser testado
        eventService.ditributionIdeas(data);

        // Verificação
        verify(userRepository, times(3)).save(user1);
        verify(userRepository, times(3)).save(user2);
    }
}
