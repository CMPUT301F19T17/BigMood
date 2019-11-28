package edu.ualberta.cmput301f19t17.bigmood.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.GeoPoint;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * This class models a mood object.
 */
public class Mood implements Parcelable {

    // Tag for Parceling
    public static final String TAG_MOOD_OBJECT = "edu.ualberta.cmput301f19t17.bigmood.model.MOOD_OBJECT";
    private static final int PARCELABLE_NULL_SITUATION = -78934865;

    // Stored because it is required for deletion. Once we pull the data from the DB there is no way to distinguish it from other Mood objects, so we have to store this information.
    private final String firestoreId;
    private final String imageId;

    // Mood instance variables
    private final EmotionalState state;
    private final Calendar datetime;
    private final SocialSituation situation;
    private final String reason;
    private final GeoPoint location;


    /**
     * Base constructor for a NEW mood. This constructor should not be used to recreate a mood from the database, since the firestoreId will be null.
     * @param imageId   An optional image that the user can attach to the mood.
     * @param state     The emotional state of the mood. This can be any one of the categories defined in the EmotionalState enum.
     * @param datetime  The Calendar object representing the date and time that the mood was created.
     * @param situation An optional social situation of the mood. This can be any one of the categories defined in the SocialSituation enum.
     * @param reason    An optional textual reason describing why the user felt the mood. This cannot be null.
     * @param location  An optional location representing the longitude and latitude of the mood.
     */
    public Mood(String imageId, EmotionalState state, Calendar datetime, SocialSituation situation, String reason, GeoPoint location) {
        this.firestoreId = null;

        if (state == null || datetime == null)
            throw new IllegalArgumentException("`state`and `datetime` are required arguments, and cannot be null.");

        if (reason == null)
            throw new IllegalArgumentException("`reason` cannot be null. Use an empty string instead.");

        this.imageId = imageId;

        this.state = state;
        this.datetime = datetime;
        this.situation = situation;
        this.reason = reason;
        this.location = location;

    }

    /**
     * Base constructor for an OLD mood. This constructor should not be used to create a new mood, as you would have to define a firestoreId.
     * @param firestoreId The Firestore ID associated with the current mood. We have to keep this information within the mood itself so that it can be identified for deletion, for example.
     * @param imageId     An optional image that the user can attach to the mood.
     * @param state       The emotional state of the mood. This can be any one of the categories defined in the EmotionalState enum.
     * @param datetime    The Calendar object representing the date and time that the mood was created.
     * @param situation   An optional social situation of the mood. This can be any one of the categories defined in the SocialSituation enum.
     * @param reason      An optional textual reason describing why the user felt the mood. This cannot be null.
     * @param location    An optional location representing the longitude and latitude of the mood.
     */
    public Mood(String firestoreId, String imageId, EmotionalState state, Calendar datetime, SocialSituation situation, String reason, GeoPoint location) {

        if (state == null || datetime == null)
            throw new IllegalArgumentException("state and datetime are required arguments, and cannot be null.");

        if (reason == null)
            throw new IllegalArgumentException("Reason cannot be null. Use an empty string instead.");

        this.firestoreId = firestoreId;
        this.imageId = imageId;

        this.state = state;
        this.datetime = datetime;
        this.situation = situation;
        this.reason = reason;
        this.location = location;
    }

    /**
     * Gets the Firestore ID associated with the mood. This will not exist if a new mood is created, so this call can return a null.
     * @return Returns the Firestore ID as a <code>String</code> or a <code>null</code> if the information was not included.
     */
    @Nullable
    public String getFirestoreId() {
        return firestoreId;
    }

    /**
     * Gets the emotional state associated with the mood. This should not be null.
     * @return Returns one of the elements defined in {@see edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState}
     */
    @NonNull
    public EmotionalState getState() {
        return state;
    }

    /**
     * Gets the Calendar object associated with the mood. This should not be null.
     * @return Returns the date and time encoded into a calendar object.
     */
    @NonNull
    public Calendar getDatetime() {

        // We clone this as to enforce that the time is mutable.
        return (Calendar) datetime.clone();

    }

    /**
     * Gets the social situation associated with the mood. This is optional, so this call can return a null.
     * @return Returns one of the elements defined in {@see edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState} or a <code>null</code> if the information was not included.
     */
    @Nullable
    public SocialSituation getSituation() {
        return situation;
    }

