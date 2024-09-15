package inovaEmpresa.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IdeaType {
    INDIVIDUAL(1),
    GROUP(2);

    private final int value;
    public static IdeaType fromValue(int value){
        for (IdeaType ideaType : IdeaType.values()){
            if (ideaType.value == value){
                return ideaType;
            }
        }
        throw  new IllegalArgumentException("invalid priority value"+ value);
    }

    public static IdeaType fromString(String type) {
        for (IdeaType ideaType : IdeaType.values()) {
            if (ideaType.name().equalsIgnoreCase(type)) {
                return ideaType;
            }
        }
        throw new IllegalArgumentException("Invalid idea type: " + type);
    }
}
