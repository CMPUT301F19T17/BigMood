package edu.ualberta.cmput301f19t17.bigmood;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;

import com.robotium.solo.Solo;
import androidx.test.rule.ActivityTestRule;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;

public class HomeActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkAddMood() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);

        solo.clickOnView(fab);
        solo.pressSpinnerItem(0, 3); //disgusted
        solo.pressSpinnerItem(1, 2); //two to several
        solo.enterText((EditText) solo.getView(R.id.tiedittext_reason), "I am grossed out");

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForText("DISGUST");


    }

    @Test
    public void checkEditMood() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);

        solo.clickOnView(fab);
        solo.pressSpinnerItem(0, 3); //disgusted
        solo.pressSpinnerItem(1, 2); //two to several
        solo.enterText((EditText) solo.getView(R.id.tiedittext_reason), "I am grossed out");

        solo.clickOnView(solo.getView(R.id.action_save));

        solo.clickInList(0); //select the mood we just created

        solo.clickOnButton("EDIT");
    }


}
