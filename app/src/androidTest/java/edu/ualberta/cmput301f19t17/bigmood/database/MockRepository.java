package edu.ualberta.cmput301f19t17.bigmood.database;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ualberta.cmput301f19t17.bigmood.database.listener.FollowingListener;
import edu.ualberta.cmput301f19t17.bigmood.database.listener.MockFollowingListenerRegistration;
import edu.ualberta.cmput301f19t17.bigmood.database.listener.MockFollowingMoodsListenerRegistration;
import edu.ualberta.cmput301f19t17.bigmood.database.listener.MockMoodsListenerRegistration;
import edu.ualberta.cmput301f19t17.bigmood.database.listener.MockRequestsListenerRegistration;
import edu.ualberta.cmput301f19t17.bigmood.database.listener.MoodsListener;
import edu.ualberta.cmput301f19t17.bigmood.database.listener.RequestsListener;
import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;
import edu.ualberta.cmput301f19t17.bigmood.model.Request;
import edu.ualberta.cmput301f19t17.bigmood.model.SocialSituation;

public class MockRepository implements Repository {

    private Map<String, User> userList;
    private Map<String, String> passwordList;
    private Map<String, List<String>> followerList;
    private Map<String, List<Mood>> moodList;
    private List<Request> requestList;

    private Integer globalAutoIndex = 13;

    private List<MoodsListener> userMoodsListeners = new ArrayList<>();
    private List<MoodsListener> followingMoodsListeners = new ArrayList<>();
    private List<FollowingListener> followingListeners = new ArrayList<>();
    private List<RequestsListener> requestsListeners = new ArrayList<>();

    /**
     * Helper class used for sorting a <code>List</code> of type <code>Mood</code>
     */
    private class MoodComparator implements Comparator<Mood> {

        @Override
        public int compare(Mood o1, Mood o2) {
            return o2.getDatetime().compareTo(o1.getDatetime());
        }

    }

    /**
     * This is a helper function that takes as input a calendar and an incrementer. It adds the specified minute to the Calendar and returns a cloned Calendar. This is used to simplify some of the sytnax when setting up the state of the mock DB.
     * @param calendar Calendar to cline
     * @param amount   Number of minutes to increment by
     * @return         A cloned calendar fast forwarded <code>amount</code> number of minutes.
     */
    private static Calendar incrementCalendar(Calendar calendar, int amount) {

        // Clone the calendar
        calendar = (Calendar) calendar.clone();

        // Add 1 min to the cloned calendar
        calendar.add(Calendar.MINUTE, amount);

        // Return the cloned calendar
        return calendar;

    }

