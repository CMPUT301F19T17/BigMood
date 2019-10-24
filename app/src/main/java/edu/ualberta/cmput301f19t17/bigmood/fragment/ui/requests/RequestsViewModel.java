package edu.ualberta.cmput301f19t17.bigmood.fragment.ui.requests;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RequestsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RequestsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Requests fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}