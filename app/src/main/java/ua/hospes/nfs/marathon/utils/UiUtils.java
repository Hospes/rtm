package ua.hospes.nfs.marathon.utils;

import android.app.Activity;
import android.view.View;

public class UiUtils {
    @SuppressWarnings("unchecked")
    public static <T> T findView(View root, int id) {
        if (root == null) return null;
        try {
            return (T) root.findViewById(id);
        } catch (ClassCastException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T findView(View root, Object tag) {
        if (root == null) return null;
        try {
            return (T) root.findViewWithTag(tag);
        } catch (ClassCastException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T findView(Activity root, int id) {
        if (root == null) return null;
        try {
            return (T) root.findViewById(id);
        } catch (ClassCastException e) {
            return null;
        }
    }
}