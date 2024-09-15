package inovaEmpresa.policies;

import inovaEmpresa.entities.User;
import inovaEmpresa.enums.UserType;
import org.springframework.stereotype.Component;

@Component
public class EventPolicy {
    public static boolean canUpdateEvent(User loggedInUser) {
        return loggedInUser.getType() == UserType.ADMIN.getValue();
    }
}
