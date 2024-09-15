package inovaEmpresa.dto.user;

import inovaEmpresa.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserType {
    private Long userId;
    private UserType userType;
}
