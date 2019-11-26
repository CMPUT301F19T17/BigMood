package edu.ualberta.cmput301f19t17.bigmood;

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
import static org.junit.Assert.assertTrue;

public class US010301Test {
    private Solo solo;
    private static AppPreferences appPreferences;
    private static MockRepository mockRepository;

    @BeforeClass
    public static void setRepository() {

        // Set app preferences
        US010301Test.appPreferences = AppPreferences.getInstance();

        // Create new in-memory database and set the app preferences to use it
        US010301Test.mockRepository = new MockRepository();
        US010301Test.appPreferences.setRepository(US010301Test.mockRepository);

        // Login with a user from the database using a specialized method in MockRepository
        US010301Test.appPreferences.login(US010301Test.mockRepository.getUser("user1"));

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
    public void testDisplayStateTimeDate() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);


        // DON'T USE clickInList it's very buggy
        // click on the FIRST occurrence of the Happy mood (in this case mood3)
        solo.clickOnMenuItem("Happy");

        Integer drawableID = (Integer) solo.getView(R.id.imageview_placeholder_emote).getTag();
        assertTrue(R.drawable.ic_emoticon_happy == drawableID);
        assertTrue(solo.waitForText("Happy", 1, 2000));
    }

    @Test
    public void  testDisplayStateTimeDateSituation() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        //click on mood4
        solo.clickOnMenuItem("Sad");

        Integer drawableID = (Integer) solo.getView(R.id.imageview_placeholder_emote).getTag();
        assertEquals(R.drawable.ic_emoticon_sad, (int) drawableID);

        assertTrue(solo.waitForText("Sad", 1, 2000));
        assertTrue(solo.waitForText("Crowd", 1, 2000));
    }

    @Test
    public void  testDisplayStateTimeDateSituationReason() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        //click on mood3
        solo.clickOnMenuItem("Happy");

        Integer drawableID = (Integer) solo.getView(R.id.imageview_placeholder_emote).getTag();
        assertTrue(R.drawable.ic_emoticon_happy == drawableID);

        assertTrue(solo.waitForText("Happy", 1, 2000));
        assertTrue(solo.waitForText("Two to several people", 1, 2000));
        assertTrue(solo.waitForText("user1 - mood3", 1, 2000));
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
