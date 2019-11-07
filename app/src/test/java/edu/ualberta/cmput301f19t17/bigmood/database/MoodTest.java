package edu.ualberta.cmput301f19t17.bigmood.database;

import com.google.firebase.firestore.GeoPoint;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Calendar;

import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;
import edu.ualberta.cmput301f19t17.bigmood.model.SocialSituation;

public class MoodTest {
    private Mood mockMood(Calendar calendar, GeoPoint location) {
        return new Mood(EmotionalState.ANGER, calendar, SocialSituation.ALONE, "Angry cause alone",
                location, null);
    }

    @Test
    public void testGetters() {
        Calendar calendar = Calendar.getInstance();
        GeoPoint gp = new GeoPoint(53.34, 60.0);

        Mood mood = mockMood(calendar, gp);

        assertEquals(EmotionalState.ANGER, mood.getState());
        assertEquals(calendar, mood.getDatetime());
        assertEquals(SocialSituation.ALONE, mood.getSituation());
        assertEquals("Angry cause alone", mood.getReason());
        assertEquals(gp, mood.getLocation());
        assertNull(mood.getImage());
        assertNull(mood.getFirestoreId());
    }

}
