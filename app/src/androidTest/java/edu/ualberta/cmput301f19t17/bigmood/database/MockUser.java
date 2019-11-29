package edu.ualberta.cmput301f19t17.bigmood.database;

/**
 * This is a class meant for testing. It is used by MockRepository to simulate having a user in Firestore.
 */
public class MockUser extends User {

    /**
     * Constructor for MockUser
     * @param username the username of the MockUser
     * @param firstname the first name of the MockUser
     * @param lastname the last name of the MockUser
     */
    public MockUser(String username, String firstname, String lastname) {
        super(username, firstname, lastname);
    }
}
