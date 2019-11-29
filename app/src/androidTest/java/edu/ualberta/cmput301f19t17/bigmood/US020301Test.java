package edu.ualberta.cmput301f19t17.bigmood;

import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.database.MockRepository;
import edu.ualberta.cmput301f19t17.bigmood.database.MockUser;
import edu.ualberta.cmput301f19t17.bigmood.model.SocialSituation;

import static org.junit.Assert.assertTrue;

public class US020301Test {
    private Solo solo;
    private static AppPreferences appPreferences;
    private static MockRepository mockRepository;

    @BeforeClass
    public static void setRepository() {

        // Set app preferences
        US020301Test.appPreferences = AppPreferences.getInstance();

        // Create new in-memory database and set the app preferences to use it
        US020301Test.mockRepository = new MockRepository();
        US020301Test.appPreferences.setRepository(US020301Test.mockRepository);

        // Login with a user from the database using a specialized method in MockRepository
        US020301Test.appPreferences.login(US020301Test.mockRepository.getUser("user1"));
    }

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before //Clears the mood list before each test
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        // Clear the user's mood list
        US020301Test.mockRepository.deleteAllUserMoods(US020301Test.appPreferences.getCurrentUser());
        solo.clickOnText(solo.getCurrentActivity().getText(R.string.title_user_moods).toString(), 2);
        solo.sleep(1500);
    }

    @Test
    public void checkSocialSituationAlone() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);
        solo.sleep(3000);

        View situationSpinner = solo.getView(R.id.situation_spinner);
        solo.clickOnView(situationSpinner);
        solo.clickOnText(SocialSituation.ALONE.toString());
        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForDialogToClose();
        solo.clickInList(1, 0);
        assertTrue(solo.waitForText("Alone",1,2000));
    }

    @Test
    public void checkSocialSituationOneOther() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);
        solo.sleep(3000);

        View situationSpinner = solo.getView(R.id.situation_spinner);
        solo.clickOnView(situationSpinner);
        solo.clickOnText(SocialSituation.ONE.toString());
        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForDialogToClose();
        solo.clickInList(1, 0);
        assertTrue(solo.waitForText("One person",1,2000));
    }

    @Test
    public void checkSocialSituationSeveral() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);
        solo.sleep(3000);

        View situationSpinner = solo.getView(R.id.situation_spinner);
        solo.clickOnView(situationSpinner);
        solo.clickOnText(SocialSituation.SEVERAL.toString());
        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForDialogToClose();
        solo.clickInList(1, 0);
        assertTrue(solo.waitForText("Two to several people",1,2000));
    }

    @Test
    public void checkSocialSituationCrowd() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        View fab = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(fab);
        solo.sleep(3000);

        View situationSpinner = solo.getView(R.id.situation_spinner);
        solo.clickOnView(situationSpinner);
        solo.clickOnText(SocialSituation.CROWD.toString());
        solo.clickOnView(solo.getView(R.id.action_save));
        solo.waitForDialogToClose();
        solo.clickInList(1, 0);
        assertTrue(solo.waitForText("Crowd",1,2000));
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
