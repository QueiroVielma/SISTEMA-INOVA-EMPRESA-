package inovaEmpresa.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public enum UserType {
    ADMIN(1),
    EVALUATOR(2),
    COLLABORATOR(3);

    private final int value;

    public static UserType fromValue(int value){
        for (UserType userType : UserType.values()){
            if (userType.value == value){
                return userType;
            }
        }
        throw  new IllegalArgumentException("invalid priority value"+ value);
    }

    public static UserType fromString(String type) {
        for (UserType userType : UserType.values()) {
            if (userType.name().equalsIgnoreCase(type)) {
                return userType;
            }
        }
        throw new IllegalArgumentException("Invalid user type: " + type);
    }
}
