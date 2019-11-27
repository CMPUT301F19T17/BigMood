package edu.ualberta.cmput301f19t17.bigmood;

import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.database.MockRepository;

public class US060201Test {
    private Solo solo;
    private static AppPreferences appPreferences;
    private static MockRepository mockRepository;

    @BeforeClass
    public static void setRepository() {

        // Set app preferences
        US060201Test.appPreferences = AppPreferences.getInstance();

        // Create new in-memory database and set the app preferences to use it
        US060201Test.mockRepository = new MockRepository();
        US060201Test.appPreferences.setRepository(US060201Test.mockRepository);

        // Login with a user from the database using a specialized method in MockRepository
        US060201Test.appPreferences.login(US060201Test.mockRepository.getUser("user1"));
    }

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        // Clear the user's mood list
        US060201Test.mockRepository.deleteAllUserMoods(US060201Test.appPreferences.getCurrentUser());
        solo.clickOnText(solo.getCurrentActivity().getText(R.string.title_user_moods).toString(), 2);
        solo.sleep(1500);
    }

    @Test
    public void testUserMoodMap() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        View userMap = solo.getCurrentActivity().findViewById(R.id.action_maps_user);
        solo.clickOnView(userMap);
        solo.sleep(2000);
        // This XY coordinate is hard-coded, seems like Robotium has no method for click on a drawable
        // This XY should be the top left (X) button of the top toolbar for exiting
        solo.clickOnScreen(75, 113);

    }
}
