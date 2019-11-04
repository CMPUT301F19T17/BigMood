package edu.ualberta.cmput301f19t17.bigmood.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.model.Request;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;

/**
 * This class handles all the database requests coming from the application. It implements methods for interacting with the collections and documents that this application should need. All methods return a Task<T>, where T is the applicable return type. Since these are asynchronous you must make sure to add an OnSuccessListener() and an OnFailureListener() to the tasks you get back so that you can give feedback to the user.
 */
public class Repository {

    // Singleton implementation
    private static Repository repository = null;

    // Firestore database object
    private FirebaseFirestore db;

    /**
     * This method gets the single instance of the Repository class. If it does not exist it creates one. No public constructor is available.
     * @return Returns the instance of the repository class
     */
    public static Repository getInstance() {

        if (Repository.repository == null)
            Repository.repository = new Repository();

        return Repository.repository;

    }

    /**
     * This constructor handles the responsibility of instantiating the only instance of Repository (singleton class). It is private because we want only one instance to exist at once.
     */
    private Repository() {
        this.db = FirebaseFirestore.getInstance();
    }

    // USER RELATED METHODS //

    /**
     * This method checks if a user exists in the database by a username lookup.
     * @param username Username string of the user to look up
     * @return         Returns an asynchronous Task of type Boolean. The Boolean is true if the user exists and false otherwise.
     */
    public Task<Boolean> userExists(final String username) {

        return db
                .collection(FirestoreMapping.COLLECTION_USERS)
                .document(username)
                .get()
                .continueWith(new Continuation<DocumentSnapshot, Boolean>() {
                    @Override
                    public Boolean then(@NonNull Task<DocumentSnapshot> task) throws Exception {

                        // Will propagate an exception if .getResult() produces one. If not, then the Document is assigned to doc.
                        DocumentSnapshot doc = task.getResult();

                        // We have a potential document, we just have to check if that particular entry exists or not.
                        return doc.exists();

                    }


                });

    }

    /**
     * This method attempts to register a user using the parameters it was passed.
     * @param username  Username of the user to register. This must be unique or else the query will fail.
     * @param password  Password of the user to register.
     * @param firstName First name iof the user to register.
     * @param lastName  Last name iof the user to register.
     * @return          Returns A Task of type Void. The task will succeed if the writes were successful and fail otherwise (if the username is not unique for example).
     */
    public Task<Void> registerUser(String username, String password, String firstName, String lastName) {

        // Check if the username, password, firstName, and lastName are at least one character.
        if (username.length() <= 0 || password.length() <= 0 || firstName.length() <= 0 || lastName.length() <= 0)
            throw new IllegalArgumentException("Any of username, password, firstName, and lastName have to be at least one character.");

        // Create batch object
        WriteBatch batch = db.batch();


        // Prepare user data
        User newUser = new User(username, firstName, lastName);

        // Prepare password data
        Map<String, Object> passwordData = new HashMap<>();
        passwordData.put(FirestoreMapping.FIELD_CREDENTIAL_PASSWORD, password);

        // Prepare follower_list
        Map<String, Object> followerData = new HashMap<>();
        followerData.put(FirestoreMapping.FIELD_FOLLOWER_FOLLOWERLIST, FieldValue.arrayUnion());


        // Create user document
        DocumentReference userDocument = db
                .collection(FirestoreMapping.COLLECTION_USERS)
                .document(username);

        // Create password document
        DocumentReference passwordDocument = db
                .collection(FirestoreMapping.COLLECTION_USERS)
                .document(newUser.getUsername())
                .collection(FirestoreMapping.COLLECTION_PRIVATE)
                .document(FirestoreMapping.DOCUMENT_CREDENTIAL);

        // Create follower document
        DocumentReference followerDocument = db
                .collection(FirestoreMapping.COLLECTION_USERS)
                .document(newUser.getUsername())
                .collection(FirestoreMapping.COLLECTION_PRIVATE)
                .document(FirestoreMapping.DOCUMENT_FOLLOWER);


        // ALL three of these transactions have to be in a batch write or else the write will fail. EVERY user must have these three documents to be valid. The application order does not matter.
        batch.set(userDocument, FirestoreConversion.UserToFirestore(newUser));
        batch.set(passwordDocument, passwordData);
        batch.set(followerDocument, followerData);

        // Since there is no object or document to return we are returning a task of type void
        return batch.commit();

    }

