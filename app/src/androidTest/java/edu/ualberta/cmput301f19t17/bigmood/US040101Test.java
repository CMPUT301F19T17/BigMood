package edu.ualberta.cmput301f19t17.bigmood;

import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.textfield.TextInputLayout;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.database.MockUser;
import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;
import edu.ualberta.cmput301f19t17.bigmood.model.SocialSituation;

import static org.junit.Assert.assertEquals;

public class US040101Test {
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
        // TODO: 2019-11-06 Cameron:
        solo.waitForText("HillyBillyBobTesterino", 0, 10000);
    }

    @Test
    public void checkFilterMood() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);


        //get message from async update before checking number of items in list
        solo.waitForText("HillyBillyBobTesterino", 0, 10000);

        ListView moodList = (ListView) solo.getView(R.id.mood_list);
        ListAdapter moodArrayAdapter = moodList.getAdapter();

        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);

        // Create 2 Mood for each state

        int mood_quantity = 2;
        int state_quantity = EmotionalState.values().length;

        for (EmotionalState state : EmotionalState.values()) {
            for (int i = 0; i < mood_quantity; i++) {
                solo.clickOnView(fab);
                solo.pressSpinnerItem(0, state.getStateCode());
                solo.pressSpinnerItem(1, SocialSituation.SEVERAL.getSituationCode()); //two to several
                solo.enterText(((TextInputLayout) solo.getView(R.id.text_input_reason)).getEditText(), "I am " + state.toString() + i);
                solo.clickOnView(solo.getView(R.id.action_save));
                solo.waitForText(state.toString(), i+1, 1000);
            }
        }
        // This assert also guarantees the filter at startup stay at None
        // solo.waitForText("HillyBillyBobTesterino", 0, 10000);
        assertEquals(mood_quantity*state_quantity, moodArrayAdapter.getCount());

        View filter = solo.getCurrentActivity().findViewById(R.id.action_filter);
        solo.clickOnView(filter);
        for (EmotionalState state : EmotionalState.values()) {
            // select a mood and re-click the filter to make it disappear
            solo.clickOnMenuItem(state.toString());
            solo.clickOnView(filter);
            solo.waitForText(state.toString(), mood_quantity, 1000);
            // the number of mood show should be equal to the number of mood being filtered
            // solo.waitForText("HillyBillyBobTesterino", 0, 10000);
            assertEquals(mood_quantity, moodArrayAdapter.getCount());
        }

        // Go back to None filter, it should show full moods

        solo.clickOnMenuItem("None");
        solo.clickOnView(filter);
        assertEquals(mood_quantity*state_quantity, moodArrayAdapter.getCount());
        solo.clickOnView(filter);
        // We select a random mood i in the list try to Edit/Delete it
        Random random = new Random();
        int i = random.nextInt(state_quantity);
        solo.clickOnMenuItem(EmotionalState.findByStateCode(i).toString());
            // The mood get edited to a random mood j
        solo.clickOnButton("EDIT");
        solo.waitForText("Edit Mood", 1, 1000); //make sure DefineMoodDialogFragment opens itself correctly as a "Edit" rather than "Add"

            //Select a random state j other than the one we created
        int j = random.nextInt(state_quantity);
            // i/2 will return the state code
        while (i == j) {
            j = random.nextInt(state_quantity);
        }

        solo.pressSpinnerItem(0, j);
        solo.pressSpinnerItem(1, SocialSituation.CROWD.getSituationCode()); //crowd

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForText(EmotionalState.findByStateCode(j).toString(), mood_quantity+1,1000);
        solo.waitForText(EmotionalState.findByStateCode(i).toString(), mood_quantity-1, 1000);
        solo.waitForText("HillyBillyBobTesterino", 0, 10000);
        assertEquals(mood_quantity*state_quantity, moodArrayAdapter.getCount());


    }

    @Test
    public void checkFilteredMood() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);


        //get message from async update before checking number of items in list
        solo.waitForText("HillyBillyBobTesterino", 0, 10000);

        ListView moodList = (ListView) solo.getView(R.id.mood_list);
        ListAdapter moodArrayAdapter = moodList.getAdapter();

        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);

        // Create 2 Mood for each state

        int mood_quantity = 2;
        int state_quantity = EmotionalState.values().length;

        for (EmotionalState state : EmotionalState.values()) {
            for (int i = 0; i < mood_quantity; i++) {
                solo.clickOnView(fab);
                solo.pressSpinnerItem(0, state.getStateCode());
                solo.pressSpinnerItem(1, SocialSituation.SEVERAL.getSituationCode()); //two to several
                solo.enterText(((TextInputLayout) solo.getView(R.id.text_input_reason)).getEditText(), "I am " + state.toString() + i);
                solo.clickOnView(solo.getView(R.id.action_save));
                solo.waitForText(state.toString(), i + 1, 1000);
            }
        }

        // Attempt to add/edit/delete mood inside of a filter
        View filter = solo.getCurrentActivity().findViewById(R.id.action_filter);
        solo.clickOnView(filter);
        solo.clickOnMenuItem("Happy");

        // Add a mood and see if it's update
        solo.clickOnView(fab);
        solo.pressSpinnerItem(0, EmotionalState.HAPPINESS.getStateCode()); //Happy
        solo.pressSpinnerItem(1, SocialSituation.SEVERAL.getSituationCode()); //two to several
        solo.enterText(((TextInputLayout) solo.getView(R.id.text_input_reason)).getEditText(), "I am HAPPY2");
        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForText("Happy", mood_quantity+1, 1000);
        solo.waitForText("HillyBillyBobTesterino", 0, 10000);
        assertEquals(mood_quantity+1, moodArrayAdapter.getCount());

        // Delete that mood
        solo.clickOnMenuItem("Happy");
        solo.clickOnButton("DELETE");
        solo.waitForText("Happy", mood_quantity, 1000);
        solo.waitForText("HillyBillyBobTesterino", 0, 10000);

        assertEquals(mood_quantity, moodArrayAdapter.getCount());

        // Edit another mood
        solo.clickOnMenuItem("Happy");
        solo.clickOnButton("EDIT");
            // Change the mood to sad
        solo.waitForText("Edit Mood", 1, 1000); //make sure DefineMoodDialogFragment opens itself correctly as a "Edit" rather than "Add"

        solo.pressSpinnerItem(0, EmotionalState.SADNESS.getStateCode()); //sad
        solo.pressSpinnerItem(1, SocialSituation.CROWD.getSituationCode()); //crowd

        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForText("Happy", mood_quantity-1, 1000);
        solo.waitForText("HillyBillyBobTesterino", 0, 10000);

        assertEquals(mood_quantity-1, moodArrayAdapter.getCount());

        appPreferences.getRepository().deleteAllMoods(appPreferences.getCurrentUser());

    }
}