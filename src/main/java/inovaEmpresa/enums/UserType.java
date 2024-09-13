package inovaEmpresa.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public enum UserType {
    ADMIN(1),
    evaluators(2),
    collaborators(3);

    private final int velue;

    public static UserType fromVelue(int value){
        for (UserType userType : UserType.values()){
            if (userType.velue==value){
                return userType;
            }
        }
        throw  new IllegalArgumentException("invalid priority value"+ value);
    }
}
