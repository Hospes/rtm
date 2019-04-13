package ua.hospes.rtm.core.ui;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ThemedSpinnerAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Andrew Khloponin
 */
public abstract class AbsSpinnerAdapter<T, VH extends AbsSpinnerAdapter.ViewHolder> extends BaseAdapter implements ThemedSpinnerAdapter {
    private final Object mLock = new Object();
    private final Resources resources;
    private final LayoutInflater mInflater;
    private final List<T> objects;
    private boolean mNotifyOnChange = true;


    public AbsSpinnerAdapter(@NonNull Context mContext) {
        this.resources = mContext.getResources();
        this.mInflater = LayoutInflater.from(mContext);
        this.objects = new ArrayList<>();
    }

    public AbsSpinnerAdapter(@NonNull Context mContext, List<T> objects) {
        this.resources = mContext.getResources();
        this.mInflater = LayoutInflater.from(mContext);
        this.objects = objects == null ? new ArrayList<>() : objects;
    }

    public AbsSpinnerAdapter(@NonNull Context mContext, T... objects) {
        this.resources = mContext.getResources();
        this.mInflater = LayoutInflater.from(mContext);
        this.objects = new ArrayList<>();
        Collections.addAll(this.objects, objects);
    }


    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     */
    public void add(@Nullable T object) {
        synchronized (mLock) {
            objects.add(object);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
     *                                       is not supported by this list
     * @throws ClassCastException            if the class of an element of the specified
     *                                       collection prevents it from being added to this list
     * @throws NullPointerException          if the specified collection contains one
     *                                       or more null elements and this list does not permit null
     *                                       elements, or if the specified collection is null
     * @throws IllegalArgumentException      if some property of an element of the
     *                                       specified collection prevents it from being added to this list
     */
    public void addAll(@NonNull Collection<? extends T> collection) {
        synchronized (mLock) {
            objects.addAll(collection);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Adds the specified items at the end of the array.
     *
     * @param items The items to add at the end of the array.
     */
    public void addAll(T... items) {
        synchronized (mLock) {
            Collections.addAll(objects, items);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Inserts the specified object at the specified index in the array.
     *
     * @param object The object to insert into the array.
     * @param index  The index at which the object must be inserted.
     */
    public void insert(@Nullable T object, int index) {
        synchronized (mLock) {
            objects.add(index, object);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Removes the specified object from the array.
     *
     * @param object The object to remove.
     */
    public void remove(@Nullable T object) {
        synchronized (mLock) {
            objects.remove(object);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        synchronized (mLock) {
            objects.clear();
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }


    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public T getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Resources getResources() {
        return resources;
    }


    protected abstract VH onCreateViewHolder(LayoutInflater inflater);

    protected abstract void onBindViewHolder(VH holder, T item, int position);


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH holder;
        if (convertView == null) {
            holder = onCreateViewHolder(mInflater);
            convertView = holder.itemView;
            convertView.setTag(holder);
        } else {
            holder = (VH) convertView.getTag();
        }
        onBindViewHolder(holder, getItem(position), position);
        return convertView;
    }


    @Override
    public void setDropDownViewTheme(@Nullable Resources.Theme theme) {}

    @Nullable
    @Override
    public Resources.Theme getDropDownViewTheme() {
        return null;
    }

    public abstract class ViewHolder {
        View itemView;


        private ViewHolder(View itemView) {
            this.itemView = itemView;
            findViews(itemView);
        }

        public ViewHolder(LayoutInflater inflater, @LayoutRes int layoutId) {
            this(inflater.inflate(layoutId, null, false));
        }


        protected abstract void findViews(View itemView);

        @SuppressWarnings("unchecked")
        public <V> V findView(View root, @IdRes int id) {
            if (root == null) return null;
            try {
                return (V) root.findViewById(id);
            } catch (ClassCastException e) {
                return null;
            }
        }

        @SuppressWarnings("unchecked")
        public <V> V findView(@IdRes int id) {
            return findView(itemView, id);
        }
    }
}