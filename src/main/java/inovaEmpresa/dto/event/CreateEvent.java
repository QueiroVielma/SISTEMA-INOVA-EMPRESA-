package inovaEmpresa.dto.event;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateEvent {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate juryEvaluationDate;

    private LocalDate popularEvaluationDate;


    private Long userId;
}