    /**
     * This method handles our """authentication""". It basically validates a username and password against the database and if it is successful it returns a constructed User object that is used as the token for """authentication""".
     * @param username Username of the user to validate
     * @param password Password of the use to validate
     * @return         Returns a task of type User. The task will succeed if there is a valid username and password combination in the database. In the OnSuccessListener you can then extract the user object to use and store as an authentication "token".
     */
    public Task<User> validateUser(final String username, final String password) {

        // Queue up a task to get the user document
        Task<DocumentSnapshot> userTask = db
                .collection(FirestoreMapping.COLLECTION_USERS)
                .document(username)
                .get();

        // Queue up a task to get the credential document of the user.
        Task<DocumentSnapshot> credentialTask = db
                .collection(FirestoreMapping.COLLECTION_USERS)
                .document(username)
                .collection(FirestoreMapping.COLLECTION_PRIVATE)
                .document(FirestoreMapping.DOCUMENT_CREDENTIAL)
                .get();

        // Define a task list containing
        Task<List<DocumentSnapshot>> allTasks = Tasks.whenAllSuccess(userTask, credentialTask);

        // Define a continuation to actually verify the data
        return allTasks.continueWith(new Continuation<List<DocumentSnapshot>, User>() {
            @Override
            public User then(@NonNull Task<List<DocumentSnapshot>> task) throws Exception {

                // If any of the two tasks failed, .getResult() will propagate an error.
                List<DocumentSnapshot> taskList = task.getResult();

                // Check all the documents in the list exist. I am not sure if this is entirely necessary but just to be sure I included it.
                for (DocumentSnapshot doc : taskList)
                    if (doc == null)
                        throw new IllegalArgumentException("One or more documents are null in the task list. This should not happen.");

                // Since the task list is evaluated in the order we listed them in the first element is the user document. If the user document does not exist then the username/password combo does not match anything. In that case we return null.
                DocumentSnapshot userDoc = taskList.get(0);
                if (! userDoc.exists())
                    return null;

                // Just to be safe (and to avoid an error), if the password document does not exist we have to throw an error. We assume that the username exists but since they have no password this user is in an illegal state and should not exist in the DB.
                DocumentSnapshot passwordDoc = taskList.get(1);
                if (! passwordDoc.exists())
                    throw new IllegalStateException(String.format("User '%s' does not have a password document in the database. This user is in an invalid state and must be recreated. Please delete this user in the Firebase console.", taskList.get(0).getId()));

                // Get the password stored in the database. Don't freak out. It's fine.
                String dbPassword = passwordDoc.getString(FirestoreMapping.FIELD_CREDENTIAL_PASSWORD);

                // We validate the user's password here, and if it succeeds we return a user object.
                // Okay, stop yelling at me. I know this is an eternal sin to do this but here me out. Firstly, we have no custom authentication server to dish out JWTs, and secondly, for the purposes of our app (basically school demonstration) using Firebase's Authentication would be way too overkill and cumbersome to demo. This app is not supposed to be public, which is why I am committing this unpardonable sin. I'm so sorry. It hurts me as well.
                if (dbPassword.equals(password))
                    return new User(
                            username,
                            userDoc.getString(FirestoreMapping.FIELD_USER_FIRSTNAME),
                            userDoc.getString(FirestoreMapping.FIELD_USER_LASTNAME)
                    );
                else
                    return null;

            }
        });

    }

    // MOOD RELATED METHODS //

    /**
     * This method gets as a List the set of all moods belonging to a particular User.
     * @param user The User whose moods we will retrieve.
     * @return     Returns a Task of type List<Mood>. If the task succeeds you can iterate over the List in the OnSuccessListener.
     */
    public Task<List<Mood>> getUserMoods(User user) {

        return db
                .collection(FirestoreMapping.COLLECTION_USERS)
                .document(user.getUsername())
                .collection(FirestoreMapping.COLLECTION_MOODS)
                .get()
                .continueWith(new Continuation<QuerySnapshot, List<Mood>>() {
                    @Override
                    public List<Mood> then(@NonNull Task<QuerySnapshot> task) throws Exception {

                        // Will propagate error
                        QuerySnapshot snapshot = task.getResult();

                        if (snapshot == null)
                            return null;

                        List<Mood> moodList = new ArrayList<>();

                        for (QueryDocumentSnapshot doc : snapshot)
                            moodList.add( FirestoreConversion.MoodFromFirestore(doc) );

                        return  moodList;
                    }
                });

    }

