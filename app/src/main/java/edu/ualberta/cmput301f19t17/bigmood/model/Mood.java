package edu.ualberta.cmput301f19t17.bigmood.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * This class is used to represent a user's mood.
 * It is able to hold the date, time, emotional state,
 * reason for mood, social situation, the physical location
 * and an image showing why the user felt this way.
 */
public class Mood implements Parcelable {

    public static final String TAG_MOOD_OBJECT = "edu.ualberta.cmput301f19t17.bigmood.model.MOOD_OBJECT";

    //PRIVATE VARIABLES
    private String date;
    private String time;
    private String state;
    private String reason;
    private String situation;
    private Pair<Double, Double> location;
    private Bitmap image;

    //PARCELABLE REQUIRED METHODS

    /**
     * This constructor is required for Parcelable. It is used to create a Mood by passing it as a Parcel.
     *
     * @param in the information that is brought in
     */
    protected Mood(Parcel in) {
        date = in.readString();
        time = in.readString();
        state = in.readString();
        reason = in.readString();
        situation = in.readString();
        //TODO get image, need more research
        //image = in.readParcelable(Bitmap.class.getClassLoader());
    }

    /**
     * This method is required for Parcelable. It is used to pass a Mood to another Fragment or Activity.
     *
     * @param out the parcel we are passing out
     * @param i   TODO find out what i is for
     */
    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(date);
        out.writeString(time);
        out.writeString(state);
        out.writeString(reason);
        out.writeString(situation);
        out.writeDouble(location.first);
        out.writeDouble(location.second);
        //TODO write image to parcelable, need to do more research for how to do this
        //out.writeParcelable(image);
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    //CONSTRUCTOR

    /**
     * This is the minimal constructor with just the minimal elements needed to make a mood.
     * It also initializes values to defaults in case the getters are called before they are
     * given real values, so as to avoid creating an Exception.
     *
     * @param date  The date that the mood was felt. This is the current date, not inputted by the user.
     * @param time  The time that the mood was felt. This is the current time, not inputted by the user.
     * @param state The emotional state that created the mood. This is inputted by the user.
     */
    public Mood(@NonNull String date, @NonNull String time, @NonNull String state) {
        this.date = date;
        this.time = time;
        this.state = state;
        reason = "";
        situation = "";
        //TODO Cameron Oct 31 2019 initialize location and image
    }

    /**
     * This is the overloaded constructor, with all available elements to make a mood.
     *
     * @param date      The date that the mood was felt. This is the current date, not inputted by the user.
     * @param time      The time that the mood was felt. This is the current time, not inputted by the user.
     * @param state     The emotional state that created the mood. This is inputted by the user.
     * @param reason    The reason why the user felt the mood. This is inputted by the user.
     * @param situation The social situation that the mood happened in. This is inputted by the user.
     * @param location  The location that the mood happened at. This is inputted by the user.
     * @param image     An image representing the mood. This is inputted by the user.
     */
    public Mood(@NonNull String date, @NonNull String time, @NonNull String state,
                @Nullable String reason, @Nullable String situation,
                @Nullable Pair<Double, Double> location, @Nullable Bitmap image) {
        this.date = date;
        this.time = time;
        this.state = state;
        this.reason = reason;
        this.situation = situation;
        this.location = location;
        this.image = image;
    }


    //GETTERS

    /**
     * Gets the date that the mood was added.
     *
     * @return date.
     */
    public String getDate() {
        return date;
    }

    /**
     * Gets the time that the mood was added.
     *
     * @return time.
     */
    public String getTime() {
        return time;
    }

    /**
     * Gets the emotional state of the mood.
     *
     * @return emotional state.
     */
    public String getState() {
        return state;
    }

    /**
     * Gets the reason why the mood was felt.
     * May not have been initialized, since it is not necessary.
     * In this case, we return an empty string
     * @return reason why mood was felt.
     */
    public String getReason() {
        if (reason.length()==0) {
            return "";
        }
        return reason;
    }

    /**
     * Gets the social situation that the mood was felt in.
     *
     * @return social situation.
     */
    public String getSituation() {
        return situation;
    }

    /**
     * Gets the location that the mood happened at.
     *
     * @return location.
     */
    public Pair<Double, Double> getLocation() {
        return location;
    }

    /**
     * Gets the Bitmap of the image that represents the mood.
     *
     * @return image
     */
    public Bitmap getImage() {
        return image;
    }


    //SETTERS

    // Note: There are no setters for date and time as they are
    // supposed to be the date and time that the mood was added, and as such
    // should not be changed

    /**
     * Changes the emotional state of the mood.
     *
     * @param state The new emotional state.
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Changes the reason why the mood was felt.
     *
     * @param reason the new reason.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Changes the social situation of the mood.
     *
     * @param situation the new social situation
     */
    public void setSituation(String situation) {
        this.situation = situation;
    }

    /**
     * Changes the location of the mood.
     *
     * @param location the new location
     */
    public void setLocation(Pair<Double, Double> location) {
        this.location = location;
    }

    /**
     * Changes the image associated with the mood.
     *
     * @param image the new image
     */
    public void setImage(Bitmap image) {
        this.image = image;
    }

}
