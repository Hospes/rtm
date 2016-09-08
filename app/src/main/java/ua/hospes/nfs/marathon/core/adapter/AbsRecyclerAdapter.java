package ua.hospes.nfs.marathon.core.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import rx.Observable;

/**
 * @author Andrew Khloponin
 */
public abstract class AbsRecyclerAdapter<MODEL, HOLDER extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<HOLDER> implements Filterable {
    protected final List<MODEL> items = new ArrayList<>();
    private OnItemClickListener<MODEL> onItemClickListener;
    private OnItemLongClickListener<MODEL> onItemLongClickListener;

    /**
     * Lock used to modify the content of {@link #items}. Any write operation
     * performed on the array should be synchronized on this lock. This lock is also
     * used by the filter (see {@link #getFilter()} to make a synchronized copy of
     * the original array of data.
     */
    private final Object mLock = new Object();

    private List<MODEL> originalItems = null;
    private Filter mFilter;

    @Override
    public int getItemCount() {
        return items.size();
    }

    public MODEL getItem(int position) {
        return position < getItemCount() && position >= 0 ? items.get(position) : null;
    }

    public void clear() {
        if (originalItems != null) originalItems.clear();
        items.clear();
        notifyDataSetChanged();
    }

    public void addAll(Collection<MODEL> collection) {
        if (collection == null) return;
        if (originalItems != null) {
            originalItems.addAll(collection);
        } else {
            items.addAll(collection);
        }
        notifyDataSetChanged();
    }

    public void addAll(MODEL... MODEL) {
        if (originalItems != null) {
            Collections.addAll(originalItems, MODEL);
        } else {
            Collections.addAll(items, MODEL);
        }
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (position < getItemCount() && position >= 0) {
            items.remove(position);
        }
    }

    public void removeItem(MODEL MODEL) {
        items.remove(MODEL);
    }

    public int getItemPosition(MODEL MODEL) {
        return items.indexOf(MODEL);
    }

    public void addAll(int location, Collection<MODEL> MODELs) {
        if (originalItems != null) {
            originalItems.addAll(location, MODELs);
        } else {
            items.addAll(location, MODELs);
        }
        notifyDataSetChanged();
    }

    public void addAll(int location, MODEL... MODELs) {
        if (originalItems != null) {
            originalItems.addAll(location, Arrays.asList(MODELs));
        } else {
            items.addAll(location, Arrays.asList(MODELs));
        }
        notifyDataSetChanged();
    }

    public List<MODEL> getItems() {
        return items;
    }

    public Observable<MODEL> getItemsRx() {
        return Observable.from(items);
    }


    public Filter getFilter() {
        return mFilter == null ? mFilter = new ArrayFilter() : mFilter;
    }

    public List<MODEL> doFiltering(List<MODEL> originalItems, CharSequence prefix) {
        return null;
    }

    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (originalItems == null) {
                synchronized (mLock) {
                    originalItems = new ArrayList<>(items);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<MODEL> list;
                synchronized (mLock) {
                    list = new ArrayList<>(originalItems);
                }
                results.values = list;
                results.count = list.size();
            } else {
                List<MODEL> newValues = doFiltering(originalItems, prefix);
                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            items.clear();
            items.addAll((List<MODEL>) results.values);
            if (results.count > 0) {
                notifyDataSetChanged();
            }
        }
    }


    protected void onItemClick(MODEL item, int position) {
        if (onItemClickListener != null) onItemClickListener.onItemClick(item, position);
    }

    public OnItemClickListener<MODEL> getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener<MODEL> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    protected void onItemLongClick(MODEL item, int position) {
        if (onItemLongClickListener != null) onItemLongClickListener.onItemLongClick(item, position);
    }

    public OnItemLongClickListener<MODEL> getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<MODEL> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }


    public interface OnItemClickListener<T> {
        void onItemClick(T item, int position);
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(T item, int position);
    }
}