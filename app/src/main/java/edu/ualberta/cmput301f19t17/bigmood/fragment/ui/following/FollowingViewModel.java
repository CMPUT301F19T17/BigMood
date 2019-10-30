package edu.ualberta.cmput301f19t17.bigmood.fragment.ui.following;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FollowingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FollowingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Following fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}