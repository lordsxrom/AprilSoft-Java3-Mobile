package com.n.aprilsoftchat.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.n.aprilsoftchat.model.Event;
import com.n.aprilsoftchat.model.Message;
import com.n.aprilsoftchat.model.Repo;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private Repo repo;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        repo = Repo.getRepo(application);
    }

    public void sendMessage(String text) {
        repo.sendMessage(text);
    }

    public MutableLiveData<Event<Integer, String>> getEvent() {
        return repo.getEvent();
    }

    public MutableLiveData<List<Message>> getMessages() {
        return repo.getMessages();
    }

    public void startRequestingMessages() {
        repo.startRequestingMessages();
    }

    public void stopRequestingMessages() {
        repo.stopRequestingMessages();
    }
}
