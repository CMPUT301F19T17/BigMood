package edu.ualberta.cmput301f19t17.bigmood;

import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.textfield.TextInputLayout;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.Map;

import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.database.MockRepository;
import edu.ualberta.cmput301f19t17.bigmood.fragment.dialog.MapDialogFragment;
import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;

import static org.junit.Assert.assertTrue;

public class US060201Test {
    private Solo solo;
    private static AppPreferences appPreferences;
    private static MockRepository mockRepository;

    @BeforeClass
    public static void setRepository() {

        // Set app preferences
        US060201Test.appPreferences = AppPreferences.getInstance();

        // Create new in-memory database and set the app preferences to use it
        US060201Test.mockRepository = new MockRepository();
        US060201Test.appPreferences.setRepository(US060201Test.mockRepository);

        // Login with a user from the database using a specialized method in MockRepository
        US060201Test.appPreferences.login(US060201Test.mockRepository.getUser("user1"));
    }

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        // Clear the user's mood list
        US060201Test.mockRepository.deleteAllUserMoods(US060201Test.appPreferences.getCurrentUser());
        solo.clickOnText(solo.getCurrentActivity().getText(R.string.title_user_moods).toString(), 2);
        solo.sleep(1500);
    }

    @Test
    public void testUserMoodMap() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        //No mood added

        View userMap = solo.getCurrentActivity().findViewById(R.id.action_maps_user);
        solo.clickOnView(userMap);
        assertTrue(solo.waitForLogMessage(InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.title_user_maps)));
        assertTrue(solo.waitForLogMessage(InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.toast_error_mood_adapter_empty)));
        solo.goBack();

        //Add a Happy mood

        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);
            //Add a slight the delay until the dialog has been opened (time may vary)
            //Robotium might lag out if the delay is too low
        solo.sleep(1000);
        solo.pressSpinnerItem(0, EmotionalState.HAPPINESS.getStateCode());
        solo.clickOnView(solo.getView(R.id.action_save));
        solo.clickOnView(userMap);
        assertTrue(solo.waitForLogMessage(InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.title_user_maps)));
        assertTrue(solo.waitForLogMessage(InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.toast_success_mood_marker_added)+1));

    }
}
