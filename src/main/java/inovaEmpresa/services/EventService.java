package inovaEmpresa.services;

import inovaEmpresa.dto.event.AssignEvaluators;
import inovaEmpresa.dto.event.CreateEvent;
import inovaEmpresa.entities.Event;
import inovaEmpresa.entities.User;
import inovaEmpresa.policies.EventPolicy;
import inovaEmpresa.repositories.EventRepository;

import inovaEmpresa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;

    public Event store(CreateEvent createEvent) {
        User userLogged = userRepository.findById(createEvent.getUserId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged user not found"));
        System.out.println(userLogged.getType());
        if(EventPolicy.canUpdateEvent(userLogged)){
            Event event = new Event();
            event.setName(createEvent.getName());
            event.setDescription(createEvent.getDescription());
            event.setStartDate(createEvent.getStartDate());
            event.setEndDate(createEvent.getEndDate());
            event.setJuryEvaluationDate(createEvent.getJuryEvaluationDate());
            event.setDatePopularEvaluation(createEvent.getPopularEvaluationDate());

            return eventRepository.save(event);
        }else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario não é um admin");
        }
    }

    public void assignEvents(AssignEvaluators data) {
        Event event = eventRepository.findById(data.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        List<User> evaluators = userRepository.findAllById(data.getEvaluatorsId());
        if (evaluators.size() != data.getEvaluatorsId().size()) {
            throw new IllegalArgumentException("One or more evaluators not found");
        }

        event.setEvaluators(evaluators);
        eventRepository.save(event);
    }
}
