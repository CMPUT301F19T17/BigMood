package edu.ualberta.cmput301f19t17.bigmood;

import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.textfield.TextInputLayout;
import com.robotium.solo.Solo;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.regex.Pattern;

import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.database.MockUser;
import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;
import edu.ualberta.cmput301f19t17.bigmood.model.SocialSituation;

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
    private AppPreferences appPreferences;

    @BeforeClass //runs before anything else runs
    public static void setUpAppPrefs() throws Exception {
        AppPreferences.getInstance().setCurrentUser(new MockUser("CMPUT301", "CMPUT", "301"));
    }

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before //runs before every test
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        appPreferences = AppPreferences.getInstance();
        appPreferences.getRepository().deleteAllMoods(appPreferences.getCurrentUser());
        // This is actually the sweet spots for sleep time
        // 1 second would be too low and my trash laptop cant handle that :(
        solo.sleep(2000);
    }
    @AfterClass //runs after all tests have run
    public static void cleanUp() {
        AppPreferences.getInstance().getRepository().deleteAllMoods(AppPreferences.getInstance().getCurrentUser());
    }

    @Test
    public void checkSort() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        // switch to the Following tab
        solo.clickOnText("Following");

        //make sure the item at the top is the newly added item
        //gotta use Pattern.quote because it's related somehow to the way Robotium sees string
        //link: https://stackoverflow.com/questions/17741680/robotium-for-android-solo-searchtext-not-working
        // TODO: 2019-11-12 Cameron: change the following to make sure we check times properly, we will likely have to change this, since we will likely be adding moods to a second user and following them, without altering the moods in between
        solo.clickOnMenuItem("Happy");
        assertTrue(solo.searchText(Pattern.quote("at time +" + 1000 +"s")));

        solo.clickOnImage(R.drawable.ic_close_black_24dp);

        //make sure the second item is the previously added item
        solo.clickOnMenuItem("Disgust");
        assertTrue(solo.searchText(Pattern.quote("at time 0")));

    }
}
