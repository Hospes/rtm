package ua.hospes.rtm.core.ui

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.ThemedSpinnerAdapter

import java.util.ArrayList
import java.util.Collections

/**
 * @author Andrew Khloponin
 */
abstract class AbsSpinnerAdapter<T, VH : AbsSpinnerAdapter.ViewHolder> : BaseAdapter, ThemedSpinnerAdapter {
    private val mLock = Any()
    val resources: Resources
    private val mInflater: LayoutInflater
    private val objects: MutableList<T>
    private var mNotifyOnChange = true


    constructor(mContext: Context) {
        this.resources = mContext.resources
        this.mInflater = LayoutInflater.from(mContext)
        this.objects = ArrayList()
    }

    constructor(mContext: Context, objects: List<T>?) {
        this.resources = mContext.resources
        this.mInflater = LayoutInflater.from(mContext)
        this.objects = objects ?: ArrayList()
    }

    constructor(mContext: Context, vararg objects: T) {
        this.resources = mContext.resources
        this.mInflater = LayoutInflater.from(mContext)
        this.objects = ArrayList()
        Collections.addAll(this.objects, *objects)
    }


    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     */
    fun add(`object`: T?) {
        synchronized(mLock) {
            objects.add(`object`)
        }
        if (mNotifyOnChange) notifyDataSetChanged()
    }

    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
     * is not supported by this list
     * @throws ClassCastException            if the class of an element of the specified
     * collection prevents it from being added to this list
     * @throws NullPointerException          if the specified collection contains one
     * or more null elements and this list does not permit null
     * elements, or if the specified collection is null
     * @throws IllegalArgumentException      if some property of an element of the
     * specified collection prevents it from being added to this list
     */
    fun addAll(collection: Collection<T>) {
        synchronized(mLock) {
            objects.addAll(collection)
        }
        if (mNotifyOnChange) notifyDataSetChanged()
    }

    /**
     * Adds the specified items at the end of the array.
     *
     * @param items The items to add at the end of the array.
     */
    fun addAll(vararg items: T) {
        synchronized(mLock) {
            Collections.addAll(objects, *items)
        }
        if (mNotifyOnChange) notifyDataSetChanged()
    }

    /**
     * Inserts the specified object at the specified index in the array.
     *
     * @param object The object to insert into the array.
     * @param index  The index at which the object must be inserted.
     */
    fun insert(`object`: T?, index: Int) {
        synchronized(mLock) {
            objects.add(index, `object`)
        }
        if (mNotifyOnChange) notifyDataSetChanged()
    }

    /**
     * Removes the specified object from the array.
     *
     * @param object The object to remove.
     */
    fun remove(`object`: T?) {
        synchronized(mLock) {
            objects.remove(`object`)
        }
        if (mNotifyOnChange) notifyDataSetChanged()
    }

    /**
     * Remove all elements from the list.
     */
    fun clear() {
        synchronized(mLock) {
            objects.clear()
        }
        if (mNotifyOnChange) notifyDataSetChanged()
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
        mNotifyOnChange = true
    }

    fun setNotifyOnChange(notifyOnChange: Boolean) {
        mNotifyOnChange = notifyOnChange
    }


    override fun getCount(): Int {
        return objects.size
    }

    override fun getItem(position: Int): T {
        return objects[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    protected abstract fun onCreateViewHolder(inflater: LayoutInflater): VH

    protected abstract fun onBindViewHolder(holder: VH, item: T, position: Int)


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: VH
        if (convertView == null) {
            holder = onCreateViewHolder(mInflater)
            convertView = holder.itemView
            convertView!!.tag = holder
        } else {
            holder = convertView.tag as VH
        }
        onBindViewHolder(holder, getItem(position), position)
        return convertView
    }

    override fun getDropDownView(position: Int, convertView: View, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    override fun setDropDownViewTheme(theme: Resources.Theme?) {}

    override fun getDropDownViewTheme(): Resources.Theme? {
        return null
    }

    abstract inner class ViewHolder private constructor(internal var itemView: View) {


        init {
            findViews(itemView)
        }

        constructor(inflater: LayoutInflater, @LayoutRes layoutId: Int) : this(inflater.inflate(layoutId, null, false)) {}


        protected abstract fun findViews(itemView: View)

        fun <V> findView(root: View?, @IdRes id: Int): V? {
            if (root == null) return null
            try {
                return root.findViewById<View>(id) as V
            } catch (e: ClassCastException) {
                return null
            }

        }

        fun <V> findView(@IdRes id: Int): V? {
            return findView<V>(itemView, id)
        }
    }
}