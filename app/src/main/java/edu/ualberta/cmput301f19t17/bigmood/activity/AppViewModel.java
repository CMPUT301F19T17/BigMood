package edu.ualberta.cmput301f19t17.bigmood.activity;

import androidx.lifecycle.ViewModel;

import edu.ualberta.cmput301f19t17.bigmood.database.FirestoreRepository;
import edu.ualberta.cmput301f19t17.bigmood.database.Repository;
import edu.ualberta.cmput301f19t17.bigmood.database.User;

/**
 * A class responsible for holding data that ALL the activity and navigation fragments need to access. In particular this can include the currently logged in user and the database repository
 */
public class AppViewModel extends ViewModel {

    // TODO: 2019-11-02 Nectarios: Implement 
    private User currentUser;
    private Repository repository;

    /**
     * Basic constructor for this ViewModel. This should in theory only be run once per session as it contains information that all activities and fragments need to access.
     */
    public AppViewModel() {
        this.currentUser = null;
        this.repository = FirestoreRepository.getInstance();
    }

    /**
     * Gets the current logged in user. A value of null represents
     * @return
     */
    public User getCurrentUser() {
        return this.currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Repository getRepository() {
        return this.repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }
}
