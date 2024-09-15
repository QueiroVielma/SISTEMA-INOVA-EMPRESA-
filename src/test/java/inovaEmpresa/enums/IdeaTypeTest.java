package inovaEmpresa.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IdeaTypeTest {
    @Test
    void testFromValueValid() {
        int individualValue = 1;
        int groupValue = 2;

        IdeaType individual = IdeaType.fromValue(individualValue);
        IdeaType group = IdeaType.fromValue(groupValue);

        assertEquals(IdeaType.INDIVIDUAL, individual);
        assertEquals(IdeaType.GROUP, group);
    }
    
    @Test
    void testFromValueInvalid() {
        int invalidValue = 99;

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            IdeaType.fromValue(invalidValue);
        });
        assertEquals("invalid priority value" + invalidValue, thrown.getMessage());
    }

    @Test
    void testFromStringValid() {
        String individualString = "INDIVIDUAL";
        String groupString = "group";

        IdeaType individual = IdeaType.fromString(individualString);
        IdeaType group = IdeaType.fromString(groupString);

        assertEquals(IdeaType.INDIVIDUAL, individual);
        assertEquals(IdeaType.GROUP, group);
    }

    @Test
    void testFromStringInvalid() {
        String invalidType = "INVALID";

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            IdeaType.fromString(invalidType);
        });
        assertEquals("Invalid idea type: " + invalidType, thrown.getMessage());
    }
}
