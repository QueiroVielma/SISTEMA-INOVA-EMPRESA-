package inovaEmpresa.services;

import inovaEmpresa.dto.event.AssignEvaluators;
import inovaEmpresa.dto.event.AssignIdeas;
import inovaEmpresa.dto.event.CreateEvent;
import inovaEmpresa.entities.Event;
import inovaEmpresa.entities.Idea;
import inovaEmpresa.entities.User;
import inovaEmpresa.enums.UserType;
import inovaEmpresa.policies.EventPolicy;
import inovaEmpresa.repositories.EventRepository;

import inovaEmpresa.repositories.IdeaRepository;
import inovaEmpresa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IdeaRepository ideaRepository;

    @Autowired
    private EventPolicy eventPolicy;
    public Event store(CreateEvent createEvent) {
        User userLogged = userRepository.findById(createEvent.getUserId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged user not found"));
        System.out.println(userLogged.getType());
        if(eventPolicy.canUpdateEvent(userLogged)){
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

        for (User evaluator : evaluators) {
                if (evaluator.getType() == UserType.EVALUATOR.getValue()) {
                    event.getEvaluators().add(evaluator);
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + evaluator.getId() + " is not an evaluator");
                }
        }

        event.setEvaluators(evaluators);
        eventRepository.save(event);
    }

    public void ditributionIdeas(AssignIdeas data) {
        Event event = eventRepository.findById(data.getId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        List<Idea> ideas = event.getIdeas();
        List<User> evaluators = event.getEvaluators();
        Collections.shuffle(ideas);
        int index = 0;
        for (Idea idea : ideas) {
            User jurado1 = evaluators.get(index % evaluators.size());
            User jurado2 = evaluators.get((index + 1) % evaluators.size());

            if (jurado1.getIdeas() == null) {
                jurado1.setIdeas(new ArrayList<>());
            }
            if (jurado2.getIdeas() == null) {
                jurado2.setIdeas(new ArrayList<>());
            }

            jurado1.getIdeas().add(idea);
            jurado2.getIdeas().add(idea);
            userRepository.save(jurado1);
            userRepository.save(jurado2);
              index++;
        }
    }
}
