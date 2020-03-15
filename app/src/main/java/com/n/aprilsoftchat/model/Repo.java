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

    private Timer timer;

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

    public void sendMessage(String text) {
        String username = mPrefs.getString("username", "default_user");

        Call<Void> callback = mClient.sendMessage(username, text);
        callback.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    event.setValue(new Event<>(Event.EVENT_RESPONSE_ERROR, "Error. Code: " + response.code()));
                    return;
                }

                event.setValue(new Event<>(Event.EVENT_MESSAGE_SEND, "Success. Code: " + response.code()));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                event.setValue(new Event<>(Event.EVENT_REQUEST_FAILURE, "Failure. " + t.getMessage()));
            }
        });
    }

    public void startRequestingMessages() {
        timer = new Timer();

        final Handler handler = new Handler();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Call<Message> callback = mClient.getMessages(0);
                        callback.enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                if (!response.isSuccessful() || response.body() == null) {
                                    event.setValue(new Event<>(Event.EVENT_RESPONSE_ERROR, "Error. Code: " + response.code()));
                                    return;
                                }

                                messages.setValue(response.body().getMessages());
                            }

                            @Override
                            public void onFailure(Call<Message> call, Throwable t) {
                                event.setValue(new Event<>(Event.EVENT_REQUEST_FAILURE, "Failure. " + t.getMessage()));
                            }
                        });
                    }
                });
            }
        };

        timer.schedule(task, 0, 5000);
    }

    public void stopRequestingMessages() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    public MutableLiveData<Event<Integer, String>> getEvent() {
        return event;
    }

    public MutableLiveData<List<Message>> getMessages() {
        return messages;
    }

}