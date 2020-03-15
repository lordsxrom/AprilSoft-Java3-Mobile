package com.n.aprilsoftchat.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.n.aprilsoftchat.R;
import com.n.aprilsoftchat.model.Event;
import com.n.aprilsoftchat.model.Message;
import com.n.aprilsoftchat.viewmodel.MainActivityViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel viewModel;

    private EditText etMessage;

    private RecyclerView rvChatbox;
    private MessageAdapter adapter;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("AprilSoft", MODE_PRIVATE);

        checkLogin();

        initViewModel();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.startRequestingMessages();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.stopRequestingMessages();
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        viewModel.getEvent().observe(this, new Observer<Event<Integer, String>>() {
            @Override
            public void onChanged(Event<Integer, String> event) {
                if (event.isHandled()) return;

                int key = event.getKey();
                String value = event.getValueIfNotHandled();

                switch (key) {
                    case Event.EVENT_RESPONSE_ERROR:
                    case Event.EVENT_REQUEST_FAILURE:
                    case Event.EVENT_MESSAGE_SEND: {
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        });

        viewModel.getMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                adapter.setData(messages);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void checkLogin() {
        String username = preferences.getString("username", "");
        if (username.isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initViews() {
        etMessage = findViewById(R.id.et_message);

        rvChatbox = findViewById(R.id.rv_chatbox);
        rvChatbox.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MessageAdapter(getApplicationContext());
        rvChatbox.setAdapter(adapter);
    }

    public void send(View view) {
        String text = etMessage.getText().toString().trim();
        if (text.isEmpty()) return;

        etMessage.getText().clear();
        viewModel.sendMessage(text);
    }

    public void logout(View view) {
        preferences.edit().remove("username").apply();

        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

}
