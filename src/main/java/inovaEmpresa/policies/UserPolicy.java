package inovaEmpresa.policies;

import inovaEmpresa.entities.User;
import inovaEmpresa.enums.UserType;

import org.springframework.stereotype.Component;

@Component
public class UserPolicy {
    public static boolean canUpdateUser(User loggedInUser) {
        System.out.println(loggedInUser.getType());
        return loggedInUser.getType() == UserType.ADMIN.getValue();
    }
}
