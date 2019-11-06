package edu.ualberta.cmput301f19t17.bigmood.fragment.ui.requests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import edu.ualberta.cmput301f19t17.bigmood.R;

public class RequestsFragment extends Fragment {

    private RequestsViewModel requestsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.requestsViewModel = ViewModelProviders.of(this).get(RequestsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_requests, container, false);

        return root;
    }
}