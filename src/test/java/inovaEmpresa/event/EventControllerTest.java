package inovaEmpresa.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import inovaEmpresa.controllers.EventController;
import inovaEmpresa.dto.event.AssignEvaluators;
import inovaEmpresa.dto.event.CreateEvent;
import inovaEmpresa.entities.Event;
import inovaEmpresa.services.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testStoreEventSuccess() throws Exception {
        CreateEvent createEvent = new CreateEvent();
        createEvent.setName("Test Event");
        createEvent.setDescription("Event Description");

        Event event = new Event();
        event.setName("Test Event");
        event.setDescription("Event Description");

        when(eventService.store(any(CreateEvent.class))).thenReturn(event);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEvent)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(event)));
    }

    @Test
    void testStoreEventNotFound() throws Exception {
        CreateEvent createEvent = new CreateEvent();
        createEvent.setName("Test Event");
        createEvent.setDescription("Event Description");

        when(eventService.store(any(CreateEvent.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged user not found"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEvent)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Logged user not found"));
    }

    @Test
    void testStoreEventInternalError() throws Exception {
        CreateEvent createEvent = new CreateEvent();
        createEvent.setName("Test Event");
        createEvent.setDescription("Event Description");

        when(eventService.store(any(CreateEvent.class)))
                .thenThrow(new RuntimeException("Unexpected Error"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEvent)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    @Test
    void testAssignEvaluatorsSuccess() throws Exception {
        AssignEvaluators assignEvaluators = new AssignEvaluators();
        assignEvaluators.setEventId(1L);
        assignEvaluators.setEvaluatorsId(List.of(2L, 3L));

        doNothing().when(eventService).assignEvents(any(AssignEvaluators.class));
        mockMvc.perform(MockMvcRequestBuilders.put("/api/events/assign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignEvaluators)))
                .andExpect(status().isOk());
    }
}
