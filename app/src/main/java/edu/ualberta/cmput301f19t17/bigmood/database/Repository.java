package edu.ualberta.cmput301f19t17.bigmood.database;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.ListenerRegistration;

import edu.ualberta.cmput301f19t17.bigmood.database.listener.FollowingListener;
import edu.ualberta.cmput301f19t17.bigmood.database.listener.ImageProgressListener;
import edu.ualberta.cmput301f19t17.bigmood.database.listener.MoodsListener;
import edu.ualberta.cmput301f19t17.bigmood.database.listener.RequestsListener;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;
import edu.ualberta.cmput301f19t17.bigmood.model.Request;

/**
 * This interface provides the method bodies for a generic Repository class (database interaction). The purpose of doing it this way is it allows us to easily create mock Repositories for testing.
 */
public interface Repository {

    /**
    * This method checks if a user exists in the database by a username lookup.
    * @param username Username string of the user to look up
    * @param successListener A SuccessListener of type <code>Boolean</code>. This will be called when the task succeeds (can connect to the DB and security rules allow the request)
    * @param failureListener A FailureListener for the Task. This will be called when the task fails (likely when the security rules prevent a certain request).
    */
    void userExists(String username, OnSuccessListener<Boolean> successListener, OnFailureListener failureListener);

    /**
     * This method attempts to register a user using the parameters it was passed.
     * @param username  Username of the user to register. This must be unique or else the query will fail.
     * @param password  Password of the user to register.
     * @param firstName First name of the user to register.
     * @param lastName  Last name of the user to register.
     * @param successListener A SuccessListener of type <code>Void</code>. This will be called when the task succeeds (can connect to the DB and security rules allow the request)
     * @param failureListener A FailureListener for the Task. This will be called when the task fails (likely when the security rules prevent a certain request).
     */
    void registerUser(String username, String password, String firstName, String lastName, OnSuccessListener<Void> successListener, OnFailureListener failureListener);

    /**
     * This method handles our """authentication""". It basically validates a username and password against the database and if it is successful it returns a constructed User object that is used as the token for """authentication""".
     * @param username Username of the user to validate
     * @param password Password of the use to validate
     * @param successListener A SuccessListener of type <code>User</code>. This will be called when the task succeeds (can connect to the DB and security rules allow the request)
     * @param failureListener A FailureListener for the Task. This will be called when the task fails (likely when the security rules prevent a certain request).
     */
    void validateUser(String username, String password, OnSuccessListener<User> successListener, OnFailureListener failureListener);

    /**
     * This method gets as a List the set of all moods belonging to a particular User.
     * @param user The User whose moods we will retrieve.
     * @return     Returns a ListenerRegistration. Upon the first call and any other change to the database the callback method will be invoked.
     */
    ListenerRegistration getUserMoods(User user, MoodsListener listener);

    /**
     * This method sets up a ListenerRegistration that polls for changes in the following document, which holds the following list for each user.
     * @param user     The User who the following list belongs to
     * @param listener An implemented callback interface that will be called whenever there is an update in the follower list.
     * @return         Returns a ListenerRegistration. Make sure to remove() it when you don't need it anymore.
     */
    ListenerRegistration getFollowingList(User user, final FollowingListener listener);

    /**
     * This method gets the most recent mood (read: limit 1) from ALL the Users the passed in User is following.
     * @param user The current User. We will use their information to get the applicable moods for the users they are following.
     * @return     Returns a ListenerRegistration. Upon the first call and any other change to the database the callback method will be invoked.
     */
    ListenerRegistration getFollowingMoods(User user, MoodsListener listener);

    /**
     * This method attempts to create a Mood in the database given the parameters.
     * @param user The User for which the new Mood should fall under.
     * @param mood The Mood we are trying to post to the database. The mood passed in should be a NEW mood and NOT have a firestoreId/databaseId.
     * @param successListener A SuccessListener of type <code>Void</code>. This will be called when the task succeeds (can connect to the DB and security rules allow the request)
     * @param failureListener A FailureListener for the Task. This will be called when the task fails (likely when the security rules prevent a certain request).
     */
    void createMood(User user, Mood mood, OnSuccessListener<Void> successListener, OnFailureListener failureListener);

    /**
     * this method attempts to delete a Mood in the database given the parameters.
     * @param user The User where we would find the given Mood.
     * @param mood The Mood we are trying to delete from the database. The mood passed in should be an OLD mood and HAVE a firestoreId/databaseId.
     * @param successListener A SuccessListener of type <code>Void</code>. This will be called when the task succeeds (can connect to the DB and security rules allow the request)
     * @param failureListener A FailureListener for the Task. This will be called when the task fails (likely when the security rules prevent a certain request).
     */
    void deleteMood(User user, Mood mood, OnSuccessListener<Void> successListener, OnFailureListener failureListener);

