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

import static org.junit.Assert.assertTrue;

/**
 * Test class for DefineMoodDialogFragment. All the UI tests are written here. Robotium test framework is used.
 */
public class US010101Test {
    private Solo solo;
    private static AppPreferences appPreferences;
    private static MockRepository mockRepository;

    @BeforeClass //Runs before anything else runs. Sets up the MockRepository for testing purposes.
    public static void setRepository() {

        // Set app preferences
        US010101Test.appPreferences = AppPreferences.getInstance();

        // Create new in-memory database and set the app preferences to use it
        US010101Test.mockRepository = new MockRepository();
        US010101Test.appPreferences.setRepository(US010101Test.mockRepository);

        // Login with a preset user already in MockRepository
        US010101Test.appPreferences.login(US010101Test.mockRepository.getUser("user1"));

    }

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before //runs before every test
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        // Delete all moods and refresh the list manually. We click on the second mathc because we are already in the user moods (and the title is the first match). I realize this is a bit hacky but Robotium doesn't exactly make it easy to click on the navigation bar
        US010101Test.mockRepository.deleteAllUserMoods(appPreferences.getCurrentUser());
        solo.clickOnText(solo.getCurrentActivity().getText(R.string.title_user_moods).toString(), 2);
        solo.sleep(1500);

    }

    @Test
    public void checkAddMood() {
        // Add a mood
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);
        solo.sleep(5000); // Wait 5s for the dialog fragment to come up, otherwise the wrong thing will be clicked.

        View stateSpinner = solo.getView(R.id.spinner_state);
        solo.clickOnView(stateSpinner);
        solo.clickOnText(EmotionalState.DISGUST.toString());

        View situationSpinner = solo.getView(R.id.situation_spinner);
        solo.clickOnView(situationSpinner);
        solo.clickOnText(SocialSituation.SEVERAL.toString());

        String reason = "got puked on";
        solo.typeText(((TextInputLayout) solo.getView(R.id.text_input_reason)).getEditText(), reason);

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForDialogToClose();

        // Check the mood list for the mood just added
        Integer listItemDrawableID = (Integer) solo.getView(R.id.mood_item_emoticon).getTag();
        assertTrue(EmotionalState.DISGUST.getDrawableId() == listItemDrawableID);
        assertTrue(solo.waitForText(EmotionalState.DISGUST.toString(),1,2000));

        // Check the details
        solo.clickInList(1,0);
        Integer imageViewDrawableID = (Integer) solo.getView(R.id.image_view_placeholder_emote).getTag();
        assertTrue(EmotionalState.DISGUST.getDrawableId() == imageViewDrawableID);
        assertTrue(solo.waitForText(EmotionalState.DISGUST.toString(),1,2000));
        assertTrue(solo.waitForText(SocialSituation.SEVERAL.toString(),1,2000));
        assertTrue(solo.waitForText(reason,1,2000));

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
