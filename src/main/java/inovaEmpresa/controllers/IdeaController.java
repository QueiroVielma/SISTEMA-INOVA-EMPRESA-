package inovaEmpresa.controllers;


import inovaEmpresa.dto.idea.CreateIdea;
import inovaEmpresa.entities.Idea;
import inovaEmpresa.services.IdeaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ideas")
public class IdeaController {

    @Autowired
    private IdeaService ideaService;

    @PostMapping
    public ResponseEntity<Idea> createIdea(@Valid @RequestBody CreateIdea data) {
        Idea createdIdea = ideaService.store(data);
        return ResponseEntity.ok(createdIdea);
    }
    @GetMapping("/best-ideas")
    public ResponseEntity<List<Idea>> bestIdeas() {
        List<Idea> topIdeas = ideaService.getTop10Ideas();
        return ResponseEntity.ok(topIdeas);
    }
}