    /**
     * This constructor creates a new MockRepository with the state defined within this function. Please don't modify this constructor.
     */
    public MockRepository() {

        this.requestList = new ArrayList<>();

        this.userList = new HashMap<>();

        this.passwordList = new HashMap<>();
        this.followerList = new HashMap<>();
        this.moodList = new HashMap<>();

        // Three users, with passwords below
        userList.put("user1", new User("user1", "User", "1"));
        userList.put("user2", new User("user2", "User", "2"));
        userList.put("user3", new User("user3", "User", "3"));

        // Passwords for the three users above
        passwordList.put("user1", "p1");
        passwordList.put("user2", "p2");
        passwordList.put("user3", "p3");

        // `user1` follows `user2` and `user3`
        followerList.put("user1", new ArrayList<>(Arrays.asList(
                "user2",
                "user3"
        )));

        // `user2` follows `user1`
        followerList.put("user2", new ArrayList<>(Arrays.asList(
                "user1"
        )));

        // `user3` follows no one
        followerList.put("user3", new ArrayList<String>());

        // Get calendar with specific time
        Calendar baseCalendar = Calendar.getInstance();
        baseCalendar.set(2019, 10, 23, 12, 0);

        // Four moods for user1, all labeled
        moodList.put(
                "user1",
                new ArrayList<>(Arrays.asList(

                        new Mood("1", EmotionalState.HAPPINESS, MockRepository.incrementCalendar(baseCalendar, 1), SocialSituation.ALONE,   "user1 - mood1", new GeoPoint(1, 1), null),
                        new Mood("2", EmotionalState.SADNESS,   MockRepository.incrementCalendar(baseCalendar, 2), SocialSituation.ONE,     "user1 - mood2", new GeoPoint(2, 2), null),
                        new Mood("3", EmotionalState.HAPPINESS, MockRepository.incrementCalendar(baseCalendar, 3), SocialSituation.SEVERAL, "user1 - mood3", new GeoPoint(3, 3), null),
                        new Mood("4", EmotionalState.SADNESS,   MockRepository.incrementCalendar(baseCalendar, 4), SocialSituation.CROWD,   "user1 - mood2", new GeoPoint(4, 4), null)

                ))
        );

        // Four moods for user2, all labeled
        moodList.put(
                "user2",
                new ArrayList<>(Arrays.asList(

                        new Mood("5", EmotionalState.FEAR,    MockRepository.incrementCalendar(baseCalendar, 5), SocialSituation.ALONE,   "user2 - mood5", new GeoPoint(5, 5), null),
                        new Mood("6", EmotionalState.DISGUST, MockRepository.incrementCalendar(baseCalendar, 6), SocialSituation.ONE,     "user2 - mood6", new GeoPoint(6, 6), null),
                        new Mood("7", EmotionalState.FEAR,    MockRepository.incrementCalendar(baseCalendar, 7), SocialSituation.SEVERAL, "user2 - mood7", new GeoPoint(7, 7), null),
                        new Mood("8", EmotionalState.DISGUST, MockRepository.incrementCalendar(baseCalendar, 8), SocialSituation.CROWD,   "user2 - mood8", new GeoPoint(8, 8), null)

                ))
        );

        // Four moods for user3, all labeled
        moodList.put(
                "user3",
                new ArrayList<>(Arrays.asList(

                        new Mood("9",  EmotionalState.ANGER,    MockRepository.incrementCalendar(baseCalendar, 9),  SocialSituation.ALONE,   "user3 - mood9",  new GeoPoint(9,   9), null),
                        new Mood("10", EmotionalState.SURPRISE, MockRepository.incrementCalendar(baseCalendar, 10), SocialSituation.ONE,     "user3 - mood10", new GeoPoint(10, 10), null),
                        new Mood("11", EmotionalState.ANGER,    MockRepository.incrementCalendar(baseCalendar, 11), SocialSituation.SEVERAL, "user3 - mood11", new GeoPoint(11, 11), null),
                        new Mood("12", EmotionalState.SURPRISE, MockRepository.incrementCalendar(baseCalendar, 12), SocialSituation.CROWD,   "user3 - mood12", new GeoPoint(12, 12), null)

                ))
        );

        // There is one pending request from `user3` to `user2`
        requestList.addAll(new ArrayList<>(Collections.singletonList(
                new Request("user3user2", "user3", "user2")
        )));

    }


    // Utility commands //


    public User getUser(String username) {

        User user = this.userList.get(username);

        if (user == null)
            return null;

        return new User(user.getUsername(), user.getFirstName(), user.getLastName());

    }


    // Destruction Commands //


    /**
     * Completely clears out the in-memory database. Make sure to use this when you're not yet logged in, like in a method annotated with @BeforeClass. Or else the app can crash.
     */
    public void resetDatabase() {

        this.requestList = new ArrayList<>();

        this.userList = new HashMap<>();

        this.passwordList = new HashMap<>();
        this.followerList = new HashMap<>();
        this.moodList = new HashMap<>();

    }

    /**
     * This method is used to delete all of the moods that belong to a user.
     * It is only used for testing, so as to stop clogging up the user with 50+ moods
     * @param user The user whose moods we want to delete
     */
    void deleteAllUserMoods(User user) {

        User dbUser = this.userList.get(user.getUsername());
        List<Mood> dbMoodList = this.moodList.get(user.getUsername());

        if (dbUser == null || dbMoodList == null) {

            return;

        }

        dbMoodList.clear();

        this.updateUserMoodsListeners(user);
        this.updateFollowingMoodsListeners(user);

    }


    // Listener Destruction Commands //


    public void removeUserMoodsListener(MoodsListener listener) {

        for (MoodsListener l : this.userMoodsListeners) {
            if (l == listener) {
                this.userMoodsListeners.remove(listener);
                return;
            }
        }

        throw new IllegalArgumentException("listener does not exist in the array.");

    }

