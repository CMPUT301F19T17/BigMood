package edu.ualberta.cmput301f19t17.bigmood.activity;

import androidx.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

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

    private Repository repository;

    private User currentUser;
    private List<String> followingList;

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
        this.followingList = new ArrayList<>();
        this.repository = FirestoreRepository.getInstance();
    }

    /**
     * This method logs out the current user and clears all related variables.
     */
    public void logout() {

        // In order to log out the current user we have to clear the currentUser variable and clear the followingList so that it does not bleed over into the next login.
        this.currentUser = null;
        this.followingList.clear();

    }

    /**
     * This method sets the current user to a validated user. This "logs" the user into the system.
     * @param user the <code>User</code>> to log in
     */
    public void login(User user) {

        if (user == null)
            throw new IllegalArgumentException("You cannot log in with a null user. If you were looking to log out. use the logout() method.");

        // Just for safety, we log out first before "logging in" again.
        this.logout();

        this.currentUser = user;
    }

    /**
     * This method sets the repository to a new repository.
     * This can be used for replacing the repository class
     * with a testable in-memory database.
     * @param repository the repo to set
     */
    @VisibleForTesting
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

    /**
     * This method returns the cached follower list from the
     * @return
     */
    public List<String> getFollowingList() {
        return followingList;
    }

}
