package edu.ualberta.cmput301f19t17.bigmood;


import android.app.Activity;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.ThrowOnExtraProperties;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.activity.LoginActivity;

/**
 * Test class for ViewUserMoodDialogFragment. All the UI tests are written here.
 * Robotium test framework is used.
 */
public class ViewUserMoodDialogFragmentTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);


    /**
     * Runs before all tests. Creates solo instances for LoginActivity and HomeActivity.
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void LoginActivity(){
        solo.assertCurrentActivity("Wrong activity",LoginActivity.class);
        solo.typeText(((TextInputLayout) solo.getView(R.id.text_input_username)).getEditText(), "CMPUT301");
        solo.typeText(((TextInputLayout) solo.getView(R.id.text_input_password)).getEditText(), "password");
        solo.clickOnView(solo.getView(R.id.button_login));
        solo.clickOnButton("Log In");
        solo.assertCurrentActivity("Wrong activity",HomeActivity.class);
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);

    }


}
