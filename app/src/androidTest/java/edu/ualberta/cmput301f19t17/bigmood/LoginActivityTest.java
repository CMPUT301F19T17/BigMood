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
import edu.ualberta.cmput301f19t17.bigmood.activity.LoginActivity;
import edu.ualberta.cmput301f19t17.bigmood.database.MockRepository;

import static org.junit.Assert.assertTrue;

/**
 * Test class for LoginActivity. All the UI tests are written here. Robotium test framework is
 used
 */
// unit test to ensure that logic returns desired result
public class LoginActivityTest {
    private Solo solo;
    private static AppPreferences appPreferences;
    private static MockRepository mockRepository;

    @BeforeClass
    public static void setRepository() {

        // Set app preferences
        LoginActivityTest.appPreferences = AppPreferences.getInstance();

        // Create new in-memory database and set the app preferences to use it
        LoginActivityTest.mockRepository = new MockRepository();
        LoginActivityTest.appPreferences.setRepository(LoginActivityTest.mockRepository);

        // Login with a user from the database using a specialized method in MockRepository
        LoginActivityTest.appPreferences.login(LoginActivityTest.mockRepository.getUser("user1"));

    }

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class,true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testWrongUsername() {
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
        solo.typeText(((TextInputLayout) solo.getView(R.id.text_input_username)).getEditText(), "NotForTestingOnly");
        solo.typeText(((TextInputLayout) solo.getView(R.id.text_input_password)).getEditText(), "password");
        solo.clickOnView(solo.getView(R.id.button_login));
        assertTrue(solo.waitForText("Username/password incorrect", 1, 1000));
    }

    @Test
    public void testWrongPassword() {
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
        solo.typeText(((TextInputLayout) solo.getView(R.id.text_input_username)).getEditText(), "ForTestingOnly");
        solo.typeText(((TextInputLayout) solo.getView(R.id.text_input_password)).getEditText(), "HELLO");
        solo.clickOnView(solo.getView(R.id.button_login));
        assertTrue(solo.waitForText("Username/password incorrect", 1, 1000));
    }

    @Test
    public void testCorrectUsernamePassword() {
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
        solo.typeText(((TextInputLayout) solo.getView(R.id.text_input_username)).getEditText(), "user1");
        solo.typeText(((TextInputLayout) solo.getView(R.id.text_input_password)).getEditText(), "p1");
        solo.clickOnView(solo.getView(R.id.button_login));
        solo.assertCurrentActivity("Wrong activity", HomeActivity.class);
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
