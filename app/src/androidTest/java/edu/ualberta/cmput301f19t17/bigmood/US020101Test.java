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

import static org.junit.Assert.assertTrue;

public class US020101Test {
    private Solo solo;
    private static AppPreferences appPreferences;
    private static MockRepository mockRepository;

    @BeforeClass
    public static void setRepository() {

        // Set app preferences
        US020101Test.appPreferences = AppPreferences.getInstance();

        // Create new in-memory database and set the app preferences to use it
        US020101Test.mockRepository = new MockRepository();
        US020101Test.appPreferences.setRepository(US020101Test.mockRepository);

        // Login with a user from the database using a specialized method in MockRepository
        US020101Test.appPreferences.login(US020101Test.mockRepository.getUser("user1"));
    }

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before //Clears the mood list before each test
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        // Clear the user's mood list
        US020101Test.mockRepository.deleteAllUserMoods(US020101Test.appPreferences.getCurrentUser());
        solo.clickOnText(solo.getCurrentActivity().getText(R.string.title_user_moods).toString(), 2);
        solo.sleep(1500);
    }

    @Test
    public void checkReasonMaxLength(){

        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);

        solo.typeText(((TextInputLayout) solo.getView(R.id.text_input_reason)).getEditText(), "check reason length is too long.");
        solo.clickOnView(solo.getView(R.id.action_save));
        assertTrue(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.error_reason_too_long),1,2000));

    }

    @Test
    public void checkReasonNumWords() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);
        //Add a slight the delay until the dialog has been opened (time may vary)
        //Robotium might lag out if the delay is too low
        solo.sleep(1000);

        solo.typeText(((TextInputLayout) solo.getView(R.id.text_input_reason)).getEditText(), "chk len too long");
        solo.clickOnView(solo.getView(R.id.action_save));
        assertTrue(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.error_reason_word_count),1,2000));
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
