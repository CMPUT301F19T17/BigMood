package edu.ualberta.cmput301f19t17.bigmood;

import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.textfield.TextInputLayout;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.database.MockRepository;
import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;
import edu.ualberta.cmput301f19t17.bigmood.model.SocialSituation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class US010301Test {
    private Solo solo;
    private static AppPreferences appPreferences;
    private static MockRepository mockRepository;

    @BeforeClass
    public static void setRepository() {

        // Set app preferences
        US010301Test.appPreferences = AppPreferences.getInstance();

        // Create new in-memory database and set the app preferences to use it
        US010301Test.mockRepository = new MockRepository();
        US010301Test.appPreferences.setRepository(US010301Test.mockRepository);

        // Login with a user from the database using a specialized method in MockRepository
        US010301Test.appPreferences.login(US010301Test.mockRepository.getUser("user1"));
    }

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        // Clear the user's mood list
        US010301Test.mockRepository.deleteAllUserMoods(US010301Test.appPreferences.getCurrentUser());
        solo.clickOnText(solo.getCurrentActivity().getText(R.string.title_user_moods).toString(), 2);
        solo.sleep(1500);
    }

    @Test
    public void testDisplayMood() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        // Add a surprise mood
        EmotionalState emotionalState = EmotionalState.SURPRISE;
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);
        solo.sleep(2000); // Wait 5s for the dialog fragment to come up, otherwise the wrong thing will be clicked.
        View stateSpinner = solo.getView(R.id.spinner_state);
        solo.clickOnView(stateSpinner);
        solo.clickOnText(emotionalState.toString());

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForDialogToClose();

        // Check the ViewMoodDialogFragment
        solo.clickInList(1, 0);
        Integer imageViewDrawableID = (Integer) solo.getView(R.id.image_view_placeholder_emote).getTag();
        assertTrue(emotionalState.getDrawableId() == imageViewDrawableID);
        assertTrue(solo.waitForText(emotionalState.toString(), 1, 2000));
        assertTrue(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.placeholder_situation), 1, 2000));
        assertTrue(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.placeholder_reason), 1, 2000));
        assertTrue(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.label_no_image),1, 2000));
        assertTrue(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.label_no_location),1, 2000));

    }

    @Test
    public void testViewUserMood() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);
        solo.sleep(1000);
        solo.pressSpinnerItem(0, EmotionalState.HAPPINESS.getStateCode());
        solo.clickOnView(solo.getView(R.id.action_save));

        solo.clickInList(1, 0);
        assertTrue(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.menu_option_edit), 1, 2000));
        assertTrue(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.menu_option_delete), 1, 2000));
    }

    @Test
    public void testViewFollowingMood() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.title_following));
        solo.clickInList(1, 0);
        assertFalse(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.menu_option_edit), 1, 2000));
        assertFalse(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.menu_option_delete), 1, 2000));
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
