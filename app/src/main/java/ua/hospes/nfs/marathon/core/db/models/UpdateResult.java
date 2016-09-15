package ua.hospes.nfs.marathon.core.db.models;

public class UpdateResult<T> extends AbsDbResult<T> {
    private long result;


    public UpdateResult(long result, T data) {
        super(data);
        this.result = result;
    }


    public long getResult() {
        return result;
    }


    @Override
    public String toString() {
        return "UpdateResult{" +
                "result=" + result + ", " +
                "data=" + getData() +
                "}";
    }
}