    // TODO: 2019-11-01 Nectarios: IMPLEMENT QUERY
    /**
     * This method gets the most recent mood (read: limit 1) from ALL the Users the passed in User is following.
     * @param user The current User. We will use their information to get the applicable moods for the users they are following.
     * @return     Returns a Task of type List<Mood>. If the task succeeds you can iterate over the List in the OnSuccessListener.
     */
    public Task<List<Mood>> getFollowerMoods(User user) {

        throw new IllegalArgumentException("This method is not implemented yet");

    }

    /**
     * This method attempts to create a Mood in the database given the parameters.
     * @param user The User for which the new Mood should fall under.
     * @param mood The Mood we are trying to post to the database. The mood passed in should be a NEW mood and NOT have a firestoreId.
     * @return     Returns a Task of type Void. The task will succeed if it was able to write the document to the database.
     */
    public Task<Void> createMood(User user, Mood mood) {

        // Since the Firestore ID is final in the class, we can assume that if it has one, it came from the database. We don't want to add another duplicate mood to the database if it already exists. In other words we play it safe
        if (mood.getFirestoreId() != null)
            throw new IllegalArgumentException("This mood cannot be from the database -- it must be created as new.");

        return db
                .collection(FirestoreMapping.COLLECTION_USERS)
                .document(user.getUsername())
                .collection(FirestoreMapping.COLLECTION_MOODS)
                .add(FirestoreConversion.MoodToFirestore(mood))
                .continueWith(new Continuation<DocumentReference, Void>() {
                    @Override
                    public Void then(@NonNull Task<DocumentReference> task) throws Exception {

                        // Propagate error and return null.
                        task.getResult();

                        return null;
                    }
                });

    }

    /**
     * this method attempts to delete a Mood in the database given the parameters.
     * @param user The User where we would find the given Mood.
     * @param mood The Mood we are trying to delete from the database. The mood passed in should be an OLD mood and HAVE a firestoreId.
     * @return     Returns a Task of type Void. The task will succeed if it was able to delete the document from the database.
     */
    public Task<Void> deleteMood(User user, Mood mood) {

        // Any mood that is passed into this task has to have a FirestoreId. If it doesn't, then this mood was not created as valid.
        if (mood.getFirestoreId() == null)
            throw new IllegalArgumentException("This mood cannot be new -- it must be created from the database");

        return db
                .collection(FirestoreMapping.COLLECTION_USERS)
                .document(user.getUsername())
                .collection(FirestoreMapping.COLLECTION_MOODS)
                .document(mood.getFirestoreId())
                .delete();

    }

    /**
     * this method attempts to modify a Mood in the database given the parameters.
     * @param user The User where we would find the given Mood.
     * @param mood The Mood we are trying to delete from the database. The mood passed in should be an OLD mood and HAVE a firestoreId.
     * @return     Returns a Task of type Void. The task will succeed if it was able to delete the document from the database.
     */
    public Task<Void> updateMood(User user, Mood mood) {

        // Any mood that is passed into this task has to have a FirestoreId. If it doesn't, then this mood was not created as valid.
        if (mood.getFirestoreId() == null)
            throw new IllegalArgumentException("This mood cannot be new -- it must be created from the database");

        return db
                .collection(FirestoreMapping.COLLECTION_USERS)
                .document(user.getUsername())
                .collection(FirestoreMapping.COLLECTION_MOODS)
                .document(mood.getFirestoreId())
                .update(FirestoreConversion.MoodToFirestore(mood));

    }

    // REQUEST RELATED METHODS //

