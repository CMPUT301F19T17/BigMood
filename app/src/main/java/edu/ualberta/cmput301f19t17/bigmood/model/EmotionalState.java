package edu.ualberta.cmput301f19t17.bigmood.model;

import androidx.annotation.Nullable;

/**
 * This Enumeration defines the categories for an emotional state. It also is associated with a stateCode so that we can store an enumeration in a database.
 */
public enum EmotionalState {

    HAPPINESS (0),
    SADNESS (1),
    FEAR (2),
    DISGUST (3),
    ANGER (4),
    SURPRISE (5),
    ;

    private int stateCode;

    /**
     * Constructor that allows each state to be associated with a code.
     * @param stateCode The stateCode the EmotionalState should be associated with
     */
    EmotionalState(int stateCode) {
        this.stateCode = stateCode;
    }

    /**
     * This method retrieves the state code from the enum category
     * @return Returns an integer state code.
     */
    public int getStateCode() {
        return stateCode;
    }

    /**
     * This method allows the reverse lookup of a state code into a member of the enumeration. This would be used to retrieve the state from an integer taken from a database entry.
     * @param code The code to look up
     * @return     Returns either a member of the enum or a null if the state code does not map to any member.
     */
    @Nullable
    public static EmotionalState findByStateCode(int code) {

        for (EmotionalState state : EmotionalState.values())
            if (code == state.getStateCode())
                return state;

        return null;

    }

}
