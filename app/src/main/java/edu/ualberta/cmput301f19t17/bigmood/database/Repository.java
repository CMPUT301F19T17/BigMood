package edu.ualberta.cmput301f19t17.bigmood.database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;

import edu.ualberta.cmput301f19t17.bigmood.database.listener.MoodsListener;
import edu.ualberta.cmput301f19t17.bigmood.database.listener.RequestsListener;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;
import edu.ualberta.cmput301f19t17.bigmood.model.Request;

/**
 * This interface provides the method bodies for a generic Repository class (database interaction). The purpose of doing it this way is it allows us to easily create mock Repositories for testing.
 */
public interface Repository {

    Task<Boolean> userExists(String username);

    Task<Void> registerUser(String username, String password, String firstName, String lastName);

    Task<User> validateUser(String username, String password);

    ListenerRegistration getUserMoods(User user, MoodsListener listener);

    ListenerRegistration getFollowingMoods(User user, MoodsListener listener);

    Task<Void> createMood(User user, Mood mood);

    Task<Void> deleteMood(User user, Mood mood);

    Task<Void> deleteAllMoods(User user);

    Task<Void> updateMood(User user, Mood mood);

    ListenerRegistration getUserRequests(User user, RequestsListener listener);

    Task<Void> createRequest(Request request);

    Task<Void> acceptRequest(Request request);

    Task<Void> declineRequest(Request request);

}
