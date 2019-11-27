package edu.ualberta.cmput301f19t17.bigmood;

import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.textfield.TextInputLayout;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.regex.Pattern;

import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.database.MockRepository;
import edu.ualberta.cmput301f19t17.bigmood.database.MockUser;
import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;
import edu.ualberta.cmput301f19t17.bigmood.model.SocialSituation;

import static org.junit.Assert.assertTrue;

public class US040101Test {
    private Solo solo;
    private static AppPreferences appPreferences;
    private static MockRepository mockRepository;

    @BeforeClass
    public static void setRepository() {

        // Set app preferences
        US040101Test.appPreferences = AppPreferences.getInstance();

        // Create new in-memory database and set the app preferences to use it
        US040101Test.mockRepository = new MockRepository();
        US040101Test.appPreferences.setRepository(US040101Test.mockRepository);

        // Login with a user from the database using a specialized method in MockRepository
        US040101Test.appPreferences.login(US040101Test.mockRepository.getUser("user1"));

        // Clear the user's mood list
        US040101Test.mockRepository.deleteAllUserMoods(US040101Test.appPreferences.getCurrentUser());

    }

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before //Clears the mood list before each test
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void checkSort() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);

        solo.clickOnView(fab);
        solo.pressSpinnerItem(0, EmotionalState.DISGUST.getStateCode()); //disgusted
        //solo.enterText(((TextInputLayout) solo.getView(R.id.text_input_reason)).getEditText(), "I am grossed out");
        solo.typeText(((TextInputLayout) solo.getView(R.id.text_input_reason)).getEditText(), "at time 0");

        solo.clickOnView(solo.getView(R.id.action_save));

        //wait for 10 second before adding another mood
        // TODO: 2019-11-07 Cameron: Remove and find a better method to check this, perhaps by creating a specific testUser for this problem, and dont delete the moods after testing
        int wait_time = 10;
        solo.sleep(wait_time*1000);

        solo.clickOnView(fab);
        solo.pressSpinnerItem(0, EmotionalState.HAPPINESS.getStateCode());
        solo.typeText(((TextInputLayout) solo.getView(R.id.text_input_reason)).getEditText(), "at time +" + wait_time +"s");

        solo.clickOnView(solo.getView(R.id.action_save));

        solo.sleep(1000);

        //make sure the item at the top is the newly added item
        //gotta use Pattern.quote because it's related somehow to the way Robotium sees string
        //link: https://stackoverflow.com/questions/17741680/robotium-for-android-solo-searchtext-not-working
        solo.clickOnMenuItem("Happy");
        assertTrue(solo.searchText(Pattern.quote("at time +" + wait_time +"s")));

        //I dont know how to press the X button in ViewMoodDialogFragment, so we will just press edit, and then close the fragment
        solo.clickOnButton("EDIT");
        solo.clickOnView(solo.getView(R.id.action_save));

        solo.sleep(1000);
        //make sure the second item is the previously added item
        solo.clickOnMenuItem("Disgust");
        assertTrue(solo.searchText(Pattern.quote("at time 0")));
    }

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
