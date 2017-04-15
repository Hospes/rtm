package ua.hospes.rtm.core.ui;

/**
 * @author Andrew Khloponin
 */
public abstract class BasePresenter<V> {
    private V view;

    public void attachView(V view) {
        this.view = view;
    }

    public void detachView() {
        this.view = null;
    }

    public final V getView() {
        return view;
    }
}