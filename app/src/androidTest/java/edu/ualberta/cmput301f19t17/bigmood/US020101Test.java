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
        // Delete all previous mood
        US020101Test.mockRepository.deleteAllUserMoods(US020101Test.mockRepository.getUser("user1"));
    }

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before //Clears the mood list before each test
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        appPreferences = AppPreferences.getInstance();
        solo.sleep(1000);
    }

    @Test
    public void checkReasonMaxLength(){

        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);

        solo.enterText(((TextInputLayout) solo.getView(R.id.text_input_reason)).getEditText(), "check reason length is too long.");
        solo.clickOnView(solo.getView(R.id.action_save));
        assertTrue(solo.waitForText("Reason too long"));

    }

    @Test
    public void checkReasonNumWords() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);

        solo.enterText(((TextInputLayout) solo.getView(R.id.text_input_reason)).getEditText(), "chk len too long");
        solo.clickOnView(solo.getView(R.id.action_save));
        assertTrue(solo.waitForText("Reason cannot be longer than 3 words"));
    }


}
