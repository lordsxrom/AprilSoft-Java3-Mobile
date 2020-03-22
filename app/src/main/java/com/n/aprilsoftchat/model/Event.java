package com.n.aprilsoftchat.model;

public class Event<K, V> {

    public static final int EVENT_RESPONSE_ERROR = 0;
    public static final int EVENT_LOGIN_FAILURE = 1;
    public static final int EVENT_REQUEST_FAILURE = 2;

    public static final int EVENT_LOGIN_SUCCESS = 3;
    public static final int EVENT_MESSAGE_SEND = 4;

    private K key;
    private V value;
    private boolean handled = false;

    public Event(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public V getValueIfNotHandled() {
        if (handled) {
            return null;
        }
        handled = true;
        return value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public boolean isHandled() {
        return handled;
    }
    
}
