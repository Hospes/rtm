package ua.hospes.rtm.ui.race

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton

import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView

import java.util.Locale

import ua.hospes.absrvadapter.AbsRecyclerAdapter
import ua.hospes.absrvadapter.AbsRecyclerHolder
import ua.hospes.absrvadapter.OnItemClickListener
import ua.hospes.rtm.R
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.utils.UiUtils
import ua.hospes.rtm.widgets.DriverTimeView
import ua.hospes.rtm.widgets.SessionTimeView
import ua.hospes.undobutton.UndoButton
import ua.hospes.undobutton.UndoButtonController

/**
 * @author Andrew Khloponin
 */
internal class RaceAdapter(context: Context, private val sessionButtonType: String, private val undoButtonController: UndoButtonController<*>) : AbsRecyclerAdapter<RaceItem, RaceAdapter.MyHolder>() {
    @ColorInt
    private val carDefaultColor: Int
    @ColorInt
    private val sessionTrackColor: Int
    @ColorInt
    private val sessionPitColor: Int
    private var onSetCarClickListener: OnItemClickListener<RaceItem>? = null
    private var onSetDriverClickListener: OnItemClickListener<RaceItem>? = null
    private var onPitClickListener: OnItemClickListener<RaceItem>? = null
    private var onOutClickListener: OnItemClickListener<RaceItem>? = null
    private var onUndoClickListener: OnItemClickListener<RaceItem>? = null


    init {
        this.carDefaultColor = getButtonTextAppearance(context)
        this.sessionTrackColor = getSessionTrackColor(context)
        this.sessionPitColor = getSessionPitColor(context)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        when (sessionButtonType) {
            "next" -> return MyHolderNext(parent)

            "undo" -> return MyHolderUndoNext(parent)

            "pit" -> return MyHolderPit(parent)
            else -> return MyHolderPit(parent)
        }
    }

