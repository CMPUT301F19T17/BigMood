package edu.ualberta.cmput301f19t17.bigmood;

import android.view.View;
import android.widget.Spinner;

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

        // Clear the user's mood list
        US010301Test.mockRepository.deleteAllUserMoods(US010301Test.appPreferences.getCurrentUser());

    }

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before //Clears the mood list before each test
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test // A basic mood refers to a mood where only the state is provided and the social situation, reason, photograph, and map are not provided
    public void testDisplayBasicMood() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        // Add a happy mood
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);
        View stateSpinner = solo.getView(R.id.spinner_state);
        solo.clickOnView(stateSpinner);
        solo.clickOnText(EmotionalState.HAPPINESS.toString());
        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForDialogToClose();

        // Check the ViewMoodDialogFragment
        solo.clickInList(1, 0);
        Integer imageViewDrawableID = (Integer) solo.getView(R.id.image_view_placeholder_emote).getTag();
        assertTrue(EmotionalState.HAPPINESS.getDrawableId() == imageViewDrawableID);
        assertTrue(solo.waitForText(EmotionalState.HAPPINESS.toString(), 1, 2000));
        assertTrue(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.placeholder_situation), 1, 2000));
        assertTrue(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.placeholder_reason), 1, 2000));

        Integer photographImageViewTag = (Integer) solo.getView(R.id.image_view_placeholder_photo).getTag();
        assertTrue(photographImageViewTag == R.drawable.ic_placeholder_image_black_24dp);

        // Needs to be refactored b/c at the time this was written the location is hardcoded to [32.32 N, 142.22 E] and not null
        // When refactoring go to ViewMoodDialogFragment and reposition setTag for map image view
        Integer mapImageViewTag = (Integer) solo.getView(R.id.image_view_placeholder_location).getTag();
        assertTrue(mapImageViewTag == R.drawable.ic_placeholder_image_black_24dp);
    }

    @Test // A full mood refers to a mood where the state, social situation, reason, photograph, and map are all provided
    public void testDisplayFullMood() {
        // Add an anger mood
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);

        View stateSpinner = solo.getView(R.id.spinner_state);
        solo.clickOnView(stateSpinner);
        solo.clickOnText(EmotionalState.ANGER.toString());

        View situationSpinner = solo.getView(R.id.situation_spinner);
        solo.clickOnView(situationSpinner);
        solo.clickOnText(SocialSituation.CROWD.toString());

        solo.typeText(((TextInputLayout) solo.getView(R.id.text_input_reason)).getEditText(),"some reason");
        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForDialogToClose();

        // Check the ViewMoodDialogFragment
        solo.clickInList(1, 0);
        Integer imageViewDrawableID = (Integer) solo.getView(R.id.image_view_placeholder_emote).getTag();
        assertTrue(EmotionalState.ANGER.getDrawableId() == imageViewDrawableID);
        assertTrue(solo.waitForText(EmotionalState.ANGER.toString(), 1, 2000));
        assertTrue(solo.waitForText(SocialSituation.CROWD.toString(), 1, 2000));
        assertTrue(solo.waitForText("some reason", 1, 2000));
        // missing photograph and map check
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
