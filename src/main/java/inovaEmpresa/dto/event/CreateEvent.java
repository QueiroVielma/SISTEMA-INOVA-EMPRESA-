package inovaEmpresa.dto.event;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateEvent {
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate juryEvaluationDate;
    private LocalDate popularEvaluationDate;
}
