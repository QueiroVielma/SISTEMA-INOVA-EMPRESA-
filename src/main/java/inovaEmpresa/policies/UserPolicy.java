package inovaEmpresa.policies;

import inovaEmpresa.entities.User;
import inovaEmpresa.enums.UserType;

import org.springframework.stereotype.Component;

@Component
public class UserPolicy {
    public static boolean canUpdateUser(User loggedInUser) {
        return loggedInUser.getType() == UserType.ADMIN.getValue();
    }
}
