package edu.ualberta.cmput301f19t17.bigmood.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import edu.ualberta.cmput301f19t17.bigmood.R;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_AZURE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_MAGENTA;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_VIOLET;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_YELLOW;

/**
 * This Enumeration defines the categories for an emotional state. It also is associated with a state code, a display name, and a drawable ID.
 */
public enum EmotionalState {

    HAPPINESS (0, "Happy", R.drawable.ic_emoticon_happy, HUE_YELLOW),
    SADNESS (1, "Sad", R.drawable.ic_emoticon_sad, HUE_AZURE),
    FEAR (2, "Afraid", R.drawable.ic_emoticon_fear, HUE_VIOLET),
    DISGUST (3, "Disgusted", R.drawable.ic_emoticon_disgust, HUE_GREEN),
    ANGER (4, "Angry", R.drawable.ic_emoticon_anger, HUE_RED),
    SURPRISE (5, "Surprised", R.drawable.ic_emoticon_surprise, HUE_MAGENTA),
    ;

    private int stateCode;
    private String displayName;
    private int drawableId;
    private float markerColor;

    /**
     * Constructor that allows each state to be associated with a code.
     * @param stateCode The stateCode the EmotionalState should be associated with
     */
    EmotionalState(int stateCode, String displayName, int drawableId, float markerColor) {
        this.stateCode = stateCode;
        this.displayName = displayName;
        this.drawableId = drawableId;
        this.markerColor = markerColor;
    }

    /**
     * This method retrieves the state code from the enum category
     * @return Returns an integer state code.
     */
    public int getStateCode() {
        return this.stateCode;
    }

    /**
     * Returns a resource id of the drawable representing the state
     * @return The resource ID of the drawable vector image representing the state.
     */
    public int getDrawableId() {
        return this.drawableId;
    }

    /**
     * Returns the float value of the marker color on the Map
     * @return float for color
     */
    public float getMarkerColor() {return this.markerColor;}

    /**
     * This method converts an EmotionalState into a string
     * @return the displayName of the EmotionalState
     */
    @NonNull
    @Override
    public String toString() {
        return this.displayName;
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
