package inovaEmpresa.services;

import inovaEmpresa.dto.idea.CreateIdea;
import inovaEmpresa.entities.Idea;
import inovaEmpresa.entities.User;
import inovaEmpresa.repositories.IdeaRepository;
import inovaEmpresa.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
