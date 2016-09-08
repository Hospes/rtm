package ua.hospes.nfs.marathon.core.db.models;

public abstract class AbsDbResult<T> {
    private final T data;


    public AbsDbResult(T data) {
        this.data = data;
    }


    public T getData() {
        return data;
    }
}