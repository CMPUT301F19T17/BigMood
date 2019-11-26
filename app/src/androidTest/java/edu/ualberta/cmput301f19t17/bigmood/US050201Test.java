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
        US050201Test.appPreferences.login(US050201Test.mockRepository.getUser("user1"));

    }

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before //runs before every test
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void acceptRequestTest(){
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        // switch to the Following tab
        solo.clickOnText("Requests");
        solo.clickOnButton("ACCEPT");
        assertTrue(solo.waitForText("Request successfully accepted.", 1, 2000));

    }
    @Test
    public void rejectRequestTest(){
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        // switch to the Following tab
        solo.clickOnText("Requests");
        solo.clickOnButton("REJECT");
        assertTrue(solo.waitForText("Request successfully rejected.", 1, 2000));


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