    /**
     * Gets the textual reason associated with the mood. Although this is optional, it cannot be null.
     * @return Returns a string containing the reason (
     */
    @NonNull
    public String getReason() {
        return reason;
    }

    /**
     * Gets the location associated with the mood. This is optional, so this call can return a null.
     * @return Returns the location as a <code>Geopoint</code> or a <code>null</code> if the information was not included.
     */
    @Nullable
    public GeoPoint getLocation() {
        return location;
    }

    /**
     * Gets the image associated with the mood. This is optional, so this call can return a null.
     * @return Returns the image as a <code>Bitmap</code> or a <code>null</code> if the information was not included.
     */
    @Nullable
    public String getImageId() {
        return imageId;
    }

    /**
     * This method compares THIS mood with another mood to see if they have the same attributes.
     * @param other the other mood
     * @return Returns true if the two moods have the same attributes, and false otherwise
     */
    @Override
    public boolean equals(Object other) {

        // Return true if the object to compare is the object itself
        if (other == this)
            return true;

        // Double check that the object passed in is an instance of this class.
        if (! (other instanceof Mood))
            return false;

        // Cast other to another object
        Mood mood = (Mood) other;

        // Do comparison
        return (this.imageId.equals(mood.getImageId()) &&
                this.state == mood.getState() &&
                this.datetime == mood.getDatetime() &&
                this.situation == mood.getSituation() &&
                this.reason.equals(mood.getReason()) &&
                this.location == mood.getLocation()
        );

    }

    // PARCELABLE METHODS //

    /**
     * This method gets called when Android wants to reconstruct our object. We read in the same order we wrote in.
     *
     * @param in The Parcel with all the information in it
     */
    private Mood(Parcel in) {

        this.firestoreId = in.readString();
        this.imageId = in.readString();

        this.state = EmotionalState.findByStateCode(in.readInt());

        this.datetime = Calendar.getInstance(TimeZone.getTimeZone(in.readString()));
        this.datetime.setTimeInMillis(in.readLong());

        int situationCode = in.readInt();

        // Remember this could have been null, which in that case would be set to the special int value. If it is, we know that it should be null. Otherwise we leave it up to the situation to find the EmotionalState
        if (situationCode == Mood.PARCELABLE_NULL_SITUATION)
            this.situation = null;
        else
            this.situation = SocialSituation.findBySituationCode(situationCode);

        // get reason and geopoint
        this.reason = in.readString();
        this.location = new GeoPoint(in.readLong(), in.readLong());

    }

    /**
     * This method is required for Parcelable. It is used to pass a Mood to another Fragment or Activity.
     * @param out   The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {

        // Write the firestore Id. This can be null.
        out.writeString(this.firestoreId);

        // Write the image Id. This can be null.
        out.writeString(this.imageId);

        // Write the state as a string (from the enumeration). This cannot be null.
        out.writeInt(this.state.getStateCode());

        // Write timestamp and time zone ID. This cannot be null.
        out.writeString(this.datetime.getTimeZone().getID());
        out.writeLong(this.datetime.getTimeInMillis());

        // Write the situation as a string (from the enumeration). We have to handle the else case because of the order-sensitive nature of the parcel writing. We also cannot write an enumeration directly so while this value can be null, we can't just call .getSituationCode() or else we risk a NullPointerException.
        if (this.situation != null)
            out.writeInt(this.situation.getSituationCode());
        else
            out.writeInt(Mood.PARCELABLE_NULL_SITUATION);

        // Write the reason as a string
        out.writeString(this.reason);

        // Write the longitude and latitude
        out.writeDouble(location.getLatitude());
        out.writeDouble(location.getLongitude());

        // TODO write image to parcelable, need to do more research for how to do this
//        out.writeParcelable(image);

    }

    /**
     * All parcelable objects must implement the CREATOR interface which has two methods. The former of which calls the special constructor below.
     */
    public static final Creator<Mood> CREATOR = new Creator<Mood>() {
        @Override
        public Mood createFromParcel(Parcel in) {
            return new Mood(in);
        }

        @Override
        public Mood[] newArray(int size) {
            return new Mood[size];
        }
    };

    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
     * @return a bitmask indicating the set of special object types marshaled by this Parcelable object instance. Value is either 0 or CONTENTS_FILE_DESCRIPTOR
     */
    @Override
    public int describeContents() {
        return 0;
    }

}