    public void removeFollowingMoodsListener(MoodsListener listener) {

        for (MoodsListener l : this.followingMoodsListeners) {
            if (l == listener) {
                this.followingMoodsListeners.remove(listener);
                return;
            }
        }

        throw new IllegalArgumentException("listener does not exist in the array.");

    }

    public void removeFollowingListener(FollowingListener listener) {

        for (FollowingListener l : this.followingListeners) {
            if (l == listener) {
                this.followingListeners.remove(listener);
                return;
            }
        }

        throw new IllegalArgumentException("listener does not exist in the array.");

    }

    public void removeRequestsListener(RequestsListener listener) {

        for (RequestsListener l : this.requestsListeners) {
            if (l == listener) {
                this.requestsListeners.remove(listener);
                return;
            }
        }

        throw new IllegalArgumentException("listener does not exist in the array.");

    }


    // Listener update commands //


    private void updateUserMoodsListeners(User user) {

        List<Mood> moodList = new ArrayList<>(this.moodList.get(user.getUsername()));

        Collections.sort(moodList, new MoodComparator());

        for (MoodsListener listener : this.userMoodsListeners)
            listener.onUpdate(moodList);

    }

    private void updateFollowingMoodsListeners(User user) {

        List<String> followingList = new ArrayList<>(this.followerList.get(user.getUsername()));
        List<Mood> moodList = new ArrayList<>();

        for (String username : followingList) {

            // Get mood list of the particular username and sort
            List<Mood> tempMoodList = new ArrayList<>(this.moodList.get(username));
            Collections.sort(tempMoodList, new MoodComparator());

            // Add the most recent one if the list was non empty
            if (tempMoodList.size() > 0)
                moodList.add(tempMoodList.get(0));

        }

        // Sort the final list because the username order may not be correct
        Collections.sort(moodList, new MoodComparator());

        // Update all the listeners
        for (MoodsListener listener : this.followingMoodsListeners)
            listener.onUpdate(moodList);

    }

    private void updateFollowingListeners(User user) {

        List<String> followerList = new ArrayList<>(this.followerList.get(user.getUsername()));

        for (FollowingListener listener : this.followingListeners)
            listener.onUpdate(followerList);

    }

    private void updateRequestsListeners(User user) {

        // Original DB list
        List<Request> dbRequestList = this.requestList;

        // Final request list for the user
        List<Request> requestList = new ArrayList<>();

        // For every request in the base List, if the "to" field matches the current user's username, ti's intended for them. Therefore, add it to the requestList
        for (Request request : dbRequestList)
            if (request.getTo().equals(user.getUsername()))
                requestList.add(request);

        for (RequestsListener listener : this.requestsListeners)
            listener.onUpdate(requestList);

    }


    // Implemented methods //


    @Override
    public void userExists(String username, OnSuccessListener<Boolean> successListener, OnFailureListener failureListener) {

        if (this.userList.get(username) == null) {

            if (successListener != null)
                successListener.onSuccess(false);

        } else {

            if (successListener != null)
                successListener.onSuccess(true);

        }

    }

