package inovaEmpresa.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTypeTest {


    @Test
    void testFromValueValid() {
        int adminValue = 1;
        int evaluatorValue = 2;
        int collaboratorValue = 3;
        UserType admin = UserType.fromValue(adminValue);
        UserType evaluator = UserType.fromValue(evaluatorValue);
        UserType collaborator = UserType.fromValue(collaboratorValue);
        assertEquals(UserType.ADMIN, admin);
        assertEquals(UserType.EVALUATOR, evaluator);
        assertEquals(UserType.COLLABORATOR, collaborator);
    }

    @Test
    void testFromValueInvalid() {
        int invalidValue = 99;
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            UserType.fromValue(invalidValue);
        });
        assertEquals("invalid priority value" + invalidValue, thrown.getMessage());
    }

    @Test
    void testFromStringValid() {

        String adminString = "ADMIN";
        String evaluatorString = "evaluator";
        String collaboratorString = "COLLABORATOR";
        UserType admin = UserType.fromString(adminString);
        UserType evaluator = UserType.fromString(evaluatorString);
        UserType collaborator = UserType.fromString(collaboratorString);

        assertEquals(UserType.ADMIN, admin);
        assertEquals(UserType.EVALUATOR, evaluator);
        assertEquals(UserType.COLLABORATOR, collaborator);
    }

    @Test
    void testFromStringInvalid() {
        String invalidType = "INVALID";

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            UserType.fromString(invalidType);
        });
        assertEquals("Invalid user type: " + invalidType, thrown.getMessage());
    }

}
