package edu.ualberta.cmput301f19t17.bigmood.database.listener;

import java.util.List;

import edu.ualberta.cmput301f19t17.bigmood.model.Mood;

/**
 * This interface is used to listen to updates to the moodList.
 */
public interface MoodsListener {

    void onUpdate(List<Mood> moodList);

}