    /**
     * this method attempts to modify a Mood in the database given the parameters.
     * @param user The User where we would find the given Mood.
     * @param mood The Mood we are trying to delete from the database. The mood passed in should be an OLD mood and HAVE a firestoreId/databaseId.
     * @param successListener A SuccessListener of type <code>Void</code>. This will be called when the task succeeds (can connect to the DB and security rules allow the request)
     * @param failureListener A FailureListener for the Task. This will be called when the task fails (likely when the security rules prevent a certain request).
     */
    void updateMood(User user, Mood mood, OnSuccessListener<Void> successListener, OnFailureListener failureListener);

    /**
     * This method attempts to upload an image pointed to the Uri to Firebase storage
     *
     * @param user                  The user to which to save the image under
     * @param imageUri              The URI of the image to save
     * @param fileExtension         The file extension of the image to save.
     * @param successListener       A SuccessListener of type <code>String</code>. This will be called when the task succeeds (can connect to the DB and security rules allow the request). The string is the unique imageId it calculated.
     * @param failureListener       A FailureListener for the Task. This will be called when the task fails (likely when the security rules prevent a certain request).
     * @param imageProgressListener An ImageProgressListener (our own interface callback. This will be called every time there is some sort of progress report. This is meant for controlling some kind of progress bar.
     */
    void uploadNewImage(User user, Uri imageUri, String fileExtension, OnSuccessListener<String> successListener, OnFailureListener failureListener, ImageProgressListener imageProgressListener);

    /**
     * This method attempts to replace an image on the remote server pointed to by the imageId with an image pointed to by the URI.
     *
     * @param imageId               The unique imageId calculated beforehand and stored in the database.
     * @param imageUri              The URI of the image to save
     * @param successListener       A SuccessListener of type <code>Void</code>. This will be called when the task succeeds (can connect to the DB and security rules allow the request).
     * @param failureListener       A FailureListener for the Task. This will be called when the task fails (likely when the security rules prevent a certain request).
     * @param imageProgressListener An ImageProgressListener (our own interface callback. This will be called every time there is some sort of progress report. This is meant for controlling some kind of progress bar.
     */
    void uploadReplaceImage(String imageId, Uri imageUri, OnSuccessListener<Void> successListener, OnFailureListener failureListener, ImageProgressListener imageProgressListener);

    /**
     * This method attempts to download an image from the Firebase storage server given an imageId.
     *
     * @param imageId               The unique imageId calculated beforehand and stored in the database.
     * @param successListener       A SuccessListener of type <code>Bitmap</code>. This will be called when the task succeeds (can connect to the DB and security rules allow the request). The Bitmap is the actual image you can draw.
     * @param failureListener       A FailureListener for the Task. This will be called when the task fails (likely when the security rules prevent a certain request).
     * @param imageProgressListener An ImageProgressListener (our own interface callback. This will be called every time there is some sort of progress report. This is meant for controlling some kind of progress bar.
     */
    void downloadImage(String imageId, OnSuccessListener<Bitmap> successListener, OnFailureListener failureListener, ImageProgressListener imageProgressListener);

    /**
     * This method gets as a List the set of all Requests belonging to a particular User.
     * @param user The User whose Requests we will retrieve.
     * @return     Returns a ListenerRegistration. Upon the first call and any other change to the database the callback method will be invoked.
     */
    ListenerRegistration getUserRequests(User user, RequestsListener listener);

    /**
     * This method attempts to create a Request in the database given the parameters.
     * @param request The Request we are trying to post to the database. The Request passed in should be a NEW request and the destination user exists.
     * @param successListener A SuccessListener of type <code>Void</code>. This will be called when the task succeeds (can connect to the DB and security rules allow the request)
     * @param failureListener A FailureListener for the Task. This will be called when the task fails (likely when the security rules prevent a certain request).
     */
    void createRequest(Request request, OnSuccessListener<Void> successListener, OnFailureListener failureListener);

    /**
     * This method handles the operation of "accepting" a request. This means that the recipient (the "to" field) has accepted the sender's (the "from" field) request to follow them. Therefore we need to add the recipient to the sender's follower list.
     * @param request The Request to accept. The Request passed in should be an OLD Request and MUST have a firestoreId.
     * @param successListener A SuccessListener of type <code>Void</code>. This will be called when the task succeeds (can connect to the DB and security rules allow the request)
     * @param failureListener A FailureListener for the Task. This will be called when the task fails (likely when the security rules prevent a certain request).
     */
    void acceptRequest(Request request, OnSuccessListener<Void> successListener, OnFailureListener failureListener);

    /**
     * This method handles the operation of "declining" a request. This means that the recipient (the "to" field) has declined the sender's (the "from" field) request to follow them. Therefore we just need to delete the request and not change anything else.
     * @param request The Request to decline. The Request passed in should be an OLD Request and MUST have a firestoreId.
     * @param successListener A SuccessListener of type <code>Void</code>. This will be called when the task succeeds (can connect to the DB and security rules allow the request)
     * @param failureListener A FailureListener for the Task. This will be called when the task fails (likely when the security rules prevent a certain request).
     */
    void declineRequest(Request request, OnSuccessListener<Void> successListener, OnFailureListener failureListener);

}
