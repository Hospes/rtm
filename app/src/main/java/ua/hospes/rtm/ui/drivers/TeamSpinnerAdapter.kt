package ua.hospes.rtm.ui.drivers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import ua.hospes.rtm.R
import ua.hospes.rtm.core.ui.AbsSpinnerAdapter
import ua.hospes.rtm.domain.team.Team

class TeamSpinnerAdapter(ctx: Context) : AbsSpinnerAdapter<Team, TeamSpinnerAdapter.MyHolder>(ctx) {

    override fun onCreateViewHolder(inflater: LayoutInflater): MyHolder = MyHolder(inflater, R.layout.simple_spinner_item)

    override fun onBindViewHolder(holder: MyHolder, item: Team?, position: Int) {
        holder.bg.text = item?.name ?: "No team"
    }


    inner class MyHolder(inflater: LayoutInflater, layoutId: Int) : ViewHolder(inflater, layoutId) {
        lateinit var bg: TextView

        override fun findViews(itemView: View) {
            bg = itemView.findViewById(R.id.text)
        }
    }
}