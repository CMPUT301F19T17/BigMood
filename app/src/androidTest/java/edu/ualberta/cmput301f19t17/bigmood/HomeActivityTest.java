package edu.ualberta.cmput301f19t17.bigmood;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;

import com.google.android.material.textfield.TextInputLayout;
import com.robotium.solo.Solo;
import androidx.test.rule.ActivityTestRule;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.database.MockUser;

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

    @After
    public void cleanUp() {
        appPreferences.getRepository().deleteAllMoods(appPreferences.getCurrentUser());
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
        solo.enterText((EditText) solo.getView(R.id.text_input_reason), "I am grossed out");

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForDialogToClose();

        ListAdapter moodArrayAdapter = ((ListView) solo.getView(R.id.mood_list)).getAdapter();

        //make sure we correctly added the mood
        assertEquals(1, moodArrayAdapter.getCount());
    }

    @Test
    public void checkEditMood() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);

        solo.clickOnView(fab);
        solo.pressSpinnerItem(0, 3); //disgusted
        solo.pressSpinnerItem(1, 2); //two to several
        solo.enterText((EditText) solo.getView(R.id.text_input_reason), "I am grossed out");

        solo.clickOnView(solo.getView(R.id.action_save));
        ListAdapter moodArrayAdapter = ((ListView) solo.getView(R.id.mood_list)).getAdapter();
        int originalNumListItems = moodArrayAdapter.getCount();


        solo.clickInList(0); //select the mood we just created (it will be 0 since the list is sorted newest -> oldest)

        solo.clickOnButton("EDIT");
        solo.waitForText("Edit Mood"); //make sure DefineMoodDialogFragment opens itself correctly as a "Edit" rather than "Add"

        solo.pressSpinnerItem(0, 1); //sad
        solo.pressSpinnerItem(1, 3); //crowd

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForText("SADNESS"); //make sure the edit worked
        //make sure no new items were added, and no items deleted
        assertEquals(originalNumListItems, moodArrayAdapter.getCount());
    }

    @Test
    public void checkDeleteMood() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        ListView moodList = (ListView) solo.getView(R.id.mood_list);
        ListAdapter moodArrayAdapter = moodList.getAdapter();
        int originalNumListItems = moodArrayAdapter.getCount();

        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);

        solo.clickOnView(fab);
        solo.pressSpinnerItem(0, 3); //disgusted
        solo.pressSpinnerItem(1, 2); //two to several
        solo.enterText(((TextInputLayout) solo.getView(R.id.text_input_reason)).getEditText(), "I am grossed out");

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForText("DISGUST");

        solo.clickInList(0); //select the mood we just created

        solo.clickOnButton("DELETE");


        //make sure there are no new elements in the list (ie, after we added the mood, it was deleted)
        assertEquals(originalNumListItems, moodArrayAdapter.getCount());
    }


}
