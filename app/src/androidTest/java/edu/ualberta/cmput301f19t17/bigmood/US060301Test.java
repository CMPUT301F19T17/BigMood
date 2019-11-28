package edu.ualberta.cmput301f19t17.bigmood;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.GeoPoint;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.Calendar;

import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.database.MockRepository;
import edu.ualberta.cmput301f19t17.bigmood.database.User;
import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;
import edu.ualberta.cmput301f19t17.bigmood.model.SocialSituation;

public class US060301Test {
    private Solo solo;
    private static AppPreferences appPreferences;
    private static MockRepository mockRepository;


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
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, 10, 23, 12, 0);

        //first mood with Location at BeerCade on WhyteAve
        Mood mood1 = new Mood(EmotionalState.HAPPINESS, calendar, SocialSituation.ALONE, "I like beer.", new GeoPoint(53.5184, -113.5023), null);
        mockRepository.createMood(follow2, mood1, null, null);

        //second mood with Location at the Funky Buda on WhyteAve
        Mood mood2 = new Mood(EmotionalState.ANGER, calendar, SocialSituation.CROWD, "Line is long.", new GeoPoint(53.5179, -113.4965), null);
        mockRepository.createMood(follow3, mood2, null, null);

    }

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before //runs before every test
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void checkFollowingMap() {
        //we are logged in as user1, which means we follow user2 and user3

        // switch to the Following tab
        solo.clickOnText("Following");

        //go to the map view
        solo.clickOnView(solo.getView(R.id.action_maps_following));

        //solo.click
        solo.waitForText("");
        solo.sleep(10000);

    }
}
