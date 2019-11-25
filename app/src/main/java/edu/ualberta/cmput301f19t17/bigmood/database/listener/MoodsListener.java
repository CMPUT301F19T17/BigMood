package edu.ualberta.cmput301f19t17.bigmood.database.listener;

import java.util.List;

import edu.ualberta.cmput301f19t17.bigmood.model.Mood;

/**
 * This interface defines a callback method for live mood updates from the database the repository pulls from.
 */
public interface MoodsListener {
    /**
     * This method is called whenever the listener hears that there is an update in the moodList
     * in FireStore, and updates the list, and applies a filter, if the user has selected one
     * @param moodList the new list that has the updated values
     */
    void onUpdate(List<Mood> moodList);

}
