package inovaEmpresa.policies;

import inovaEmpresa.entities.User;
import inovaEmpresa.enums.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class EventPolicyTest {
    @InjectMocks
    private EventPolicy eventPolicy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCanUpdateEventWhenUserIsAdmin() {
        User adminUser = new User();
        adminUser.setType(UserType.ADMIN.getValue());
        boolean result = eventPolicy.canUpdateEvent(adminUser);
        assertTrue(result, "Admin user should be able to update the event.");
    }

    @Test
    void testCanUpdateEventWhenUserIsNotAdmin() {
        User regularUser = new User();
        regularUser.setType(UserType.COLLABORATOR.getValue());
        boolean result = eventPolicy.canUpdateEvent(regularUser);
        assertFalse(result, "Regular user should not be able to update the event.");
    }
}
