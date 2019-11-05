package edu.ualberta.cmput301f19t17.bigmood.activity;

import edu.ualberta.cmput301f19t17.bigmood.database.Repository;
import edu.ualberta.cmput301f19t17.bigmood.database.User;

/**
 * This Preferences class is a singleton class that holds the currently activated User,
 * as well as the reference to the FireStore Repository, so that all the classes can use them
 * for storing data to FireStore
 */
public class AppPreferences {
    private static AppPreferences preferences = null;

    //Leaving this to do in, not sure what nick had in mind, but to be on the safe side, leaving
    // TODO: 2019-11-02 Nectarios: Implement
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
        this.repository = Repository.getInstance();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public Repository getRepository() {
        return this.repository;
    }
}
