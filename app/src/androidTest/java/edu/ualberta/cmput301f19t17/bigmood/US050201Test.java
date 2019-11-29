package edu.ualberta.cmput301f19t17.bigmood;

import android.widget.ListAdapter;
import android.widget.ListView;

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
import edu.ualberta.cmput301f19t17.bigmood.database.MockRepository;
import edu.ualberta.cmput301f19t17.bigmood.database.User;
import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;
import edu.ualberta.cmput301f19t17.bigmood.model.SocialSituation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class US050201Test {

    private Solo solo;
    private static AppPreferences appPreferences;
    private static MockRepository mockRepository;

    @BeforeClass
    public static void setRepository() {

        // Set app preferences
        US050201Test.appPreferences = AppPreferences.getInstance();

        // Create new in-memory database and set the app preferences to use it
        US050201Test.mockRepository = new MockRepository();
        US050201Test.appPreferences.setRepository(US050201Test.mockRepository);

        // Login as user2 from the database using a specialized method in MockRepository
        US050201Test.appPreferences.login(US050201Test.mockRepository.getUser("user2"));

        //clear all user's mood and add some new ones
        User follow1 = mockRepository.getUser("user1");
        User follow2 = mockRepository.getUser("user2");
        User follow3 = mockRepository.getUser("user3");

        mockRepository.deleteAllUserMoods(follow1);
        mockRepository.deleteAllUserMoods(follow2);
        mockRepository.deleteAllUserMoods(follow3);

        // create a calendar on which we base the other calendars
        Calendar baseCalendar = Calendar.getInstance();
        baseCalendar.set(2019, 10, 23, 12, 0);

        // add mood to user2
        Calendar calendar1 = (Calendar) baseCalendar.clone();
        calendar1.add(Calendar.MINUTE, 5);
        Mood mood1 = new Mood(null, EmotionalState.HAPPINESS, calendar1, SocialSituation.ALONE, "I am happy.", new GeoPoint(53.5461, 113.4938));
        mockRepository.createMood(follow2, mood1, null, null);

        // add mood to user3
        Calendar calendar2 = (Calendar) baseCalendar.clone();
        Mood mood2 = new Mood(null, EmotionalState.ANGER, calendar2, SocialSituation.CROWD, "I am mad.", new GeoPoint(53.5461, 113.4938));
        mockRepository.createMood(follow3, mood2, null, null);


    }

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before //runs before every test
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }



    @Test
    public void acceptRequestTest(){

        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.title_profile));

        // user2 send a new request to user3
        User requestedUser =  US050201Test.mockRepository.getUser("user3");
        solo.typeText(((TextInputLayout) solo.getView(R.id.text_input_username)).getEditText(), requestedUser.getUsername());
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.button_request));
        assertTrue(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.toast_success_request_sent), 1, 2000));

        // login as user3
        US050201Test.appPreferences.getInstance().login(requestedUser);
        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.title_profile));

        // Go to Requests and accept the request
        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.title_requests));
        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.label_request_accept));
        assertTrue(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.toast_success_request_accept), 1, 2000));

        // login as user2, go to Following and check that user3's recent mood is there
        User followedUser =  US050201Test.mockRepository.getUser("user2");
        US050201Test.appPreferences.getInstance().login(followedUser);
        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.title_profile));
        solo.sleep(1000);

        //switch to following tab
        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.title_following));
        solo.sleep(1000);

        //make sure the second item is the previously added item
        solo.clickOnMenuItem("Angry");

        //ensure we have opened the item correctly
        assertTrue(solo.waitForText(SocialSituation.CROWD.toString()));
        solo.sleep(5000);

    }

    @Test
    public void rejectRequestTest(){

        // Logged in as user2 who already has a pending request from user3
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        // Go to Requests and reject the request from user3
        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.title_requests));
        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.label_request_reject));
        assertTrue(solo.waitForText(solo.getCurrentActivity().getResources().getString(R.string.toast_success_request_reject), 1, 2000));

        // we clear all the moods, so the following list should be empty
        solo.clickOnText(solo.getCurrentActivity().getResources().getString(R.string.title_following));

        ListView moodList = (ListView) solo.getView(R.id.following_mood_list);
        ListAdapter moodArrayAdapter = moodList.getAdapter();
        assertEquals(0, moodArrayAdapter.getCount());

        solo.sleep(5000);

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

