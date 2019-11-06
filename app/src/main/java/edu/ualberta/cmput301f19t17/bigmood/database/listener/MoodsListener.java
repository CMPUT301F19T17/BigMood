package edu.ualberta.cmput301f19t17.bigmood.database.listener;

import java.util.List;

import edu.ualberta.cmput301f19t17.bigmood.model.Mood;

public interface MoodsListener {

    void onUpdate(List<Mood> moodList);

}
