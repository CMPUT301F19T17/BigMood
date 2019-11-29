package edu.ualberta.cmput301f19t17.bigmood.database.listener;

import java.util.List;

/**
 * This interface defines a callback method for live mood updates from the database the repository pulls from.
 */
public interface FollowingListener {
    /**
     * This method is called whenever the listener hears that there is an update in the moodList
     * in FireStore, and when implemented should update the local list to fit the new list in Firestore.
     * @param followingList the list of people the user is following
     */
    void onUpdate(List<String> followingList);

}
