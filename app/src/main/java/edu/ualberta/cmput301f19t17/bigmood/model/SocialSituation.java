package edu.ualberta.cmput301f19t17.bigmood.model;

import androidx.annotation.Nullable;

/**
 * This Enumeration defines the categories for an social situation. It also is associated with a situationCode so that we can store an enumeration in a database.
 */
public enum SocialSituation {

    ONE (0),
    SEVERAL (1),
    CROWD (2),
    ;

    private int situationCode;

    /**
     * Constructor that allows each state to be associated with a code.
     * @param situationCode
     */
    SocialSituation(int situationCode) {
        this.situationCode = situationCode;
    }

    /**
     * This method retrieves the situation code from the enum category
     * @return
     */
    public int getSituationCode() {
        return situationCode;
    }

    /**
     * This method allows the reverse lookup of a situation code into a member of the enumeration. This would be used to retrieve the state from an integer taken from a database entry.
     * @param code The code to look up
     * @return     Returns either a member of the enum or a null if the state code does not map to any member.
     */
    @Nullable
    public static SocialSituation findBySituationCode(int code) {

        for (SocialSituation situation : SocialSituation.values())
            if (code == situation.getSituationCode())
                return situation;

        return null;

    }

}
