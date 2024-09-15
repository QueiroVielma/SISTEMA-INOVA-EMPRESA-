package inovaEmpresa.repositories;

import inovaEmpresa.entities.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IdeaRepository  extends JpaRepository<Idea, Long> {
    Optional<Idea> findByUserId(Long userId);

    @Query("SELECT i FROM Idea i ORDER BY i.score DESC")
    List<Idea> findTop10Ideas();
}