    @Override
    public void registerUser(String username, String password, String firstName, String lastName, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {

        // Check if the username, password, firstName, and lastName are at least one character.
        if (username.length() <= 0 || password.length() <= 0 || firstName.length() <= 0 || lastName.length() <= 0)
            throw new IllegalArgumentException("Any of username, password, firstName, and lastName have to be at least one character.");

        // If the user already exists, fail and exit.
        if (this.userList.get(username) != null) {

            if (failureListener != null)
                failureListener.onFailure(new RuntimeException("User already exists"));

            return;

        }

        // The user DNE so we "register" a new one
        this.userList.put(username, new User(username, firstName, lastName));

        // Add a password, as well as a follower and mood list.
        this.passwordList.put(username, password);
        this.followerList.put(username, new ArrayList<String>());
        this.moodList.put(username, new ArrayList<Mood>());

        // Call the success case
        if (successListener != null)
            successListener.onSuccess(null);

    }

    @Override
    public void validateUser(String username, String password, OnSuccessListener<User> successListener, OnFailureListener failureListener) {

        User dbUser = this.userList.get(username);
        String dbPassword = this.passwordList.get(username);

        // Handle case when username/password combo is incorrect.
        if ( dbUser == null || !password.equals(dbPassword) ) {

            if (successListener != null)
                successListener.onSuccess(null);

        } else {

            if (successListener != null)
                successListener.onSuccess(dbUser);

        }


    }

    @Override
    public ListenerRegistration getUserMoods(User user, MoodsListener listener) {

        // Add listener to the list
        this.userMoodsListeners.add(listener);

        // Update all listeners
        this.updateUserMoodsListeners(user);

        // Return registration
        return new MockMoodsListenerRegistration(this, listener);

    }

    @Override
    public ListenerRegistration getFollowingList(User user, FollowingListener listener) {

        // Add listener to the list
        this.followingListeners.add(listener);

        // Update all listeners
        this.updateFollowingListeners(user);

        // Return registration
        return new MockFollowingListenerRegistration(this, listener);

    }

    @Override
    public ListenerRegistration getFollowingMoods(User user, MoodsListener listener) {

        // Add listener to the list
        this.followingMoodsListeners.add(listener);

        // Update all listeners
        this.updateFollowingMoodsListeners(user);

        // Return registration
        return new MockFollowingMoodsListenerRegistration(this, listener);

    }

    @Override
    public void createMood(User user, Mood mood, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {

        // Since the Firestore ID is final in the class, we can assume that if it has one, it came from the database. We don't want to add another duplicate mood to the database if it already exists. In other words we play it safe
        if (mood.getFirestoreId() != null)
            throw new IllegalArgumentException("This mood cannot be from the database -- it must be created as new.");

        User dbUser = this.userList.get(user.getUsername());
        List<Mood> dbMoodList = this.moodList.get(user.getUsername());

        if (dbUser == null) {

            if (failureListener != null)
                failureListener.onFailure(new RuntimeException("User does not exist."));

            return;

        }

        if (dbMoodList == null) {

            if (failureListener != null)
                failureListener.onFailure(new RuntimeException("Mood list for user does not exist"));

            return;

        }

        dbMoodList.add(new Mood(

                this.globalAutoIndex.toString(),
                mood.getState(),
                mood.getDatetime(),
                mood.getSituation(),
                mood.getReason(),
                mood.getLocation(),
                mood.getImage()

        ));

        this.globalAutoIndex++;

        if (successListener != null)
            successListener.onSuccess(null);

        this.updateUserMoodsListeners(user);
        this.updateFollowingMoodsListeners(user);

    }

    @Override
    public void deleteMood(User user, Mood mood, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {

        // Any mood that is passed into this task has to have a FirestoreId. If it doesn't, then this mood was not created as valid.
        if (mood.getFirestoreId() == null)
            throw new IllegalArgumentException("This mood cannot be new -- it must be created from the database");

        User dbUser = this.userList.get(user.getUsername());
        List<Mood> dbMoodList = this.moodList.get(user.getUsername());

        if (dbUser == null) {

            if (failureListener != null)
                failureListener.onFailure(new RuntimeException("User does not exist."));

            return;

        }

        if (dbMoodList == null) {

            if (failureListener != null)
                failureListener.onFailure(new RuntimeException("Mood list for user does not exist"));

            return;

        }

        // Scan through all the moods
        for (Mood dbMood : dbMoodList) {

            // If the dbMood matches the firestore ID, remove that particular mood.
            if (dbMood.getFirestoreId().equals(mood.getFirestoreId())) {

                dbMoodList.remove(dbMood);

                // Success case
                if (successListener != null)
                    successListener.onSuccess(null);

                this.updateUserMoodsListeners(user);
                this.updateFollowingMoodsListeners(user);

                return;

            }

        }

        // No mood was found, so this is the failure case
        if (failureListener != null)
            failureListener.onFailure(new RuntimeException("Mood does not exist in the DB"));

    }

    @Override
    public void updateMood(User user, Mood mood, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {

        // Any mood that is passed into this task has to have a FirestoreId. If it doesn't, then this mood was not created as valid.
        if (mood.getFirestoreId() == null)
            throw new IllegalArgumentException("This mood cannot be new -- it must be created from the database");

        User dbUser = this.userList.get(user.getUsername());
        List<Mood> dbMoodList = this.moodList.get(user.getUsername());

        if (dbUser == null) {

            if (failureListener != null)
                failureListener.onFailure(new RuntimeException("User does not exist."));

            return;

        }

        if (dbMoodList == null) {

            if (failureListener != null)
                failureListener.onFailure(new RuntimeException("Mood list for user does not exist"));

            return;

        }

        for (int i = 0; i < dbMoodList.size(); i++) {

            if (dbMoodList.get(i).getFirestoreId().equals(mood.getFirestoreId())) {

                dbMoodList.set(i, mood);

                if (successListener != null)
                    successListener.onSuccess(null);

                this.updateUserMoodsListeners(user);
                this.updateFollowingMoodsListeners(user);

                return;

            }

        }

        if (failureListener != null)
            failureListener.onFailure(new RuntimeException("Mood does not exist in the database"));

    }

    @Override
    public ListenerRegistration getUserRequests(User user, RequestsListener listener) {

        // Add listener to the list
        this.requestsListeners.add(listener);

        // Update all listeners
        this.updateRequestsListeners(user);

        // Return registration
        return new MockRequestsListenerRegistration(this, listener);

    }

    @Override
    public void createRequest(Request request, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {

        if (request.getFirestoreId() != null)
            throw new IllegalArgumentException("This request cannot be from the database -- it must be created as new.");

        if (request.getFrom().equals(request.getTo())) {

            if (failureListener != null)
                failureListener.onFailure(new RuntimeException("A user cannot request to follow themselves."));

            return;

        }

        List<String> dbFollowList = this.followerList.get(request.getFrom());

        // Check if the user already follows the user he's trying to request.
        if (dbFollowList.contains(request.getTo())) {

            if (failureListener != null)
                failureListener.onFailure(new RuntimeException("Current user is already following the requested user"));

            return;

        }

        // Check if request already exists
        for (Request dbRequest : this.requestList) {

            if (dbRequest.getFirestoreId().equals(request.getFrom() + request.getTo())) {

                if (failureListener != null)
                    failureListener.onFailure(new RuntimeException("This request already exists"));

                return;

            }

        }

        // Create a new request with the ID in it
        request = new Request(request.getFrom() + request.getTo(), request.getFrom(), request.getTo());

        // Add to request list
        this.requestList.add(request);

        // Success case
        if (successListener != null)
            successListener.onSuccess(null);

        this.updateRequestsListeners(this.userList.get(request.getFrom()));

    }

    @Override
    public void acceptRequest(Request request, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {

        // If this document comes from the database (and we will know that for sure because we reference it by ID) then we know that both the "from" and "to" user exists. Therefore we don't have to do any user checking on the client side as we can be sure that both users exists to be able to complete the transaction.
        if (request.getFirestoreId() == null)
            throw new IllegalArgumentException("This request cannot be new -- it must be created from the database");

        List<String> dbFollowList = this.followerList.get(request.getFrom());

        if (dbFollowList == null) {

            if (failureListener != null)
                failureListener.onFailure(new RuntimeException("Follow List does not exist for the user"));

            return;

        }

        // Check if request already exists
        for (Request dbRequest : this.requestList) {

            if (dbRequest.getFirestoreId().equals(request.getFrom() + request.getTo())) {

                // Add to follow list and delete request.
                dbFollowList.add(request.getTo());
                this.requestList.remove(dbRequest);

                if (successListener != null)
                    successListener.onSuccess(null);

                this.updateRequestsListeners(this.userList.get(request.getFrom()));

                return;

            }

        }

        if (failureListener != null)
            failureListener.onFailure(new RuntimeException("Request does not exist."));

    }

    @Override
    public void declineRequest(Request request, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {

        // If this document comes from the database (and we will know that for sure because we reference it by ID) then we know that both the "from" and "to" user exists. Therefore we don't have to do any user checking on the client side as we can be sure that both users exists to be able to complete the transaction.
        if (request.getFirestoreId() == null)
            throw new IllegalArgumentException("This request cannot be new -- it must be created from the database");

        // Check if request already exists
        for (Request dbRequest : this.requestList) {

            if (dbRequest.getFirestoreId().equals(request.getFrom() + request.getTo())) {

                // Add to follow list and delete request.
                this.requestList.remove(dbRequest);

                if (successListener != null)
                    successListener.onSuccess(null);

                this.updateRequestsListeners(this.userList.get(request.getFrom()));

                return;

            }

        }

        if (failureListener != null)
            failureListener.onFailure(new RuntimeException("Request does not exist."));

    }

}
