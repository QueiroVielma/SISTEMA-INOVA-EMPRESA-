package inovaEmpresa.repositories;

import inovaEmpresa.entities.Idea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdeaRepository  extends JpaRepository<Idea, Long> {
}
