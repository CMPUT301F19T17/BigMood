package edu.ualberta.cmput301f19t17.bigmood.fragment.ui.requests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import edu.ualberta.cmput301f19t17.bigmood.R;

/**
 * RequestsFragment is used to view the follow requests that the user has received.
 */
public class RequestsFragment extends Fragment {

    private RequestsViewModel requestsViewModel;

    /**
     * of the on*()methods, this is the second. After the dialog has been started we want to inflate the dialog.
     * This is where we inflate all the views and *if applicable* populate all the fields.
     * @param inflater           View inflater service
     * @param container          Container that the inflater is housed in
     * @param savedInstanceState A bundle that holds the state of the fragment
     * @return                   Returns the inflated view
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.requestsViewModel = ViewModelProviders.of(this).get(RequestsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_requests, container, false);

        return root;
    }
}