    override fun onBindViewHolder(holder: MyHolder, item: RaceItem, position: Int) {
        val context = holder.itemView.context

        val bg1 = TypedValue()
        context.theme.resolveAttribute(R.attr.listItemBackground1, bg1, false)
        val bg2 = TypedValue()
        context.theme.resolveAttribute(R.attr.listItemBackground2, bg2, false)

        holder.itemView.setBackgroundResource(if (position % 2 == 0) bg1.data else bg2.data)


        holder.team.text = String.format(Locale.getDefault(), "%1\$d - %2\$s", item.teamNumber, item.team.name)
        holder.pits.text = context.resources.getString(R.string.race_pit, item.details.pitStops)

        val session = item.session
        holder.driverTimeView.session = session
        holder.sessionTimeView.session = session

        holder.undoBind(undoButtonController, item.id)

        if (session != null) {
            val car = session.car
            holder.btnSessionCar.text = car?.number?.toString() ?: context.getString(R.string.race_btn_set_car)
            holder.btnSessionCar.setTextColor(if (car == null) carDefaultColor else context.resources.getColor(car.quality.color))

            val driver = session.driver
            holder.btnSessionDriver.text = if (driver == null) context.getString(R.string.race_btn_set_driver) else session.driver!!.name
            holder.driverTimeView.visibility = if (driver == null) View.INVISIBLE else View.VISIBLE
            if (driver != null) {
                holder.driverTimeView.prevDuration = item.details.getDriverDuration(driver.id!!)
            }

            holder.sessionType.text = session.type.title
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


    fun setOnSetCarClickListener(onSetCarClickListener: OnItemClickListener<RaceItem>) {
        this.onSetCarClickListener = onSetCarClickListener
    }

    fun setOnSetDriverClickListener(onSetDriverClickListener: OnItemClickListener<RaceItem>) {
        this.onSetDriverClickListener = onSetDriverClickListener
    }

    fun setOnPitClickListener(onPitClickListener: OnItemClickListener<RaceItem>) {
        this.onPitClickListener = onPitClickListener
    }

    fun setOnOutClickListener(onOutClickListener: OnItemClickListener<RaceItem>) {
        this.onOutClickListener = onOutClickListener
    }

    fun setOnUndoClickListener(onUndoClickListener: OnItemClickListener<RaceItem>) {
        this.onUndoClickListener = onUndoClickListener
    }


    internal abstract inner class MyHolder(parent: ViewGroup, layoutId: Int) : AbsRecyclerHolder(parent, layoutId) {
        var btnSessionCar: Button

        var driverTimeView: DriverTimeView
        var btnSessionDriver: Button

        var team: TextView
        var sessionType: TextView
        var pits: TextView
        var sessionTimeView: SessionTimeView


        init {

            itemView.setOnClickListener(OnClickListener { this.initOnItemClickListener(it) })

            btnSessionCar.setOnClickListener(OnClickListener { this.initOnSetCarClickListener(it) })
            btnSessionDriver.setOnClickListener(OnClickListener { this.initOnSetDriverClickListener(it) })
        }


        override fun findViews(itemView: View) {
            btnSessionCar = UiUtils.findView(itemView, R.id.btn_session_car)

            btnSessionDriver = UiUtils.findView(itemView, R.id.btn_session_driver)
            driverTimeView = UiUtils.findView(itemView, R.id.tv_driver_all_time)

            team = UiUtils.findView(itemView, R.id.tv_team)
            sessionTimeView = UiUtils.findView(itemView, R.id.tv_session_duration)
            sessionType = UiUtils.findView(itemView, R.id.tv_session_type)

            pits = UiUtils.findView(itemView, R.id.tv_pits)
        }

        fun initOnItemClickListener(view: View) {
            val position = adapterPosition
            if (position == RecyclerView.NO_POSITION) return

            if (onItemClickListener == null) return
            onItemClick(getItem(position), position)
        }

        fun initOnSetCarClickListener(view: View) {
            val position = adapterPosition
            if (position == RecyclerView.NO_POSITION) return

            if (onSetCarClickListener == null) return
            onSetCarClickListener!!.onItemClick(getItem(position), position)
        }

        fun initOnSetDriverClickListener(view: View) {
            val position = adapterPosition
            if (position == RecyclerView.NO_POSITION) return

            if (onSetDriverClickListener == null) return
            onSetDriverClickListener!!.onItemClick(getItem(position), position)
        }

        fun initOnOutClickListener(view: View) {
            val position = adapterPosition
            if (position == RecyclerView.NO_POSITION) return

            if (onOutClickListener == null) return
            onOutClickListener!!.onItemClick(getItem(position), position)
        }

        internal open fun setPitChecked(checked: Boolean) {}

        internal open fun undoBind(controller: UndoButtonController<*>, id: Int) {}
    }

    internal inner class MyHolderNext(parent: ViewGroup) : MyHolder(parent, R.layout.item_race_next) {
        var btnNextSession: Button

        init {

            btnNextSession.setOnClickListener(OnClickListener { this.initOnOutClickListener(it) })
        }

        override fun findViews(itemView: View) {
            super.findViews(itemView)
            btnNextSession = UiUtils.findView(itemView, R.id.btn_next)
        }
    }

    internal inner class MyHolderPit(parent: ViewGroup) : MyHolder(parent, R.layout.item_race_pit) {
        var btnPitOut: ToggleButton

        init {

            btnPitOut.setOnClickListener(OnClickListener { this.initOnPitClickListener(it) })
        }

        override fun findViews(itemView: View) {
            super.findViews(itemView)
            btnPitOut = UiUtils.findView(itemView, R.id.btn_pit_out)
        }

        fun initOnPitClickListener(view: View) {
            val position = adapterPosition
            if (position == RecyclerView.NO_POSITION) return

            if (btnPitOut.isChecked) {
                if (onPitClickListener != null) onPitClickListener!!.onItemClick(getItem(position), position)
            } else {
                if (onOutClickListener != null) onOutClickListener!!.onItemClick(getItem(position), position)
            }
        }

        override fun setPitChecked(checked: Boolean) {
            btnPitOut.isChecked = checked
        }
    }

    internal inner class MyHolderUndoNext(parent: ViewGroup) : MyHolder(parent, R.layout.item_race_undo_next) {
        var btnNextSession: UndoButton

        init {

            btnNextSession.setOnClickListener(OnClickListener { this.initOnOutClickListener(it) })
            btnNextSession.setOnUndoClickListener { this.initOnUndoClickListener(it) }
            btnNextSession.setController(undoButtonController)
        }

        override fun findViews(itemView: View) {
            super.findViews(itemView)
            btnNextSession = UiUtils.findView(itemView, R.id.btn_next)
        }

        fun initOnUndoClickListener(view: View) {
            val position = adapterPosition
            if (position == RecyclerView.NO_POSITION) return

            if (onUndoClickListener == null) return
            onUndoClickListener!!.onItemClick(getItem(position), position)
        }

        override fun undoBind(controller: UndoButtonController<*>, id: Int) {
            undoButtonController.onBind(id, btnNextSession)
        }
    }


    @ColorInt
    private fun getButtonTextAppearance(context: Context): Int {
        var ta: TypedArray? = null
        try {
            val attrs = intArrayOf(android.R.attr.textColorPrimary)
            ta = context.obtainStyledAttributes(attrs)
            return ta!!.getColor(0, Color.WHITE)
        } finally {
            ta?.recycle()
        }
    }

    @ColorInt
    private fun getSessionTrackColor(context: Context): Int {
        var ta: TypedArray? = null
        try {
            val attrs = intArrayOf(R.attr.timeView_OnTrack)
            ta = context.obtainStyledAttributes(attrs)
            return ta!!.getColor(0, Color.GREEN)
        } finally {
            ta?.recycle()
        }
    }

    @ColorInt
    private fun getSessionPitColor(context: Context): Int {
        var ta: TypedArray? = null
        try {
            val attrs = intArrayOf(R.attr.timeView_OnPit)
            ta = context.obtainStyledAttributes(attrs)
            return ta!!.getColor(0, Color.RED)
        } finally {
            ta?.recycle()
        }
    }
}