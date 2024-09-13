package inovaEmpresa.entities;

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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private String password;
    private int type;
    @OneToMany (mappedBy = "user")
    private List <Idea> ideas;
    @ManyToMany(mappedBy = "evaluators")
    private List <Event> evaluatorsEvents;
    @ManyToMany(mappedBy = "evaluators")
    private List <Idea> evaluatorsideas;

}
