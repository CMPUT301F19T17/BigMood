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
        EmotionalState emotionalStateHappy = EmotionalState.HAPPINESS;
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);
        solo.sleep(5000); // Wait 5s for the dialog fragment to come up, otherwise the wrong thing will be clicked.

        View stateSpinner = solo.getView(R.id.spinner_state);
        solo.clickOnView(stateSpinner);
        solo.clickOnText(emotionalStateHappy.toString());

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForDialogToClose();

        // Check the mood list
        Integer listItemDrawableID = (Integer) solo.getView(R.id.mood_item_emoticon).getTag();
        assertTrue(emotionalStateHappy.getDrawableId() == listItemDrawableID);
        assertTrue(solo.waitForText(emotionalStateHappy.toString(), 1, 2000));

        // Check the ViewMoodDialogFragment
        solo.clickInList(1, 0);
        Integer imageViewDrawableID = (Integer) solo.getView(R.id.image_view_placeholder_emote).getTag();
        assertTrue(emotionalStateHappy.getDrawableId() == imageViewDrawableID);
        assertTrue(solo.waitForText(emotionalStateHappy.toString(), 1, 2000));

    }

    @Test
    public void testSadEmoticon() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        // Add a sad mood
        EmotionalState emotionalStateSad = EmotionalState.SADNESS;
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);
        solo.sleep(5000); // Wait 5s for the dialog fragment to come up, otherwise the wrong thing will be clicked.

        View stateSpinner = solo.getView(R.id.spinner_state);
        solo.clickOnView(stateSpinner);
        solo.clickOnText(emotionalStateSad.toString());

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForDialogToClose();

        // Check the mood list
        Integer listItemDrawableID = (Integer) solo.getView(R.id.mood_item_emoticon).getTag();
        assertTrue(emotionalStateSad.getDrawableId() == listItemDrawableID);
        assertTrue(solo.waitForText(emotionalStateSad.toString(), 1, 2000));

        // Check the ViewMoodDialogFragment
        solo.clickInList(1, 0);
        Integer imageViewDrawableID = (Integer) solo.getView(R.id.image_view_placeholder_emote).getTag();
        assertTrue(emotionalStateSad.getDrawableId() == imageViewDrawableID);
        assertTrue(solo.waitForText(emotionalStateSad.toString(), 1, 2000));
    }

    @Test
    public void testAngerEmoticon() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        // Add an anger mood
        EmotionalState emotionalStateAnger = EmotionalState.ANGER;
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);
        solo.sleep(5000); // Wait 5s for the dialog fragment to come up, otherwise the wrong thing will be clicked.

        View stateSpinner = solo.getView(R.id.spinner_state);
        solo.clickOnView(stateSpinner);
        solo.clickOnText(emotionalStateAnger.toString());

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForDialogToClose();

        // Check the mood list
        Integer listItemDrawableID = (Integer) solo.getView(R.id.mood_item_emoticon).getTag();
        assertTrue(emotionalStateAnger.getDrawableId() == listItemDrawableID);
        assertTrue(solo.waitForText(emotionalStateAnger.toString(), 1, 2000));

        // Check the ViewMoodDialogFragment
        solo.clickInList(1, 0);
        Integer imageViewDrawableID = (Integer) solo.getView(R.id.image_view_placeholder_emote).getTag();
        assertTrue(emotionalStateAnger.getDrawableId() == imageViewDrawableID);
        assertTrue(solo.waitForText(emotionalStateAnger.toString(), 1, 2000));
    }

    @Test
    public void testDisgustEmoticon() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        // Add a disgust mood
        EmotionalState emotionalStateDisgust = EmotionalState.DISGUST;
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);
        solo.sleep(5000); // Wait 5s for the dialog fragment to come up, otherwise the wrong thing will be clicked.

        View stateSpinner = solo.getView(R.id.spinner_state);
        solo.clickOnView(stateSpinner);
        solo.clickOnText(emotionalStateDisgust.toString());

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForDialogToClose();

        // Check the mood list
        Integer listItemDrawableID = (Integer) solo.getView(R.id.mood_item_emoticon).getTag();
        assertTrue(emotionalStateDisgust.getDrawableId() == listItemDrawableID);
        assertTrue(solo.waitForText(emotionalStateDisgust.toString(), 1, 2000));

        // Check the ViewMoodDialogFragment
        solo.clickInList(1, 0);
        Integer imageViewDrawableID = (Integer) solo.getView(R.id.image_view_placeholder_emote).getTag();
        assertTrue(emotionalStateDisgust.getDrawableId() == imageViewDrawableID);
        assertTrue(solo.waitForText(emotionalStateDisgust.toString(), 1, 2000));
    }

    @Test
    public void testFearEmoticon() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        // Add a fear mood
        EmotionalState emotionalStateFear = EmotionalState.FEAR;
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);
        solo.sleep(5000); // Wait 5s for the dialog fragment to come up, otherwise the wrong thing will be clicked.

        View stateSpinner = solo.getView(R.id.spinner_state);
        solo.clickOnView(stateSpinner);
        solo.clickOnText(emotionalStateFear.toString());

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForDialogToClose();

        // Check the mood list
        Integer listItemDrawableID = (Integer) solo.getView(R.id.mood_item_emoticon).getTag();
        assertTrue(emotionalStateFear.getDrawableId() == listItemDrawableID);
        assertTrue(solo.waitForText(emotionalStateFear.toString(), 1, 2000));

        // Check the ViewMoodDialogFragment
        solo.clickInList(1, 0);
        Integer imageViewDrawableID = (Integer) solo.getView(R.id.image_view_placeholder_emote).getTag();
        assertTrue(emotionalStateFear.getDrawableId() == imageViewDrawableID);
        assertTrue(solo.waitForText(emotionalStateFear.toString(), 1, 2000));
    }

    @Test
    public void testSurpriseEmoticon() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        // Add a surprise mood
        EmotionalState emotionalStateSurprise = EmotionalState.SURPRISE;
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);
        solo.sleep(5000); // Wait 5s for the dialog fragment to come up, otherwise the wrong thing will be clicked.

        View stateSpinner = solo.getView(R.id.spinner_state);
        solo.clickOnView(stateSpinner);
        solo.clickOnText(emotionalStateSurprise.toString());

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForDialogToClose();

        // Check the mood list
        Integer listItemDrawableID = (Integer) solo.getView(R.id.mood_item_emoticon).getTag();
        assertTrue(emotionalStateSurprise.getDrawableId() == listItemDrawableID);
        assertTrue(solo.waitForText(emotionalStateSurprise.toString(), 1, 2000));

        // Check the ViewMoodDialogFragment
        solo.clickInList(1, 0);
        Integer imageViewDrawableID = (Integer) solo.getView(R.id.image_view_placeholder_emote).getTag();
        assertTrue(emotionalStateSurprise.getDrawableId() == imageViewDrawableID);
        assertTrue(solo.waitForText(emotionalStateSurprise.toString(), 1, 2000));
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
