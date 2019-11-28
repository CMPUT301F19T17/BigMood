package edu.ualberta.cmput301f19t17.bigmood;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Calendar;

import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.database.MockRepository;
import edu.ualberta.cmput301f19t17.bigmood.database.User;
import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;
import edu.ualberta.cmput301f19t17.bigmood.model.SocialSituation;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class US060301Test {
    private static AppPreferences appPreferences;
    private static MockRepository mockRepository;
    private static final String BASIC_SAMPLE_PACKAGE
            = "edu.ualberta.cmput301f19t17.bigmood";
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String STRING_TO_BE_TYPED = "UiAutomator";
    private UiDevice device;


    @BeforeClass //runs before anything else runs
    public static void setRepository() {

        // Set app preferences
        US060301Test.appPreferences = AppPreferences.getInstance();

        // Create new in-memory database and set the app preferences to use it
        US060301Test.mockRepository = new MockRepository();
        US060301Test.appPreferences.setRepository(US060301Test.mockRepository);

        // Login with user 1 from the database using a specialized method in MockRepository
        US060301Test.appPreferences.login(US060301Test.mockRepository.getUser("user1"));

        // user1 follows user2 and user3, so we clear both of their moods, then add some new ones
        User follow2 = mockRepository.getUser("user2");
        User follow3 = mockRepository.getUser("user3");
        mockRepository.deleteAllUserMoods(follow2);
        mockRepository.deleteAllUserMoods(follow3);

        //create a calendar on which we base the other calendars
        Calendar baseCalendar = Calendar.getInstance();
        baseCalendar.set(2019, 10, 23, 12, 0);

        //should be the first mood
        Calendar calendar1 = (Calendar) baseCalendar.clone();
        calendar1.add(Calendar.MINUTE, 5);
        Mood mood1 = new Mood(EmotionalState.HAPPINESS, calendar1, SocialSituation.ALONE, "Games are fun", new GeoPoint(53.5184, -113.5023), null);
        mockRepository.createMood(follow2, mood1, null, null);

        //should be the second mood
        Calendar calendar2 = (Calendar) baseCalendar.clone();
        Mood mood2 = new Mood(EmotionalState.ANGER, calendar2, SocialSituation.CROWD, "Line too long", new GeoPoint(53.5179, -113.4965), null);
        mockRepository.createMood(follow3, mood2, null, null);
    }

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before
    public void initUIDevice() {
        // Initialize UiDevice instance
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }


    @Test
    public void checkFollowingMap() throws UiObjectNotFoundException {
        // switch to the Following tab
        UiObject following = device.findObject(new UiSelector()
                .text("Following"));
        if (following.exists()) {
            following.click(); //this will throw a UIObjectNotFoundException if we cannot click following
        }

        //go to the map view
        UiObject mapButton = device.findObject(new UiSelector()
                .description("Following Maps"));
        if (mapButton.exists()) {
            mapButton.click(); //this will throw a UIObjectNotFoundException if we cannot click following
        }

        String user2MoodTime = "12:05";
        String user3MoodTime = "12:00";
        //make sure there are 2 pins in the correct locations
        //first pin
        UiObject user2MoodPin = device.findObject(new UiSelector()
                .descriptionContains(user2MoodTime));
        assertTrue(user2MoodPin.exists());

        //second pin
        UiObject user3MoodPin = device.findObject(new UiSelector()
                .descriptionContains(user3MoodTime));
        assertTrue(user3MoodPin.exists());
    }
}
