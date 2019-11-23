package edu.ualberta.cmput301f19t17.bigmood.database;

import androidx.annotation.VisibleForTesting;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.ListenerRegistration;

import edu.ualberta.cmput301f19t17.bigmood.database.listener.FollowingListener;
import edu.ualberta.cmput301f19t17.bigmood.database.listener.MoodsListener;
import edu.ualberta.cmput301f19t17.bigmood.database.listener.RequestsListener;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;
import edu.ualberta.cmput301f19t17.bigmood.model.Request;

public class MockRepository implements Repository {


    @Override
    public void userExists(String username, OnSuccessListener<Boolean> successListener, OnFailureListener failureListener) {

    }

    @Override
    public void registerUser(String username, String password, String firstName, String lastName, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {

    }

    @Override
    public void validateUser(String username, String password, OnSuccessListener<User> successListener, OnFailureListener failureListener) {

    }

    @Override
    public ListenerRegistration getUserMoods(User user, MoodsListener listener) {
        return null;
    }

    @Override
    public ListenerRegistration getFollowingList(User user, FollowingListener listener) {
        return null;
    }

    @Override
    public ListenerRegistration getFollowingMoods(User user, MoodsListener listener) {
        return null;
    }

    @Override
    public void createMood(User user, Mood mood, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {

    }

    @Override
    public void deleteMood(User user, Mood mood, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {

    }

    @Override
    public void updateMood(User user, Mood mood, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {

    }

    @Override
    public ListenerRegistration getUserRequests(User user, RequestsListener listener) {
        return null;
    }

    @Override
    public void createRequest(Request request, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {

    }

    @Override
    public void acceptRequest(Request request, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {

    }

    @Override
    public void declineRequest(Request request, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {

    }

    /**
     * This method is used to delete all of the moods that belong to a user.
     * It is only used for testing, so as to stop clogging up the user with 50+ moods
     * @param user The user who's moods we want to delete
     * @param successListener A SuccessListener of type <code>Void</code>. This will be called when the task succeeds (can connect to the DB and security rules allow the request)
     * @param failureListener A FailureListener for the Task. This will be called when the task fails (likely when the security rules prevent a certain request).
     */
    @VisibleForTesting
    void deleteAllMoods(User user, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {

    }

}
