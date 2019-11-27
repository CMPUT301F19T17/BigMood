package edu.ualberta.cmput301f19t17.bigmood;

import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.database.MockRepository;
import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;

import static org.junit.Assert.assertTrue;

public class US010201Test {
    private Solo solo;
    private static AppPreferences appPreferences;
    private static MockRepository mockRepository;

    // Sets up MockRepository to be used for testing. Gets ran before everything else.
    @BeforeClass
    public static void setRepository() {

        // Set app preferences
        US010201Test.appPreferences = AppPreferences.getInstance();

        // Create new in-memory database and set the app preferences to use it
        US010201Test.mockRepository = new MockRepository();
        US010201Test.appPreferences.setRepository(US010201Test.mockRepository);

        // Login with a user from the database using a specialized method in MockRepository
        US010201Test.appPreferences.login(US010201Test.mockRepository.getUser("user1"));
    }

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before //runs before every test
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        // Clear the user's mood list
        US010201Test.mockRepository.deleteAllUserMoods(US010201Test.appPreferences.getCurrentUser());
        solo.clickOnText(solo.getCurrentActivity().getText(R.string.title_user_moods).toString(), 2);
        solo.sleep(1500);
    }

    @Test
    public void testHappyEmoticon() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        // Add a happy mood
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);
        //Add a slight the delay until the dialog has been opened (time may vary)
        //Robotium might lag out if the delay is too low
        solo.sleep(1000);

        View stateSpinner = solo.getView(R.id.spinner_state);
        solo.clickOnView(stateSpinner);
        solo.clickOnText(EmotionalState.HAPPINESS.toString());

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForDialogToClose();

        // Check the mood list
        Integer listItemDrawableID = (Integer) solo.getView(R.id.mood_item_emoticon).getTag();
        assertTrue(EmotionalState.HAPPINESS.getDrawableId() == listItemDrawableID);
        assertTrue(solo.waitForText(EmotionalState.HAPPINESS.toString(), 1, 2000));

        // Check the ViewMoodDialogFragment
        solo.clickInList(1, 0);
        Integer imageViewDrawableID = (Integer) solo.getView(R.id.image_view_placeholder_emote).getTag();
        assertTrue(EmotionalState.HAPPINESS.getDrawableId() == imageViewDrawableID);
        assertTrue(solo.waitForText(EmotionalState.HAPPINESS.toString(), 1, 2000));

    }

    @Test
    public void testSadEmoticon() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        // Add a sad mood
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);
        //Add a slight the delay until the dialog has been opened (time may vary)
        //Robotium might lag out if the delay is too low
        solo.sleep(1000);

        View stateSpinner = solo.getView(R.id.spinner_state);
        solo.clickOnView(stateSpinner);
        solo.clickOnText(EmotionalState.SADNESS.toString());

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForDialogToClose();

        // Check the mood list
        Integer listItemDrawableID = (Integer) solo.getView(R.id.mood_item_emoticon).getTag();
        assertTrue(EmotionalState.SADNESS.getDrawableId() == listItemDrawableID);
        assertTrue(solo.waitForText(EmotionalState.SADNESS.toString(), 1, 2000));

        // Check the ViewMoodDialogFragment
        solo.clickInList(1, 0);
        Integer imageViewDrawableID = (Integer) solo.getView(R.id.image_view_placeholder_emote).getTag();
        assertTrue(EmotionalState.SADNESS.getDrawableId() == imageViewDrawableID);
        assertTrue(solo.waitForText(EmotionalState.SADNESS.toString(), 1, 2000));
    }

    @Test
    public void testAngerEmoticon() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        // Add an anger mood
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);
        //Add a slight the delay until the dialog has been opened (time may vary)
        //Robotium might lag out if the delay is too low
        solo.sleep(1000);

        View stateSpinner = solo.getView(R.id.spinner_state);
        solo.clickOnView(stateSpinner);
        solo.clickOnText(EmotionalState.ANGER.toString());

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForDialogToClose();

        // Check the mood list
        Integer listItemDrawableID = (Integer) solo.getView(R.id.mood_item_emoticon).getTag();
        assertTrue(EmotionalState.ANGER.getDrawableId() == listItemDrawableID);
        assertTrue(solo.waitForText(EmotionalState.ANGER.toString(), 1, 2000));

        // Check the ViewMoodDialogFragment
        solo.clickInList(1, 0);
        Integer imageViewDrawableID = (Integer) solo.getView(R.id.image_view_placeholder_emote).getTag();
        assertTrue(EmotionalState.ANGER.getDrawableId() == imageViewDrawableID);
        assertTrue(solo.waitForText(EmotionalState.ANGER.toString(), 1, 2000));
    }

    @Test
    public void testDisgustEmoticon() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        // Add a disgust mood
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);
        //Add a slight the delay until the dialog has been opened (time may vary)
        //Robotium might lag out if the delay is too low
        solo.sleep(1000);

        View stateSpinner = solo.getView(R.id.spinner_state);
        solo.clickOnView(stateSpinner);
        solo.clickOnText(EmotionalState.DISGUST.toString());

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForDialogToClose();

        // Check the mood list
        Integer listItemDrawableID = (Integer) solo.getView(R.id.mood_item_emoticon).getTag();
        assertTrue(EmotionalState.DISGUST.getDrawableId() == listItemDrawableID);
        assertTrue(solo.waitForText(EmotionalState.DISGUST.toString(), 1, 2000));

        // Check the ViewMoodDialogFragment
        solo.clickInList(1, 0);
        Integer imageViewDrawableID = (Integer) solo.getView(R.id.image_view_placeholder_emote).getTag();
        assertTrue(EmotionalState.DISGUST.getDrawableId() == imageViewDrawableID);
        assertTrue(solo.waitForText(EmotionalState.DISGUST.toString(), 1, 2000));
    }

    @Test
    public void testFearEmoticon() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        // Add a fear mood
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);
        //Add a slight the delay until the dialog has been opened (time may vary)
        //Robotium might lag out if the delay is too low
        solo.sleep(1000);

        View stateSpinner = solo.getView(R.id.spinner_state);
        solo.clickOnView(stateSpinner);
        solo.clickOnText(EmotionalState.FEAR.toString());

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForDialogToClose();

        // Check the mood list
        Integer listItemDrawableID = (Integer) solo.getView(R.id.mood_item_emoticon).getTag();
        assertTrue(EmotionalState.FEAR.getDrawableId() == listItemDrawableID);
        assertTrue(solo.waitForText(EmotionalState.FEAR.toString(), 1, 2000));

        // Check the ViewMoodDialogFragment
        solo.clickInList(1, 0);
        Integer imageViewDrawableID = (Integer) solo.getView(R.id.image_view_placeholder_emote).getTag();
        assertTrue(EmotionalState.FEAR.getDrawableId() == imageViewDrawableID);
        assertTrue(solo.waitForText(EmotionalState.FEAR.toString(), 1, 2000));
    }

    @Test
    public void testSurpriseEmoticon() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        // Add a surprise mood
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);
        //Add a slight the delay until the dialog has been opened (time may vary)
        //Robotium might lag out if the delay is too low
        solo.sleep(1000);

        View stateSpinner = solo.getView(R.id.spinner_state);
        solo.clickOnView(stateSpinner);
        solo.clickOnText(EmotionalState.SURPRISE.toString());

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForDialogToClose();

        // Check the mood list
        Integer listItemDrawableID = (Integer) solo.getView(R.id.mood_item_emoticon).getTag();
        assertTrue(EmotionalState.SURPRISE.getDrawableId() == listItemDrawableID);
        assertTrue(solo.waitForText(EmotionalState.SURPRISE.toString(), 1, 2000));

        // Check the ViewMoodDialogFragment
        solo.clickInList(1, 0);
        Integer imageViewDrawableID = (Integer) solo.getView(R.id.image_view_placeholder_emote).getTag();
        assertTrue(EmotionalState.SURPRISE.getDrawableId() == imageViewDrawableID);
        assertTrue(solo.waitForText(EmotionalState.SURPRISE.toString(), 1, 2000));
    }

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

    /**
     * Clears the mood list after all tests are finished
     * @throws Exception
     */
    @AfterClass //runs after all tests have run
    public static void cleanUp() {
        // Clear the user's mood list
        US010201Test.mockRepository.deleteAllUserMoods(US010201Test.appPreferences.getCurrentUser());
    }
}
