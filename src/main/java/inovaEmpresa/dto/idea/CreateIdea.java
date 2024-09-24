package inovaEmpresa.dto.idea;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateIdea {
    private Long userId;

    private Long eventId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Impact is required")
    private String impact;

    private Double estimatedCost;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must be up to 1000 characters")
    private String description;

    private Double score;
}
