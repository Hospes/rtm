package ua.hospes.rtm.ui.teams

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.hospes.rtm.R
import ua.hospes.rtm.databinding.ItemTeamBinding
import ua.hospes.rtm.domain.team.Team

class TeamsAdapter : ListAdapter<Team, TeamsAdapter.MyHolder>(DIFF_CALLBACK) {
    var itemClickListener: ((item: Team) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder =
            MyHolder(parent, itemClickListener = { itemClickListener?.invoke(getItem(it)) })

    override fun onBindViewHolder(holder: MyHolder, position: Int) = holder.bind(getItem(position), position)


    inner class MyHolder(parent: ViewGroup, itemClickListener: (position: Int) -> Unit)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_team, parent, false)) {

        private val binding = ItemTeamBinding.bind(itemView)

        init {
            itemView.setOnClickListener {
                if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                itemClickListener.invoke(bindingAdapterPosition)
            }
        }

        fun bind(item: Team, position: Int) = with(itemView) {
            val bg1 = TypedValue()
            context.theme.resolveAttribute(R.attr.listItemBackground1, bg1, false)
            val bg2 = TypedValue()
            context.theme.resolveAttribute(R.attr.listItemBackground2, bg2, false)

            setBackgroundResource(if (position % 2 == 0) bg2.data else bg1.data)

            binding.name.text = item.name
            binding.drivers.text = item.drivers.map { it.name }.toString().replace("[\\[\\]]".toRegex(), "")
        }
    }
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Team>() {
    override fun areItemsTheSame(old: Team, new: Team): Boolean = old.id == new.id

    override fun areContentsTheSame(old: Team, new: Team): Boolean = old == new
}