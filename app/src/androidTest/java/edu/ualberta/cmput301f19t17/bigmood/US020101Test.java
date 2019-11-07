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
import edu.ualberta.cmput301f19t17.bigmood.database.MockUser;

public class US020101Test {
    private Solo solo;
    private AppPreferences appPreferences;

    @BeforeClass //runs before anything else runs
    public static void setUpAppPrefs() throws Exception {
        AppPreferences.getInstance().setCurrentUser(new MockUser("CMPUT301", "CMPUT", "301"));
    }

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before //runs before every test
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        appPreferences = AppPreferences.getInstance();

        appPreferences.getRepository().deleteAllMoods(appPreferences.getCurrentUser());
        solo.waitForText("HillyBillyBobTesterino", 0, 10000);
    }
    @Test
    public void checkReasonMaxLength(){

        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);

        solo.enterText(((TextInputLayout) solo.getView(R.id.text_input_reason)).getEditText(), "check reason length is too long.");
        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForText("Reason too long");


    }


}
