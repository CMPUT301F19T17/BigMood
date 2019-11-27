package edu.ualberta.cmput301f19t17.bigmood;

import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.textfield.TextInputLayout;
import com.robotium.solo.Solo;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.database.MockRepository;
import edu.ualberta.cmput301f19t17.bigmood.database.MockUser;
import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;
import edu.ualberta.cmput301f19t17.bigmood.model.SocialSituation;

import static org.junit.Assert.assertEquals;
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
//      appPreferences.getRepository().deleteAllMoods(appPreferences.getCurrentUser());\
        //solo.waitForText("HillyBillyBobTesterino", 0, 1000);

//        AppPreferences.getInstance().getRepository().deleteAllMoods(AppPreferences.getInstance().getCurrentUser());
        //solo.sleep(3000);
    }

/*    @AfterClass //runs after all tests have run
    public static void cleanUp() {
//        AppPreferences.getInstance().getRepository().deleteAllMoods(AppPreferences.getInstance().getCurrentUser());
    }*/

    @Test
    public void checkAddMood() {
        // Add a mood
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);

        solo.pressSpinnerItem(0, EmotionalState.DISGUST.getStateCode()); //state = Disgusted
        solo.pressSpinnerItem(3, SocialSituation.SEVERAL.getSituationCode()); //situation = Two to several people
        solo.typeText(((TextInputLayout) solo.getView(R.id.text_input_reason)).getEditText(), "got puked on"); //reason = got puked on

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForDialogToClose();

        //make sure the added mood is displayed


        /*ListAdapter moodArrayAdapter = ((ListView) solo.getView(R.id.mood_list)).getAdapter();

        assertTrue(solo.waitForText(EmotionalState.DISGUST.toString()));
        assertEquals(1, moodArrayAdapter.getCount());*/
    }

    @Test
    public void checkInvalidReason() {

    }

}
