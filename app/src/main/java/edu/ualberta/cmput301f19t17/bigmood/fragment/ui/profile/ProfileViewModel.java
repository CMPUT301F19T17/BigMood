package edu.ualberta.cmput301f19t17.bigmood.fragment.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * While a refactor is necessary for this to be implemented the idea is to follow the Android Lifecycle Components and abstract the business logic to this class.
 */
public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Profile Fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}