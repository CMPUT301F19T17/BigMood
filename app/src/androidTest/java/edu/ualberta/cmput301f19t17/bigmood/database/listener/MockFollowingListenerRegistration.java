package edu.ualberta.cmput301f19t17.bigmood.database.listener;

import com.google.firebase.firestore.ListenerRegistration;

import edu.ualberta.cmput301f19t17.bigmood.database.MockRepository;

public class MockFollowingListenerRegistration implements ListenerRegistration {

    private MockRepository repository;

    private FollowingListener listener;

    public MockFollowingListenerRegistration(MockRepository repository, FollowingListener listener) {
        this.repository = repository;
        this.listener = listener;
    }

    // We want to override the standard Listener registration behaviour so we make the remove() do nothing.
    @Override
    public void remove() {

        this.repository.removeFollowingListener(this.listener);

    }

}
