package edu.ualberta.cmput301f19t17.bigmood.fragment.ui.requests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import edu.ualberta.cmput301f19t17.bigmood.R;

public class RequestsFragment extends Fragment {

    private RequestsViewModel requestsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        requestsViewModel = ViewModelProviders.of(this).get(RequestsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_requests, container, false);
        final TextView textView = root.findViewById(R.id.text_requests);
        requestsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}