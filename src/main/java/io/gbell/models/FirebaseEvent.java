package io.gbell.models;

public class FirebaseEvent<T> {

    public static final int ADDED = 0;
    public static final int CHANGED = 1;
    public static final int REMOVED = 2;
    public static final int MOVED = 3;

    private final int action;
    private final T data;

    public FirebaseEvent(int action, T data) {
        this.action = action;
        this.data = data;
    }

    public int getAction() {
        return action;
    }

    public T getData() {
        return data;
    }
}
