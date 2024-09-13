package inovaEmpresa.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate juryEvaluationDates;
    private LocalDate DatePopularEvaluation;
    @OneToMany
    private Idea idea;
    @ManyToMany
    @JoinTable(
        name ="event_evaluators",
        joinColumns = @JoinColumn (name= "evends_id"),
        inverseJoinColumns = @JoinColumn(name = "evaluators_id")
    )
    @JsonManagedReference
    private List <User> evaluators;
}
