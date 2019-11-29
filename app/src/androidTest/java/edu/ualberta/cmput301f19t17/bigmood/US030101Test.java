package edu.ualberta.cmput301f19t17.bigmood;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.GeoPoint;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.Calendar;

import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.activity.SignUpActivity;
import edu.ualberta.cmput301f19t17.bigmood.database.MockRepository;
import edu.ualberta.cmput301f19t17.bigmood.database.User;
import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;
import edu.ualberta.cmput301f19t17.bigmood.model.SocialSituation;

import static org.junit.Assert.assertTrue;

public class US030101Test {
    private Solo solo;
    private static AppPreferences appPreferences;
    private static MockRepository mockRepository;

    private EditText fieldFirstName;
    private EditText fieldLastName;
    private EditText fieldUsername;
    private EditText fieldPassword;
    private EditText fieldConfirmPassword;


    @BeforeClass
    public static void setRepository() {

        // Set app preferences
        US030101Test.appPreferences = AppPreferences.getInstance();

        // Create new in-memory database and set the app preferences to use it
        US030101Test.mockRepository = new MockRepository();
        US030101Test.appPreferences.setRepository(US030101Test.mockRepository);

        // Login as user2 from the database using a specialized method in MockRepository
        US030101Test.appPreferences.login(US030101Test.mockRepository.getUser("user2"));

        //clear all user's mood and add some new ones
        User follow2 = mockRepository.getUser("user2");
        User follow3 = mockRepository.getUser("user3");

        mockRepository.deleteAllUserMoods(follow2);
        mockRepository.deleteAllUserMoods(follow3);

        // create a calendar on which we base the other calendars
        Calendar baseCalendar = Calendar.getInstance();
        baseCalendar.set(2019, 10, 23, 12, 0);

        // add mood to user2
        Calendar calendar1 = (Calendar) baseCalendar.clone();
        calendar1.add(Calendar.MINUTE, 5);
        Mood mood1 = new Mood(EmotionalState.HAPPINESS, calendar1, SocialSituation.ALONE, "I am happy.", new GeoPoint(53.5461, 113.4938), null);
        mockRepository.createMood(follow2, mood1, null, null);

        // add mood to user3
        Calendar calendar2 = (Calendar) baseCalendar.clone();
        Mood mood2 = new Mood(EmotionalState.ANGER, calendar2, SocialSituation.CROWD, "I am mad.", new GeoPoint(53.5461, 113.4938), null);
        mockRepository.createMood(follow3, mood2, null, null);


    }
    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before //runs before every test
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());



    }

    @Test
    public void testMoodInteraction_U2(){

        // Assert current activity
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        //click on user2's most recent mood
        solo.clickOnMenuItem("Happy");

        // Not done. also need to check location and image matches.
        //ensure we have opened the item correctly
        assertTrue(solo.waitForText(EmotionalState.HAPPINESS.toString()));
        assertTrue(solo.searchText("I am happy."));
        assertTrue(solo.waitForText(SocialSituation.ALONE.toString()));
        solo.sleep(1000);

        //close the ViewMoodDialogFragment by simulating the hardware back button
        solo.goBack();

    }

    @Test
    public void testMoodInteraction_U3(){

        // Assert current activity
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        // login as User3
        User requestedUser =  US030101Test.mockRepository.getUser("user3");
        US030101Test.appPreferences.getInstance().login(requestedUser);
        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.title_profile));
        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.title_user_moods));

        //click on user3's most recent mood
        solo.clickOnMenuItem("Angry");

        // Not done. Also need to check location and image matches.
        //ensure we have opened the item correctly
        assertTrue(solo.waitForText(EmotionalState.ANGER.toString()));
        assertTrue(solo.searchText("I am mad."));
        assertTrue(solo.waitForText(SocialSituation.CROWD.toString()));
        solo.sleep(1000);




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
