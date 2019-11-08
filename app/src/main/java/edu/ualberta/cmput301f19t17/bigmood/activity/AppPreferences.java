package edu.ualberta.cmput301f19t17.bigmood.activity;

import edu.ualberta.cmput301f19t17.bigmood.database.FirestoreRepository;
import edu.ualberta.cmput301f19t17.bigmood.database.Repository;
import edu.ualberta.cmput301f19t17.bigmood.database.User;

/**
 * This Preferences class is a singleton class that holds the currently activated User,
 * as well as the reference to a repository interface. This is in most cases Firestore
 * but could point to an in-memory database for testing purposes for example.
 */
public class AppPreferences {

    private static AppPreferences preferences = null;

    private User currentUser;
    private Repository repository;

    /**
     * Since this is a singleton, we force the user of the class to call getInstance()
     * which will create a new instance of the class if the instance does not already exist.
     * This ensures there is a single instance, hence "singleton"
     * @return the instance of AppPreferences
     */
    public static AppPreferences getInstance() {
        if (preferences == null)
            preferences = new AppPreferences();

        return preferences;
    }

    /**
     * This constructor initializes the class attributes.
     * We set currentUser to null since it is the job of the repository to
     * validate the user whenever they try to login. At that point, we will
     * set the user using the setter below
     */
    private AppPreferences() {
        this.currentUser = null;
        this.repository = FirestoreRepository.getInstance();
    }

    /**
     * This method sets the current user to a new user.
     * This is called whenever a user signs in
     * @param user the user to set
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * This method sets the repository to a new repository.
     * This can be used for replacing the repository class
     * with a testable in-memory database.
     * @param repository the repo to set
     */
    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    /**
     * This method returns the current user.
     * This is useful for firestore access
     * @return the current user
     */
    public User getCurrentUser() {
        return this.currentUser;
    }

    /**
     * This method returns the current user.
     * This is useful for accessing firestore methods in the database package, as
     * the repository contains all of the methods to access firestore
     * @return the repository
     */
    public Repository getRepository() {
        return this.repository;
    }
}
