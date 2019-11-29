package edu.ualberta.cmput301f19t17.bigmood;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.database.MockRepository;
import edu.ualberta.cmput301f19t17.bigmood.database.User;
import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;
import edu.ualberta.cmput301f19t17.bigmood.model.SocialSituation;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class US060201Test {
    private static AppPreferences appPreferences;
    private static MockRepository mockRepository;
    private UiDevice device;


    @BeforeClass //runs before anything else runs
    public static void setRepository() {

        // Set app preferences
        US060201Test.appPreferences = AppPreferences.getInstance();

        // Create new in-memory database and set the app preferences to use it
        US060201Test.mockRepository = new MockRepository();
        US060201Test.appPreferences.setRepository(US060201Test.mockRepository);

        User currentUser = mockRepository.getUser("user1");

        // Login with user 1 from the database using a specialized method in MockRepository
        US060201Test.appPreferences.login(currentUser);
        mockRepository.deleteAllUserMoods(currentUser);

        //create a calendar on which we base the other calendars
        Calendar baseCalendar = Calendar.getInstance();
        baseCalendar.set(2019, 10, 23, 12, 0);

        //should be the first mood
        Calendar calendar1 = (Calendar) baseCalendar.clone();
        Mood mood1 = new Mood(null, EmotionalState.HAPPINESS, calendar1, SocialSituation.ALONE, "Games are fun", new GeoPoint(53.5184, -113.5023));
        mockRepository.createMood(currentUser, mood1, null, null);
    }

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before
    public void initUIDevice() {
        // Initialize UiDevice instance
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }


    @Test
    public void checkUserMap() throws UiObjectNotFoundException, InterruptedException {

        //go to the map view
        UiObject mapButton = device.findObject(new UiSelector()
                .description("User Maps"));
        if (mapButton.exists()) {
            mapButton.click(); //this will throw a UIObjectNotFoundException if we cannot click following
        }

        Thread.sleep(100000);

        String user1MoodTime = "12:00";

        //now check if there's a mood
        UiObject user1MoodPin1 = device.findObject(new UiSelector()
                .descriptionContains(user1MoodTime));
        assertTrue(user1MoodPin1.exists());
    }
}
