package edu.ualberta.cmput301f19t17.bigmood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;

import edu.ualberta.cmput301f19t17.bigmood.R;
import edu.ualberta.cmput301f19t17.bigmood.database.User;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;

    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.appPreferences = AppPreferences.getInstance();

        this.textInputUsername = (TextInputLayout) this.findViewById(R.id.text_input_username);
        this.textInputPassword = (TextInputLayout) this.findViewById(R.id.text_input_password);

        Button buttonLogin = (Button) this.findViewById(R.id.button_login);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean failed = false;

                String username = LoginActivity.this.textInputUsername.getEditText().getText().toString().trim();
                String password = LoginActivity.this.textInputPassword.getEditText().getText().toString();

                if (username.isEmpty()) {
                    LoginActivity.this.textInputUsername.setError("Username cannot be empty.");
                    failed = true;
                } else {
                    LoginActivity.this.textInputUsername.setError(null);
                }

                if (password.isEmpty()) {
                    LoginActivity.this.textInputPassword.setError("Password cannot be empty.");
                    failed = true;
                } else {
                    LoginActivity.this.textInputPassword.setError(null);
                }

                if (failed)
                    return;

                // TODO: 2019-11-04 Nectarios: ScrollView

                LoginActivity.this.appPreferences.getRepository().validateUser(username, password)
                        .addOnSuccessListener(new OnSuccessListener<User>() {
                            @Override
                            public void onSuccess(User user) {

                                // According to
                                if (user == null) {
                                    Toast.makeText(LoginActivity.this, "Username/password incorrect.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // Set user in the ViewModel
                                LoginActivity.this.appPreferences.setCurrentUser(user);

                                // Go to the home screen
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                LoginActivity.this.startActivity(intent);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(LoginActivity.this, "Failed to log in. Please try again.", Toast.LENGTH_SHORT).show();

                                Log.d(HomeActivity.LOG_TAG, "User Validation failed: " + e.toString());

                            }
                        });




            }
        });


    }

}
