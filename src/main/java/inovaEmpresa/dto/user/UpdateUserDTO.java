package inovaEmpresa.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {
    private Long userId;
    private String name;
    private String email;
    private String password;
    private String userType;
}
