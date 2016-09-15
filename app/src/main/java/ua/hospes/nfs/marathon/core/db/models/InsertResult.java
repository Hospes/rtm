package ua.hospes.nfs.marathon.core.db.models;

public class InsertResult<T> extends AbsDbResult<T> {
    private long result;


    public InsertResult(long result, T data) {
        super(data);
        this.result = result;
    }


    public long getResult() {
        return result;
    }


    @Override
    public String toString() {
        return "InsertResult{" +
                "result=" + result + ", " +
                "data=" + getData() +
                "}";
    }
}