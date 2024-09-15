package inovaEmpresa.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AssignEvaluators {
    private Long eventId;
    private List<Long> evaluatorsId;
}
