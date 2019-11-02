package edu.ualberta.cmput301f19t17.bigmood.model;

import android.graphics.Bitmap;

import com.google.firebase.firestore.GeoPoint;

import java.util.Calendar;

/**
 * Ignore this class, this is for Firestore Testing purposes
 */
public class TestMood {

    // Stored because it is required for deletion. Once we pull the data from the DB there is no way to distinguish it from other Mood objects, so we have to store this information.
    private final String firestoreId;

    private EmotionalState state;
    private Calendar datetime;
    private SocialSituation situation;
    private String reason;
    private GeoPoint location;
    private Bitmap image;


    public TestMood(EmotionalState state, Calendar datetime) {
        this.firestoreId = null;

        if (state == null || datetime == null)
            throw new IllegalArgumentException("`state`and `datetime` are required arguments, and cannot be null.");

        this.state = state;
        this.datetime = datetime;

        this.situation = null;
        this.reason = "";
        this.location = null;
        this.image = null;

    }

    public TestMood(EmotionalState state, Calendar datetime, SocialSituation situation, String reason, GeoPoint location, Bitmap image) {
        this.firestoreId = null;

        if (state == null || datetime == null)
            throw new IllegalArgumentException("`state`and `datetime` are required arguments, and cannot be null.");

        if (reason == null)
            throw new IllegalArgumentException("Reason cannot be null. Use an empty string instead.");

        this.state = state;
        this.datetime = datetime;
        this.situation = situation;
        this.reason = reason;
        this.location = location;
        this.image = image;

    }

    public TestMood(String firestoreId, EmotionalState state, Calendar datetime, SocialSituation situation, String reason, GeoPoint location, Bitmap image) {

        if (state == null || datetime == null)
            throw new IllegalArgumentException("state and datetime are required arguments, and cannot be null.");

        if (reason == null)
            throw new IllegalArgumentException("Reason cannot be null. Use an empty string instead.");

        this.firestoreId = firestoreId;

        this.state = state;
        this.datetime = datetime;
        this.situation = situation;
        this.reason = reason;
        this.location = location;
        this.image = image;
    }

    public String getFirestoreId() {
        return firestoreId;
    }

    public EmotionalState getState() {
        return state;
    }

    public void setState(EmotionalState state) {
        this.state = state;
    }

    public Calendar getDatetime() {
        return datetime;
    }

    public void setDatetime(Calendar datetime) {
        this.datetime = datetime;
    }

    public SocialSituation getSituation() {
        return situation;
    }

    public void setSituation(SocialSituation situation) {
        this.situation = situation;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

}


