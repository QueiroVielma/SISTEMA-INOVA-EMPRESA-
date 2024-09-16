package inovaEmpresa.services;

import inovaEmpresa.dto.event.AssignIdeas;
import inovaEmpresa.dto.idea.AddScore;
import inovaEmpresa.dto.idea.CreateIdea;
import inovaEmpresa.entities.Idea;
import inovaEmpresa.entities.User;
import inovaEmpresa.repositories.IdeaRepository;
import inovaEmpresa.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class IdeaService {
    @Autowired
    private IdeaRepository ideaRepository;

    @Autowired
    private UserRepository userRepository;

    public Idea store(CreateIdea data) {
        User user = userRepository.findById(data.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + data.getUserId()));

        if (ideaRepository.findByUserId(user.getId()).isPresent()) {
            throw new IllegalStateException("User has already posted an idea");
        }
        Idea idea = new Idea();
        idea.setName(data.getName());
        idea.setImpact(data.getImpact());
        idea.setEstimatedCost(data.getEstimatedCost());
        idea.setDescription(data.getDescription());
        if (data.getScore() != null) {
            idea.setScore(data.getScore());
        } else {
            idea.setScore(0.0);
        }
        idea.setUser(user);
        return ideaRepository.save(idea);
    }

    public List<Idea> getTop10Ideas() {
        List<Idea> topIdeas = ideaRepository.findTop10Ideas();
        if (topIdeas.size() > 10) {
            return topIdeas.subList(0, 10);
        }
        return topIdeas;
    }

    public double endorseIdea(AddScore data) {
         Idea idea= ideaRepository.findById(data.getIdeaId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + data.getIdeaId()));
        if (data.getScore1() < 3 || data.getScore1() > 10) {
            if (data.getScore2() < 3 || data.getScore2() > 10)
                throw new IllegalArgumentException("A nota deve estar entre 3 e 10.");
        }
        double score= (data.getScore1()+data.getScore2())/2;
        idea.setScore(score);
        ideaRepository.save(idea);
        return score;
    }

    public void popularRating(Long idIdea, Long userId) {
        Idea idea = ideaRepository.findById(idIdea).orElseThrow(() -> new RuntimeException("Idea not found"));

        if (!idea.getVoters().contains(userId)) {
            idea.getVoters().add(userId);
            ideaRepository.save(idea);
        }
    }
}
