package inovaEmpresa.dto.idea;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AddScore {
    private Long IdeaId;
    private double score1;
    private double score2;
}
