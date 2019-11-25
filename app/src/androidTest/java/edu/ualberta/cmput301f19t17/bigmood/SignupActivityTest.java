package edu.ualberta.cmput301f19t17.bigmood;


import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.textfield.TextInputLayout;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.activity.SignUpActivity;
import edu.ualberta.cmput301f19t17.bigmood.database.MockRepository;

import static org.junit.Assert.assertTrue;

public class SignupActivityTest {

    private Solo solo;
    private static AppPreferences appPreferences;
    private static MockRepository mockRepository;

    private EditText fieldFirstName;
    private EditText fieldLastName;
    private EditText fieldUsername;
    private EditText fieldPassword;
    private EditText fieldConfirmPassword;

    // Not strictly necessary in this case but we should always run this step before
    @BeforeClass
    public static void setRepository() {

        // Set app preferences
        SignupActivityTest.appPreferences = AppPreferences.getInstance();

        // Create new in-memory database and set the app preferences to use it
        SignupActivityTest.mockRepository = new MockRepository();
        SignupActivityTest.appPreferences.setRepository(SignupActivityTest.mockRepository);

        // Login with a user from the database using a specialized method in MockRepository
        SignupActivityTest.appPreferences.login(SignupActivityTest.mockRepository.getUser("user1"));

    }

    @Rule
    public ActivityTestRule<SignUpActivity> rule = new ActivityTestRule<>(SignUpActivity.class, true, true);

    @Before
    public void setUp() throws Exception {

        this.solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        // Get references
        this.fieldFirstName = ((TextInputLayout) solo.getView(R.id.text_input_first_name)).getEditText();
        this.fieldLastName = ((TextInputLayout) solo.getView(R.id.text_input_last_name)).getEditText();
        this.fieldUsername = ((TextInputLayout) solo.getView(R.id.text_input_username)).getEditText();
        this.fieldPassword = ((TextInputLayout) solo.getView(R.id.text_input_password)).getEditText();
        this.fieldConfirmPassword = ((TextInputLayout) solo.getView(R.id.text_input_confirm_password)).getEditText();

    }

    @Test
    public void TestSuccessfulSignup() {

        // Assert current activity
        solo.assertCurrentActivity("Wrong activity", SignUpActivity.class);

        // Clear all EditTexts
        solo.clearEditText(fieldFirstName);
        solo.clearEditText(fieldLastName);
        solo.clearEditText(fieldUsername);
        solo.clearEditText(fieldPassword);
        solo.clearEditText(fieldConfirmPassword);

        // Enter information for new user
        solo.enterText(fieldFirstName, "User");
        solo.enterText(fieldLastName, "4");
        solo.enterText(fieldUsername, "user4");
        solo.enterText(fieldPassword, "p4");
        solo.enterText(fieldConfirmPassword, "p4");

        // Click on button
        solo.clickOnView(solo.getView(R.id.button_sign_up));

        // Check for the toast text
        assertTrue(solo.waitForText(solo.getCurrentActivity().getText(R.string.toast_success_registration).toString(), 1, 5000));

    }

    @Test
    public void TestFailingSignup() {

        // Assert current activity
        solo.assertCurrentActivity("Wrong activity", SignUpActivity.class);

        // Clear all EditTexts
        solo.clearEditText(fieldFirstName);
        solo.clearEditText(fieldLastName);
        solo.clearEditText(fieldUsername);
        solo.clearEditText(fieldPassword);
        solo.clearEditText(fieldConfirmPassword);

        // Enter information for new user
        solo.enterText(fieldFirstName, "User");
        solo.enterText(fieldLastName, "3");
        solo.enterText(fieldUsername, "user3");
        solo.enterText(fieldPassword, "p3");
        solo.enterText(fieldConfirmPassword, "p3");

        // Click on button
        solo.clickOnView(solo.getView(R.id.button_sign_up));

        // Check for the toast text
        assertTrue(solo.waitForText(solo.getCurrentActivity().getText(R.string.toast_error_user_exists).toString(), 1, 5000));

    }

}
