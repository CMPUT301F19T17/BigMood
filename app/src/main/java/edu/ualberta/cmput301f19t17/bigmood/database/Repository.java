package edu.ualberta.cmput301f19t17.bigmood.database;

import androidx.annotation.VisibleForTesting;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.ListenerRegistration;

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
    * @return         Returns an asynchronous Task of type Boolean. The Boolean is true if the user exists and false otherwise.
    */
    Task<Boolean> userExists(String username);

    /**
     * This method attempts to register a user using the parameters it was passed.
     * @param username  Username of the user to register. This must be unique or else the query will fail.
     * @param password  Password of the user to register.
     * @param firstName First name of the user to register.
     * @param lastName  Last name of the user to register.
     * @return          Returns A Task of type Void. The task will succeed if the writes were successful and fail otherwise (if the username is not unique for example).
     */
    Task<Void> registerUser(String username, String password, String firstName, String lastName);

    /**
     * This method handles our """authentication""". It basically validates a username and password against the database and if it is successful it returns a constructed User object that is used as the token for """authentication""".
     * @param username Username of the user to validate
     * @param password Password of the use to validate
     * @return         Returns a task of type User. The task will succeed if there is a valid username and password combination in the database. In the OnSuccessListener you can then extract the user object to use and store as an authentication "token".
     */
    Task<User> validateUser(String username, String password);

    /**
     * This method gets as a List the set of all moods belonging to a particular User.
     * @param user The User whose moods we will retrieve.
     * @return     Returns a ListenerRegistration. Upon the first call and any other change to the database the callback method will be invoked.
     */
    ListenerRegistration getUserMoods(User user, MoodsListener listener);

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
     * @return     Returns a Task of type Void. The task will succeed if it was able to write the document to the database.
     */
    Task<Void> createMood(User user, Mood mood);

    /**
     * this method attempts to delete a Mood in the database given the parameters.
     * @param user The User where we would find the given Mood.
     * @param mood The Mood we are trying to delete from the database. The mood passed in should be an OLD mood and HAVE a firestoreId/databaseId.
     * @return     Returns a Task of type Void. The task will succeed if it was able to delete the document from the database.
     */
    Task<Void> deleteMood(User user, Mood mood);

    /**
     * This method is used to delete all of the moods that belong to a user.
     * It is only used for testing, so as to stop clogging up the user with 50+ moods
     * @param user The user who's moods we want to delete
     * @return     Returns a Task of type Void. The task will succeed if it was able to retrieve all the documents from the database, and may not necessarily fail when documents fail to be deleted.
     *
     */
    @VisibleForTesting
    Task<Void> deleteAllMoods(User user);

    /**
     * this method attempts to modify a Mood in the database given the parameters.
     * @param user The User where we would find the given Mood.
     * @param mood The Mood we are trying to delete from the database. The mood passed in should be an OLD mood and HAVE a firestoreId/databaseId.
     * @return     Returns a Task of type Void. The task will succeed if it was able to delete the document from the database.
     */
    Task<Void> updateMood(User user, Mood mood);

    /**
     * This method gets as a List the set of all Requests belonging to a particular User.
     * @param user The User whose Requests we will retrieve.
     * @return     Returns a ListenerRegistration. Upon the first call and any other change to the database the callback method will be invoked.
     */
    ListenerRegistration getUserRequests(User user, RequestsListener listener);

    /**
     * This method attempts to create a Request in the database given the parameters.
     * @param request The Request we are trying to post to the database. The Request passed in should be a NEW request and the destination user exists.
     * @return        Returns a Task of type Void. The task will succeed if it was able to write the document to the database.
     */
    Task<Void> createRequest(Request request);

    /**
     * This method handles the operation of "accepting" a request. This means that the recipient (the "to" field) has accepted the sender's (the "from" field) request to follow them. Therefore we need to add the recipient to the sender's follower list.
     * @param request The Request to accept. The Request passed in should be an OLD Request and MUST have a firestoreId.
     * @return        Returns a Task of type Void. The task will succeed if it was able to complete the transaction of deleting the request and adding to the correct follower list.
     */
    Task<Void> acceptRequest(Request request);

    /**
     * This method handles the operation of "declining" a request. This means that the recipient (the "to" field) has declined the sender's (the "from" field) request to follow them. Therefore we just need to delete the request and not change anything else.
     * @param request The Request to decline. The Request passed in should be an OLD Request and MUST have a firestoreId.
     * @return        Returns a Task of type Void. The task will succeed if it was able to delete the document from the database.
     */
    Task<Void> declineRequest(Request request);

}
