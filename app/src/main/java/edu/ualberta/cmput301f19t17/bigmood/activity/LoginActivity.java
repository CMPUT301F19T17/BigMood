package edu.ualberta.cmput301f19t17.bigmood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputLayout;

import edu.ualberta.cmput301f19t17.bigmood.R;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;

    private AppViewModel appViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);

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

                // DEBUG FOR NOW //
                Toast.makeText(LoginActivity.this, "\"Logged In\"", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                LoginActivity.this.startActivity(intent);


            }
        });


    }

}
