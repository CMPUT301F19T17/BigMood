package edu.ualberta.cmput301f19t17.bigmood.database.listener;

import java.util.List;

import edu.ualberta.cmput301f19t17.bigmood.model.Request;

/**
 * This interface is used to listen to updates in the requests-to-follow list
 */
public interface RequestsListener {

    void onUpdate(List<Request> requestList);

}
