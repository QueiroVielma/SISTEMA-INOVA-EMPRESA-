package inovaEmpresa.idea;

import com.fasterxml.jackson.databind.ObjectMapper;
import inovaEmpresa.controllers.IdeaController;
import inovaEmpresa.dto.idea.CreateIdea;
import inovaEmpresa.entities.Idea;
import inovaEmpresa.services.IdeaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(IdeaController.class)
public class IdeaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IdeaService ideaService;

    @Test
    void testCreateIdea() throws Exception {
        CreateIdea createIdea = new CreateIdea();
        createIdea.setUserId(1L);
        createIdea.setName("Test idea");
        createIdea.setImpact("High");
        createIdea.setEstimatedCost(1000.0);
        createIdea.setDescription("Test");

        Idea idea = new Idea();
        idea.setId(1L);
        idea.setName("Test idea");
        idea.setImpact("High");
        idea.setEstimatedCost(1000.0);
        idea.setDescription("Test");

        Mockito.when(ideaService.store(Mockito.any(CreateIdea.class))).thenReturn(idea);

        mockMvc.perform(post("/api/ideas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createIdea)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idea.getId()));
    }

    @Test
    void testBestIdeas() throws Exception {
        Idea idea1 = new Idea();
        idea1.setId(1L);
        idea1.setName("Test idea 1");
        idea1.setImpact("High");
        idea1.setEstimatedCost(1000.0);
        idea1.setDescription("Test description 1");

        Idea idea2 = new Idea();
        idea2.setId(2L);
        idea2.setName("Test idea 2");
        idea2.setImpact("Medium");
        idea2.setEstimatedCost(2000.0);
        idea2.setDescription("Test description 2");

        List<Idea> mockIdeas = Arrays.asList(idea1, idea2);
        when(ideaService.getTop10Ideas()).thenReturn(mockIdeas);

        mockMvc.perform(get("/api/ideas/best-ideas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Test idea 1"))
                .andExpect(jsonPath("$[0].impact").value("High"))
                .andExpect(jsonPath("$[0].estimatedCost").value(1000.0))
                .andExpect(jsonPath("$[0].description").value("Test description 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Test idea 2"))
                .andExpect(jsonPath("$[1].impact").value("Medium"))
                .andExpect(jsonPath("$[1].estimatedCost").value(2000.0))
                .andExpect(jsonPath("$[1].description").value("Test description 2"));
    }
}
