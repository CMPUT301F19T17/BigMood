package edu.ualberta.cmput301f19t17.bigmood.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import edu.ualberta.cmput301f19t17.bigmood.R;

public class SignUpActivity extends AppCompatActivity {

    AppViewModel appViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);

        // get Toolbar
        Toolbar toolbar = this.findViewById(R.id.toolbar_activity_sign_up);

        // Set Title
        toolbar.setTitle(this.getString(R.string.title_activity_sign_up));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpActivity.this.onBackNavigationClicked();
            }
        });

    }

    // Software back navigation
    private void onBackNavigationClicked() {

        Log.d(HomeActivity.LOG_TAG, "Back navigation (Software) from " + this.getClass().getSimpleName());
        this.finish();

    }

    // Hardware back navigation
    @Override
    public void onBackPressed() {

        Log.d(HomeActivity.LOG_TAG, "Back navigation (Hardware) from " + this.getClass().getSimpleName());
        this.finish();

    }

}
