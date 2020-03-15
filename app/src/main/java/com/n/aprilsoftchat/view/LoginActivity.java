package com.n.aprilsoftchat.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.n.aprilsoftchat.R;
import com.n.aprilsoftchat.model.Event;
import com.n.aprilsoftchat.viewmodel.LoginActivityViewModel;

public class LoginActivity extends AppCompatActivity {

    private LoginActivityViewModel viewModel;

    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViewModel();
        initViews();
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(LoginActivityViewModel.class);
        viewModel.getEvent().observe(this, new Observer<Event<Integer, String>>() {
            @Override
            public void onChanged(Event<Integer, String> event) {
                if (event.isHandled()) return;

                int key = event.getKey();
                String value = event.getValueIfNotHandled();

                switch (key) {
                    case Event.EVENT_RESPONSE_ERROR:
                    case Event.EVENT_LOGIN_FAILURE:
                    case Event.EVENT_REQUEST_FAILURE: {
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case Event.EVENT_LOGIN_SUCCESS: {
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        startChatActivity();
                        break;
                    }
                }

            }
        });
    }

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
    }

    public void login(View view) {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter username and password", Toast.LENGTH_LONG).show();
            return;
        }

        viewModel.login(username, password);
    }

    private void startChatActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}