    /**
     * This method gets as a List the set of all Requests belonging to a particular User.
     * @param user The User whose Requests we will retrieve.
     * @return     Returns a Task of type List<Request>. If the task succeeds you can iterate over the List in the OnSuccessListener.
     */
    public Task<List<Request>> getUserRequests(User user) {

        return db
                .collection(FirestoreMapping.COLLECTION_REQUESTS)
                .whereEqualTo(FirestoreMapping.FIELD_REQUEST_TO, user.getUsername())
                .get()
                .continueWith(new Continuation<QuerySnapshot, List<Request>>() {
                    @Override
                    public List<Request> then(@NonNull Task<QuerySnapshot> task) throws Exception {

                        // Create new List object
                        List<Request> requestList = new ArrayList<>();

                        // Will propagate error
                        QuerySnapshot query = task.getResult();

                        for (QueryDocumentSnapshot doc : query)
                            requestList.add(FirestoreConversion.RequestFromFirestore(doc));

                        return requestList;

                    }
                });

    }

    /**
     * This method attempts to create a Request in the database given the parameters.
     * @param request The Request we are trying to post to the database. The Request passed in should be a NEW request and the destination user exists.
     * @return        Returns a Task of type Void. The task will succeed if it was able to write the document to the database.
     */
    public Task<Void> createRequest(Request request) {

        if (request.getFirestoreId() != null)
            throw new IllegalArgumentException("This request cannot be from the database -- it must be created as new.");

        // TODO: 2019-10-31 https://github.com/CMPUT301F19T17/BigMood/issues/4
        return db
                .collection(FirestoreMapping.COLLECTION_REQUESTS)
                .add(FirestoreConversion.RequestToFirestore(request))
                .continueWith(new Continuation<DocumentReference, Void>() {
                    @Override
                    public Void then(@NonNull Task<DocumentReference> task) throws Exception {

                        // This will propagate an error if there was an issue with the task.
                        task.getResult();

                        return null;

                    }
                });

    }

    /**
     * This method handles the operation of "accepting" a request. This means that the recipient (the "to" field) has accepted the sender's (the "from" field) request to follow them. Therefore we need to add the recipient to the sender's follower list.
     * @param request The Request to accept. The Request passed in should be an OLD Request and MUST have a firestoreId.
     * @return        Returns a Task of type Void. The task will succeed if it was able to complete the transaction of deleting the request and adding to the correct follower list.
     */
    public Task<Void> acceptRequest(Request request) {

        // If this document comes from the database (and we will know that for sure because we reference it by ID) then we know that both the "from" and "to" user exists. Therefore we don't have to do any user checking on the client side as we can be sure that both users exists to be able to complete the transaction.
        if (request.getFirestoreId() == null)
            throw new IllegalArgumentException("This request cannot be new -- it must be created from the database");

        // Create new batch object
        WriteBatch batch = db.batch();

        // Reference the document with the follower list in it
        DocumentReference followerDocument = db
                .collection(FirestoreMapping.COLLECTION_USERS)
                .document(request.getFrom())
                .collection(FirestoreMapping.COLLECTION_PRIVATE)
                .document(FirestoreMapping.DOCUMENT_FOLLOWER);

        // Reference the document with the particular request associated with this method calll
        DocumentReference requestDocument = db
                .collection(FirestoreMapping.COLLECTION_REQUESTS)
                .document(request.getFirestoreId());

        // Create new Map and put the arrayUnion request in it
        Map<String, Object> arrayUpdateData = new HashMap<>();
        arrayUpdateData.put(FirestoreMapping.FIELD_FOLLOWER_FOLLOWERLIST, FieldValue.arrayUnion(request.getTo()));

        // Apply both changes to the batch request
        batch.update(followerDocument, arrayUpdateData);
        batch.delete(requestDocument);

        // Attempt to commit the changes to the database
        return batch.commit();

    }

    /**
     * This method handles the operation of "accepting" a request. This means that the recipient (the "to" field) has declined the sender's (the "from" field) request to follow them. Therefore we just need to delete the request and not change anything else.
     * @param request The Request to decline. The Request passed in should be an OLD Request and MUST have a firestoreId.
     * @return        Returns a Task of type Void. The task will succeed if it was able to delete the document from the database.
     */
    public Task<Void> declineRequest(Request request) {

        // If this document comes from the database (and we will know that for sure because we reference it by ID) then we know that both the "from" and "to" user exists. Therefore we don't have to do any user checking on the client side as we can be sure that both users exists to be able to complete the transaction.
        if (request.getFirestoreId() == null)
            throw new IllegalArgumentException("This request cannot be new -- it must be created from the database");

        return db
                .collection(FirestoreMapping.COLLECTION_REQUESTS)
                .document(request.getFirestoreId())
                .delete();

    }

}
