package edu.ualberta.cmput301f19t17.bigmood;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.textfield.TextInputLayout;
import com.robotium.solo.Solo;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.Calendar;
import java.util.regex.Pattern;

import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.database.MockRepository;
import edu.ualberta.cmput301f19t17.bigmood.database.MockUser;
import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;
import edu.ualberta.cmput301f19t17.bigmood.model.SocialSituation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class is to test the User Story US 05.03.01
 * The test will be very similar to US 04.01.01, since the only difference between these two is the
 * location that the Moods are brought from.
 * In this class we are making sure that the moods belonging to the users that the main user is
 * following show up in reverse chronological order.
 */
// TODO: 2019-11-25 Cameron: Refactor to use MockRepository
public class US050301Test {
    private Solo solo;
    private static AppPreferences appPreferences;
    private static MockRepository mockRepository;


    @BeforeClass //runs before anything else runs
    public static void setRepository() {

        // Set app preferences
        US050301Test.appPreferences = AppPreferences.getInstance();

        // Create new in-memory database and set the app preferences to use it
        US050301Test.mockRepository = new MockRepository();
        US050301Test.appPreferences.setRepository(US050301Test.mockRepository);

        // Login with user 1 from the database using a specialized method in MockRepository
        US050301Test.appPreferences.login(US050301Test.mockRepository.getUser("user1"));

    }

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before //runs before every test
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void checkSort() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        // switch to the Following tab
        solo.clickOnText("Following");

        //make sure the item at the top is the newly added item
        //gotta use Pattern.quote because it's related somehow to the way Robotium sees string
        //link: https://stackoverflow.com/questions/17741680/robotium-for-android-solo-searchtext-not-working
        solo.clickOnMenuItem("Surprised");

        // This is the time that should be at the top, since user1 follows user2 and user3, and this is the time that user3's most
        // recent mood was added
        String user2MoodDate = "2019-11-23";
        String user2MoodTime = "12:12";

        assertTrue(solo.waitForText(Pattern.quote(user2MoodDate)));
        assertTrue(solo.waitForText(Pattern.quote(user2MoodTime)));

        solo.clickOnImage(0);

        //make sure the second item is the previously added item
        solo.clickOnMenuItem("Disgust");

        // This is the time that should be second, since user1 follows user2 and user3, and this is the time that user2's most
        // recent mood was added
        String user3MoodDate = "2019-11-23";
        String user3MoodTime = "12:08";

        assertTrue(solo.waitForText(Pattern.quote(user3MoodDate)));
        assertTrue(solo.waitForText(Pattern.quote(user3MoodTime)));
    }
}
