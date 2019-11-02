package edu.ualberta.cmput301f19t17.bigmood.model;

import edu.ualberta.cmput301f19t17.bigmood.database.User;

public class Request {

    private final String firestoreId;

    private final String from;
    private final String to;

    public Request(User user, String to) {

        if (user == null || to == null)
            throw new IllegalArgumentException("Both the 'user' and 'to' fields have to exist.");

        this.firestoreId = null;

        this.from = user.getUsername();
        this.to = to;

    }

    public Request(String firestoreId, String from, String to) {

        if (firestoreId == null || from == null || to == null)
            throw new IllegalArgumentException("All of firestoreId, from, and to fields have to exist.");

        this.firestoreId = firestoreId;

        this.from = from;
        this.to = to;
    }

    public String getFirestoreId() {
        return firestoreId;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

}
