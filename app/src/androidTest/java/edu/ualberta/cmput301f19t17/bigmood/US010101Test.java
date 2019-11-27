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

// TODO: 2019-11-06 Cameron: remove waits (replace with MockRepository calls)
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

    @Test
    public void checkInvalidReasonWord() {
        // Add a reason that consists of more than 3 words
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);

        String reasonMoreThan3Words = "I got puked on";
        solo.typeText(((TextInputLayout) solo.getView(R.id.text_input_reason)).getEditText(), reasonMoreThan3Words);

        solo.clickOnView(solo.getView(R.id.action_save));

        assertTrue(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.error_reason_word_count),1,3000));
    }

    @Test
    public void checkInvalidReasonChar() {
        // Add a reason that has more than 20 characters
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);

        String reasonMoreThan20Char = "abcdefghijklmnopqrstuvwxyz";
        solo.typeText(((TextInputLayout) solo.getView(R.id.text_input_reason)).getEditText(), reasonMoreThan20Char);

        solo.clickOnView(solo.getView(R.id.action_save));

        assertTrue(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.error_reason_too_long),1,3000));

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
