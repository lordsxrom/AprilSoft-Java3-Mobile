package com.n.aprilsoftchat.model;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import androidx.lifecycle.MutableLiveData;

import com.n.aprilsoftchat.internet.APIService;
import com.n.aprilsoftchat.internet.RetrofitClient;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class Repo {

    private static Repo repo;

    private Context mContext;
    private APIService mClient;
    private SharedPreferences mPrefs;

    private MutableLiveData<Event<Integer, String>> event = new MutableLiveData<>();
    private MutableLiveData<List<Message>> messages = new MutableLiveData<>();

    public static Repo getRepo(Application application) {
        if (repo == null) {
            return new Repo(application);
        }
        return repo;
    }

    private Repo(Application application) {
        mContext = application.getApplicationContext();
        mClient = RetrofitClient.getService();
        mPrefs = application.getSharedPreferences("AprilSoft", MODE_PRIVATE);
    }

    public void login(final String username, String password) {
        Call<String> callback = mClient.login(username, password);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    event.setValue(new Event<>(Event.EVENT_RESPONSE_ERROR, "Error. Code: " + response.code()));
                    return;
                }

                String result = response.body();
                if ("login".equals(result)) {
                    event.setValue(new Event<>(Event.EVENT_LOGIN_SUCCESS, "Welcome, " + username));
                    mPrefs.edit().putString("username", username).apply();
                } else if ("fail".equals(result)) {
                    event.setValue(new Event<>(Event.EVENT_LOGIN_FAILURE, "Wrong username or password!"));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                event.setValue(new Event<>(Event.EVENT_REQUEST_FAILURE, "Failure. " + t.getMessage()));
            }
        });
    }

    public MutableLiveData<Event<Integer, String>> getEvent() {
        return event;
    }

    public MutableLiveData<List<Message>> getMessages() {
        return messages;
    }

}