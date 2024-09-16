package inovaEmpresa.controllers;

import inovaEmpresa.dto.event.AssignEvaluators;
import inovaEmpresa.dto.event.AssignIdeas;
import inovaEmpresa.dto.event.CreateEvent;
import inovaEmpresa.entities.Event;
import inovaEmpresa.services.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/events")
public class EventController {

    @Autowired
    protected EventService eventService;
    @PostMapping
    public ResponseEntity<?> store(@Validated @RequestBody CreateEvent data){
        try{
          Event createdUser = eventService.store(data);
          return ResponseEntity.ok(createdUser);
        }catch (ResponseStatusException e){
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @PutMapping("/assign")
    public ResponseEntity<Void> assign(@RequestBody AssignEvaluators data) {
        eventService.assignEvents(data);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/evaluator")
    public ResponseEntity<String> ditributionIdeas(@RequestBody AssignIdeas data) {
        eventService.ditributionIdeas(data);
        return ResponseEntity.ok("Ideias distribuídas com sucesso entre os jurados.");
    }
}
