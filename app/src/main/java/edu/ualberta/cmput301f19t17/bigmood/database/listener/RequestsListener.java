package edu.ualberta.cmput301f19t17.bigmood.database.listener;

import java.util.List;

import edu.ualberta.cmput301f19t17.bigmood.model.Request;

public interface RequestsListener {

    void onUpdate(List<Request> moodList);

}
