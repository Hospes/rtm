package ua.hospes.rtm.ui.race

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.hospes.rtm.R
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.widgets.CustomToggleButton
import ua.hospes.rtm.widgets.DriverTimeView
import ua.hospes.rtm.widgets.SessionTimeView
//import ua.hospes.undobutton.UndoButton
//import ua.hospes.undobutton.UndoButtonController
import java.util.*

class RaceAdapter(
    context: Context,
    private val sessionButtonType: String/*,
                           private val undoButtonController: UndoButtonController<*>? = null*/
) : ListAdapter<RaceItem, RaceAdapter.MyHolder>(DIFF_CALLBACK) {
    @ColorInt private val carDefaultColor: Int
    @ColorInt private val sessionTrackColor: Int
    @ColorInt private val sessionPitColor: Int
    var itemClickListener: ((item: RaceItem) -> Unit)? = null
    var setCarClickListener: ((item: RaceItem) -> Unit)? = null
    var setDriverClickListener: ((item: RaceItem) -> Unit)? = null
    var onPitClickListener: ((item: RaceItem) -> Unit)? = null
    var onOutClickListener: ((item: RaceItem) -> Unit)? = null
    var onUndoClickListener: ((item: RaceItem) -> Unit)? = null


    init {
        this.carDefaultColor = getButtonTextAppearance(context)
        this.sessionTrackColor = getSessionTrackColor(context)
        this.sessionPitColor = getSessionPitColor(context)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder = when (sessionButtonType) {
        "next" -> MyHolderNext(parent,
            itemClickListener = { itemClickListener?.invoke(getItem(it)) },
            carClickListener = { setCarClickListener?.invoke(getItem(it)) },
            driverClickListener = { setDriverClickListener?.invoke(getItem(it)) },
            nextClickListener = { onOutClickListener?.invoke(getItem(it)) })
        //        "undo" -> MyHolderUndoNext(parent,
        //                itemClickListener = { itemClickListener?.invoke(getItem(it)) },
        //                carClickListener = { setCarClickListener?.invoke(getItem(it)) },
        //                driverClickListener = { setDriverClickListener?.invoke(getItem(it)) },
        //                nextClickListener = { onOutClickListener?.invoke(getItem(it)) },
        //                undoClickListener = { onUndoClickListener?.invoke(getItem(it)) })
        else -> MyHolderPit(parent,
            itemClickListener = { itemClickListener?.invoke(getItem(it)) },
            carClickListener = { setCarClickListener?.invoke(getItem(it)) },
            driverClickListener = { setDriverClickListener?.invoke(getItem(it)) },
            pitClickListener = { onPitClickListener?.invoke(getItem(it)) },
            outClickListener = { onOutClickListener?.invoke(getItem(it)) })
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = getItem(position)
        val context = holder.itemView.context

        val bg1 = TypedValue()
        context.theme.resolveAttribute(R.attr.listItemBackground1, bg1, false)
        val bg2 = TypedValue()
        context.theme.resolveAttribute(R.attr.listItemBackground2, bg2, false)

        holder.itemView.setBackgroundResource(if (position % 2 == 0) bg1.data else bg2.data)


        holder.team.text = String.format(Locale.getDefault(), "%1\$d - %2\$s", item.teamNumber, item.team.name)
        holder.pits.text = context.resources.getString(R.string.race_pit, item.details?.pitStops ?: 0)

        val session = item.session
        holder.driverTimeView.session = session
        holder.sessionTimeView.session = session

        //undoButtonController?.let { holder.undoBind(it, item.id) }

        if (session != null) {
            val car = session.car
            holder.btnSessionCar.text = car?.number?.toString() ?: context.getString(R.string.race_btn_set_car)
            holder.btnSessionCar.setTextColor(if (car == null) carDefaultColor else context.resources.getColor(car.quality.color))

            val driver = session.driver
            holder.btnSessionDriver.text = if (driver == null) context.getString(R.string.race_btn_set_driver) else session.driver.name
            holder.driverTimeView.visibility = if (driver == null) View.INVISIBLE else View.VISIBLE
            if (driver != null) {
                holder.driverTimeView.prevDuration = item.details?.getDriverDuration(driver.id) ?: 0L
            }

            holder.sessionType.text = when (session.type) {
                Session.Type.TRACK -> "ON TRACK"
                Session.Type.PIT -> "PIT-STOP"
            }
            holder.sessionType.visibility = View.VISIBLE

            when (session.type) {
                Session.Type.TRACK -> {
                    holder.sessionType.setTextColor(sessionTrackColor)
                    holder.sessionTimeView.setTextColor(sessionTrackColor)
                    holder.setPitChecked(false)
                }

                Session.Type.PIT -> {
                    holder.sessionType.setTextColor(sessionPitColor)
                    holder.sessionTimeView.setTextColor(sessionPitColor)
                    holder.setPitChecked(true)
                }
            }
        }
    }


    abstract inner class MyHolder(
        parent: ViewGroup, @LayoutRes layoutId: Int,
        itemClickListener: (position: Int) -> Unit,
        carClickListener: (position: Int) -> Unit,
        driverClickListener: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)) {
        val btnSessionCar: Button = itemView.findViewById(R.id.btn_session_car)
        val btnSessionDriver: Button = itemView.findViewById(R.id.btn_session_driver)

        val driverTimeView: DriverTimeView = itemView.findViewById(R.id.tv_driver_all_time)

        val team: TextView = itemView.findViewById(R.id.tv_team)
        val sessionType: TextView = itemView.findViewById(R.id.tv_session_type)
        val pits: TextView = itemView.findViewById(R.id.tv_pits)
        val sessionTimeView: SessionTimeView = itemView.findViewById(R.id.tv_session_duration)


        init {
            itemView.setOnClickListener {
                if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                itemClickListener.invoke(bindingAdapterPosition)
            }

            btnSessionCar.setOnClickListener {
                if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                carClickListener.invoke(bindingAdapterPosition)
            }

            btnSessionDriver.setOnClickListener {
                if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                driverClickListener.invoke(bindingAdapterPosition)
            }
        }


        internal open fun setPitChecked(checked: Boolean) {}

        //internal open fun undoBind(controller: UndoButtonController<*>, id: Long) {}
    }

    inner class MyHolderNext(
        parent: ViewGroup,
        itemClickListener: (position: Int) -> Unit,
        carClickListener: (position: Int) -> Unit,
        driverClickListener: (position: Int) -> Unit,
        nextClickListener: (position: Int) -> Unit
    ) : MyHolder(parent, R.layout.item_race_next, itemClickListener, carClickListener, driverClickListener) {
        init {
            itemView.findViewById<Button>(R.id.btn_next).setOnClickListener {
                if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                nextClickListener.invoke(bindingAdapterPosition)
            }
        }
    }

    internal inner class MyHolderPit(
        parent: ViewGroup,
        itemClickListener: (position: Int) -> Unit,
        carClickListener: (position: Int) -> Unit,
        driverClickListener: (position: Int) -> Unit,
        pitClickListener: (position: Int) -> Unit,
        outClickListener: (position: Int) -> Unit
    ) : MyHolder(parent, R.layout.item_race_pit, itemClickListener, carClickListener, driverClickListener) {
        private val btnPitOut: CustomToggleButton = itemView.findViewById(R.id.btn_pit_out)

        init {
            btnPitOut.setOnClickListener {
                if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                when ((it as CustomToggleButton).isChecked) {
                    true -> pitClickListener.invoke(bindingAdapterPosition)
                    false -> outClickListener.invoke(bindingAdapterPosition)
                }
            }
        }

        override fun setPitChecked(checked: Boolean) {
            btnPitOut.isChecked = checked
        }
    }

    inner class MyHolderUndoNext(
        parent: ViewGroup,
        itemClickListener: (position: Int) -> Unit,
        carClickListener: (position: Int) -> Unit,
        driverClickListener: (position: Int) -> Unit,
        nextClickListener: (position: Int) -> Unit,
        undoClickListener: (position: Int) -> Unit
    ) : MyHolder(parent, R.layout.item_race_undo_next, itemClickListener, carClickListener, driverClickListener) {
        //val btnNextSession: UndoButton = itemView.findViewById(R.id.btn_next)

        init {
            //            btnNextSession.setOnClickListener {
            //                if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
            //                nextClickListener.invoke(bindingAdapterPosition)
            //            }
            //            btnNextSession.setOnUndoClickListener {
            //                if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@setOnUndoClickListener
            //                undoClickListener.invoke(bindingAdapterPosition)
            //            }
            //            btnNextSession.setController(undoButtonController)
        }

        //override fun undoBind(controller: UndoButtonController<*>, id: Long) = undoButtonController?.onBind(id.toInt(), btnNextSession) ?: Unit
    }


    @ColorInt
    private fun getButtonTextAppearance(context: Context): Int {
        var ta: TypedArray? = null
        try {
            ta = context.obtainStyledAttributes(intArrayOf(android.R.attr.textColorPrimary))
            return ta.getColor(0, Color.WHITE)
        } finally {
            ta?.recycle()
        }
    }

    @ColorInt
    private fun getSessionTrackColor(context: Context): Int {
        var ta: TypedArray? = null
        try {
            ta = context.obtainStyledAttributes(intArrayOf(R.attr.timeView_OnTrack))
            return ta.getColor(0, Color.GREEN)
        } finally {
            ta?.recycle()
        }
    }

    @ColorInt
    private fun getSessionPitColor(context: Context): Int {
        var ta: TypedArray? = null
        try {
            ta = context.obtainStyledAttributes(intArrayOf(R.attr.timeView_OnPit))
            return ta.getColor(0, Color.RED)
        } finally {
            ta?.recycle()
        }
    }
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RaceItem>() {
    override fun areItemsTheSame(old: RaceItem, new: RaceItem): Boolean = old.id == new.id

    override fun areContentsTheSame(old: RaceItem, new: RaceItem): Boolean = old == new
}