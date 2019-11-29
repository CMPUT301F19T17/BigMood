package edu.ualberta.cmput301f19t17.bigmood;

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
import edu.ualberta.cmput301f19t17.bigmood.database.MockUser;
import edu.ualberta.cmput301f19t17.bigmood.database.User;

import static org.junit.Assert.assertTrue;

public class US050101Test {
    private Solo solo;
    private static AppPreferences appPreferences;
    private static MockRepository mockRepository;

    @BeforeClass
    public static void setRepository() {

        // Set app preferences
        US050101Test.appPreferences = AppPreferences.getInstance();

        // Create new in-memory database and set the app preferences to use it
        US050101Test.mockRepository = new MockRepository();
        US050101Test.appPreferences.setRepository(US050101Test.mockRepository);

        // Login with a user from the database using a specialized method in MockRepository
        US050101Test.appPreferences.login(US050101Test.mockRepository.getUser("user3"));

    }

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before //Clears the mood list before each test
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void TestUserDoesNotExist() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.title_profile));

        String requested_username = "SuperMario"; // SuperMario will not be in the database
        solo.typeText(((TextInputLayout) solo.getView(R.id.text_input_username)).getEditText(), requested_username);
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.button_request));
        assertTrue(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.toast_error_user_dne), 1, 2000));

        solo.sleep(5000);
    }

    @Test
    public void TestAlreadyFollowing() {
        US050101Test.appPreferences.getInstance().login(US050101Test.mockRepository.getUser("user1"));
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.title_profile));
        User requestedUser = US050101Test.mockRepository.getUser("user2");
        solo.typeText(((TextInputLayout) solo.getView(R.id.text_input_username)).getEditText(), requestedUser.getUsername());
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.button_request));
        assertTrue(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.toast_error_create_request), 1, 2000));
        solo.sleep(2000);
    }

    @Test
    public void TestRequestAlreadyExists() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.title_profile));
        User requestedUser = US050101Test.mockRepository.getUser("user2");
        solo.typeText(((TextInputLayout) solo.getView(R.id.text_input_username)).getEditText(), requestedUser.getUsername());
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.button_request));
        assertTrue(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.toast_error_create_request), 1, 2000));
        solo.sleep(2000);
    }

    @Test
    public void TestRequestCreated() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.title_profile));

        String requester_username = US050101Test.appPreferences.getCurrentUser().getUsername();
        User requestedUser =  US050101Test.mockRepository.getUser("user1");

        solo.typeText(((TextInputLayout) solo.getView(R.id.text_input_username)).getEditText(), requestedUser.getUsername());
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.button_request));
        assertTrue(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.toast_success_request_sent), 1, 2000));

        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.title_user_moods));
        solo.sleep(2000);
        US050101Test.appPreferences.getInstance().login(requestedUser);
        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.title_profile));
        assertTrue(solo.waitForText(requestedUser.getUsername(), 1, 2000));
        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.title_requests));
        assertTrue(solo.waitForText(requester_username, 1, 2000));
        solo.sleep(5000);
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
