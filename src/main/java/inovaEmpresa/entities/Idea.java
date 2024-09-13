package inovaEmpresa.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Idea {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String impact;
    private Double estimatedCost;
    private String description;
    private double score;
    private List<Long> voters;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "idea")
    private List<Event> events;  // Correção de Event mapeado para Idea

    @ManyToMany
    @JoinTable(
            name = "idea_evaluators",
            joinColumns = @JoinColumn(name = "idea_id"),
            inverseJoinColumns = @JoinColumn(name = "evaluator_id")
    )
    @JsonManagedReference
    private List<User> evaluators;
}
