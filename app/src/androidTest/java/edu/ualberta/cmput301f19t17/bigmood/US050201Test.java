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

import static org.junit.Assert.assertTrue;

public class US050201Test {

    private Solo solo;
    private static AppPreferences appPreferences;
    private static MockRepository mockRepository;

    @BeforeClass
    public static void setRepository() {

        // Set app preferences
        US050201Test.appPreferences = AppPreferences.getInstance();

        // Create new in-memory database and set the app preferences to use it
        US050201Test.mockRepository = new MockRepository();
        US050201Test.appPreferences.setRepository(US050201Test.mockRepository);

        // Login with a user from the database using a specialized method in MockRepository
        US050201Test.appPreferences.login(US050201Test.mockRepository.getUser("user2"));

    }

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before //runs before every test
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    // NOT DONE
    @Test
    public void acceptRequestTest(){
        // Logged in as user2 who already has a pending request from user3
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        // Go to Requests and accept the request from user3
        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.title_requests));
        solo.clickOnButton(solo.getCurrentActivity().getResources().getString(R.string.label_request_accept));
        assertTrue(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.toast_success_request_accept), 1, 2000));

        // Go to Following and check that user3's recent mood is there
        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.title_following));
        // waiting on the implementation of displaying the username of the person being followed
    }

    // NOT DONE
    @Test
    public void rejectRequestTest(){
        // Logged in as user2 who already has a pending request from user3
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        // Go to Requests and reject the request from user3
        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.title_requests));
        solo.clickOnButton(solo.getCurrentActivity().getResources().getString(R.string.label_request_reject));
        assertTrue(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.toast_success_request_reject), 1, 2000));

        // Go to Following and check that user3's recent mood is not there
        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.title_following));
        // waiting on the implementation of displaying the username of the person being followed
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

