package edu.ualberta.cmput301f19t17.bigmood;

import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.database.MockRepository;
import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;

import static org.junit.Assert.assertEquals;

public class US040201Test {
    private Solo solo;
    private static AppPreferences appPreferences;
    private static MockRepository mockRepository;

    @BeforeClass
    public static void setRepository() {

        // Set app preferences
        US040201Test.appPreferences = AppPreferences.getInstance();

        // Create new in-memory database and set the app preferences to use it
        US040201Test.mockRepository = new MockRepository();
        US040201Test.appPreferences.setRepository(US040201Test.mockRepository);

        // Login with a user from the database using a specialized method in MockRepository
        US040201Test.appPreferences.login(US040201Test.mockRepository.getUser("user1"));
    }
    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before //Clears the mood list before each test
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        // Clear the user's mood list
        US040201Test.mockRepository.deleteAllUserMoods(US040201Test.appPreferences.getCurrentUser());
        solo.clickOnText(solo.getCurrentActivity().getText(R.string.title_user_moods).toString(), 2);
        solo.sleep(1500);
    }


    @Test
    public void checkFilterMood() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        solo.sleep(1000);


        ListView moodList = (ListView) solo.getView(R.id.mood_list);
        ListAdapter moodArrayAdapter = moodList.getAdapter();

        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);

        // Create 2 Mood for each state

        int mood_quantity = 2;
        int state_quantity = EmotionalState.values().length;

        for (EmotionalState state : EmotionalState.values()) {
            for (int i = 0; i < mood_quantity; i++) {
                solo.clickOnView(fab);
                solo.sleep(2000);
                solo.pressSpinnerItem(0, state.getStateCode());
                solo.clickOnView(solo.getView(R.id.action_save));
            }
        }
        // This assert also guarantees the filter at startup stay at None
        // Call sleep for 3 second every time we count the number of Mood on the screen
        // The time of sleep may vary between each system's processor power

        solo.sleep(3000);
        assertEquals(mood_quantity*state_quantity, moodArrayAdapter.getCount());

        View filter = solo.getCurrentActivity().findViewById(R.id.action_filter);
        solo.clickOnView(filter);
        for (EmotionalState state : EmotionalState.values()) {
            // select a mood and re-click the filter to make it disappear
            solo.clickOnMenuItem(state.toString());
            solo.clickOnView(filter);
            // the number of mood show should be equal to the number of mood being filtered
            assertEquals(mood_quantity, moodArrayAdapter.getCount());
        }

        // Go back to None filter, it should show full moods

        solo.clickOnMenuItem("None");
        solo.clickOnView(filter);
        solo.sleep(1000);
        assertEquals(mood_quantity*state_quantity, moodArrayAdapter.getCount());


        // We select Happy filter and then try to Edit/Delete
        solo.clickOnMenuItem("Happy");

        // 1) Try to add a Mood
        solo.clickOnView(fab);
        solo.sleep(2000);
        solo.pressSpinnerItem(0, EmotionalState.HAPPINESS.getStateCode());
        solo.clickOnView(solo.getView(R.id.action_save));
        solo.sleep(3000);
        assertEquals(mood_quantity+1, moodArrayAdapter.getCount());

        // 2) Edit a mood
        solo.clickOnMenuItem("Happy");
        solo.clickOnButton("EDIT");
        solo.pressSpinnerItem(0, EmotionalState.SADNESS.getStateCode());
        solo.clickOnView(solo.getView(R.id.action_save));
        solo.sleep(3000);
        // The number of Happy Mood should be decrease by 1
        assertEquals(mood_quantity, moodArrayAdapter.getCount());
        // Check if our Happy turn into Sad
        solo.clickOnView(filter);
        solo.clickOnMenuItem("Sad");
        solo.sleep(3000);
        // The number of Sad Mood should increase by 1
        assertEquals(mood_quantity+1, moodArrayAdapter.getCount());
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