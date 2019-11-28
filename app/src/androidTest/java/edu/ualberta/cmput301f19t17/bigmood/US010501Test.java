package edu.ualberta.cmput301f19t17.bigmood;

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

import static org.junit.Assert.assertEquals;

public class US010501Test {
    private Solo solo;
    private static AppPreferences appPreferences;

    private static MockRepository mockRepository;

    @BeforeClass
    public static void setRepository() {

        // Set app preferences
        US010501Test.appPreferences = AppPreferences.getInstance();

        // Create new in-memory database and set the app preferences to use it
        US010501Test.mockRepository = new MockRepository();
        US010501Test.appPreferences.setRepository(US010501Test.mockRepository);

        // Login with a user from the database using a specialized method in MockRepository
        US010501Test.appPreferences.login(US010501Test.mockRepository.getUser("user1"));

    }
    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before //Clears the mood list before each test
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        appPreferences = AppPreferences.getInstance();
        solo.sleep(1000);
    }

    @Test
    public void checkDeleteMood() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        solo.sleep(1000);

        ListView moodList = (ListView) solo.getView(R.id.mood_list);
        ListAdapter moodArrayAdapter = moodList.getAdapter();

        int originalNumListItems = moodArrayAdapter.getCount();

        //select mood3
        solo.clickOnMenuItem("Happy");
        solo.sleep(2000); // Wait for the dialog fragment to load up
        solo.clickOnButton(solo.getCurrentActivity().getResources().getString(R.string.menu_option_delete));

        //wait for 1 seconds
        solo.sleep(5000);

        //make sure there are no new elements in the list (ie, after we added the mood, it was deleted)
        assertEquals(originalNumListItems-1, moodArrayAdapter.getCount());
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

