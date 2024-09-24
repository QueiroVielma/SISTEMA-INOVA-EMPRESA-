package inovaEmpresa.entities;

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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private int type;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Idea> ideas = new ArrayList<>();

    @ManyToMany(mappedBy = "evaluators")
    private List<Event> evaluatorsEvents;

    @ManyToMany(mappedBy = "evaluators")
    private List<Idea> evaluatorsIdeas;
}
