package inovaEmpresa.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Idea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String impact;
    private Double estimatedCost;
    private String description;
    private double score;

    private List<Long> voters=new ArrayList<>();;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne()
    @JoinColumn(name = "idea_id")
    @JsonManagedReference
    private Event event;

    @ManyToMany
    @JoinTable(
            name = "idea_evaluators",
            joinColumns = @JoinColumn(name = "idea_id"),
            inverseJoinColumns = @JoinColumn(name = "evaluator_id")
    )
    @JsonManagedReference
    private List<User> evaluators;
}
