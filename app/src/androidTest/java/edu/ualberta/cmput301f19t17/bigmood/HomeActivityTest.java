package edu.ualberta.cmput301f19t17.bigmood;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toolbar;

import androidx.test.platform.app.InstrumentationRegistry;

import com.robotium.solo.Solo;
import androidx.test.rule.ActivityTestRule;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.database.MockUser;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;

import static org.junit.Assert.assertEquals;

public class HomeActivityTest {
    private Solo solo;
    private AppPreferences appPreferences;

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        appPreferences = AppPreferences.getInstance();
        appPreferences.setCurrentUser(new MockUser("CMPUT301", "CMPUT", "301"));
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
        solo.enterText((EditText) solo.getView(R.id.reason_edit_text), "I am grossed out");

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
        solo.enterText((EditText) solo.getView(R.id.reason_edit_text), "I am grossed out");

        solo.clickOnView(solo.getView(R.id.action_save));

        solo.clickInList(0); //select the mood we just created

        solo.clickOnButton("EDIT");
        solo.waitForText("Edit Mood"); //make sure DefineMoodDialogFragment checks itself correctly

        solo.pressSpinnerItem(0, 1); //sad
        solo.pressSpinnerItem(1, 3); // crowd

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForText("SADNESS"); //make sure the edit worked
    }

    @Test
    public void checkDeleteMood() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);

        solo.clickOnView(fab);
        solo.pressSpinnerItem(0, 3); //disgusted
        solo.pressSpinnerItem(1, 2); //two to several
        solo.enterText((EditText) solo.getView(R.id.reason_edit_text), "I am grossed out");

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForText("DISGUST");

        solo.clickInList(0); //select the mood we just created

        solo.clickOnButton("DELETE");
        ListView moodList = (ListView) solo.getView(R.id.mood_list);
        ListAdapter moodArrayAdapter = moodList.getAdapter();

        //make sure there are no elements in the list
        assertEquals(0, moodArrayAdapter.getCount());
    }


}
