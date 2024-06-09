package com.example.sofascore_zavrsni_projekt.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sofascore_zavrsni_projekt.R
import com.example.sofascore_zavrsni_projekt.data.miniSofa_models.Event
import com.example.sofascore_zavrsni_projekt.data.miniSofa_models.Tournament
import com.example.sofascore_zavrsni_projekt.databinding.EventItemBinding
import com.example.sofascore_zavrsni_projekt.databinding.HeaderItemBinding
import com.example.sofascore_zavrsni_projekt.databinding.HeaderItemNoEventsBinding
import com.example.sofascore_zavrsni_projekt.ui.event_details.EventDetailsActivity
import com.example.sofascore_zavrsni_projekt.ui.tournament_details.TournamentDetailsActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EventInfoAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var items = emptyList<EventItem>()

    companion object {
        const val VIEW_TYPE_EVENT_INFO = 0
        const val VIEW_TYPE_HEADER = 1
        const val VIEW_TYPE_NO_EVENT = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_EVENT_INFO -> {
                val context = parent.context
                val layoutInflater = LayoutInflater.from(context)
                val binding = EventItemBinding.inflate(layoutInflater, parent, false)
                EventInfoViewHolder(binding)
            }
            VIEW_TYPE_HEADER -> {
                val context = parent.context
                val layoutInflater = LayoutInflater.from(context)
                val binding = HeaderItemBinding.inflate(layoutInflater, parent, false)
                HeaderItemViewHolder(binding)
            }
            VIEW_TYPE_NO_EVENT -> {
                val context = parent.context
                val layoutInflater = LayoutInflater.from(context)
                val binding = HeaderItemNoEventsBinding.inflate(layoutInflater, parent, false)
                HeaderItemNoEventsViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = items[position]) {
            is EventItem.EventInfoItem -> (holder as EventInfoViewHolder).bind(item)
            is EventItem.HeaderItem -> (holder as HeaderItemViewHolder).bind(item)
            is EventItem.HeaderNoEventsItem -> (holder as HeaderItemNoEventsViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is EventItem.EventInfoItem -> VIEW_TYPE_EVENT_INFO
            is EventItem.HeaderItem -> VIEW_TYPE_HEADER
            is EventItem.HeaderNoEventsItem -> VIEW_TYPE_NO_EVENT
        }
    }

    fun updateItems(newItems: List<EventItem>) {
        val diffResult = DiffUtil.calculateDiff(EventInfoDiffCallback(items, newItems))
        diffResult.dispatchUpdatesTo(this)
        items = newItems
    }


    class EventInfoViewHolder(private val eventItemBinding: EventItemBinding) : ViewHolder(eventItemBinding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bind(eventInfoItem: EventItem.EventInfoItem) {
            val eventInfo = eventInfoItem.eventInfo
            val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
            val outputFormatter = DateTimeFormatter.ofPattern("HH:mm")
            eventItemBinding.run {
                homeTeamName.text = eventInfo.homeTeam.name
                awayTeamName.text = eventInfo.awayTeam.name
                homeTeamIcon.setImageBitmap(eventInfoItem.listOfLogos[0])
                awayTeamIcon.setImageBitmap(eventInfoItem.listOfLogos[1])
                val formattedTime = LocalDateTime.parse(eventInfo.startDate, formatter)
                    .format(outputFormatter)
                eventTime.text = formattedTime
                when (eventInfo.status) {
                    "finished" -> {
                        eventStatus.text = "FT"
                        homeTeamScore.text = eventInfo.homeScore.total.toString()
                        awayTeamScore.text = eventInfo.awayScore.total.toString()
                    }
                    "notstarted" -> {
                        eventStatus.text = "-"
                        homeTeamScore.text = ""
                        awayTeamScore.text = ""
                        homeTeamName.setTextColor(itemView.context.getColor(R.color.on_surface_on_surface_lv_2))
                        awayTeamName.setTextColor(itemView.context.getColor(R.color.on_surface_on_surface_lv_2))
                    }
                    else -> {
                        eventStatus.text = eventInfo.status
                    }
                }
                if (eventInfo.winnerCode == "home") {
                    homeTeamName.setTextColor(itemView.context.getColor(R.color.on_surface_on_surface_lv_1))
                    homeTeamScore.setTextColor(itemView.context.getColor(R.color.on_surface_on_surface_lv_1))
                    awayTeamName.setTextColor(itemView.context.getColor(R.color.on_surface_on_surface_lv_2))
                    awayTeamScore.setTextColor(itemView.context.getColor(R.color.on_surface_on_surface_lv_2))
                } else if (eventInfo.winnerCode == "away") {
                    awayTeamName.setTextColor(itemView.context.getColor(R.color.on_surface_on_surface_lv_1))
                    awayTeamScore.setTextColor(itemView.context.getColor(R.color.on_surface_on_surface_lv_1))
                    homeTeamName.setTextColor(itemView.context.getColor(R.color.on_surface_on_surface_lv_2))
                    homeTeamScore.setTextColor(itemView.context.getColor(R.color.on_surface_on_surface_lv_2))
                }
            }
            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, EventDetailsActivity::class.java).apply {
                    putExtra("eventId", eventInfoItem.eventInfo.id.toString() + " " + eventInfoItem.eventInfo.tournament.sport.slug + " " + eventInfoItem.eventInfo.tournament.id.toString())
                }
                context.startActivity(intent)
                showPressedEffect()
            }
        }
        private fun showPressedEffect() {
            itemView.setBackgroundColor(Color.LightGray.hashCode())
            itemView.elevation = 8f
            itemView.postDelayed({
                itemView.setBackgroundColor(Color.White.hashCode())
                itemView.elevation = 0f
            }, 200)
        }

    }
    class HeaderItemViewHolder(private val binding: HeaderItemBinding) : ViewHolder(binding.root) {
        fun bind(header: EventItem.HeaderItem) {
            val eventInfo = header.tournament
            binding.run {
                tournamentCountry.text = eventInfo.country.name
                tournamentName.text = eventInfo.name
                tournamentIcon.setImageBitmap(header.tournamentLogo)
            }
            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, TournamentDetailsActivity::class.java)
                intent.putExtra("tournamentId", header.tournament.id.toString())
                context.startActivity(intent)
                showPressedEffect()
            }
        }
        private fun showPressedEffect() {
            itemView.setBackgroundColor(Color.LightGray.hashCode())
            itemView.elevation = 8f
            itemView.postDelayed({
                itemView.setBackgroundColor(Color.White.hashCode())
                itemView.elevation = 0f
            }, 200)
        }


    }

    class HeaderItemNoEventsViewHolder(private val binding: HeaderItemNoEventsBinding) : ViewHolder(binding.root) {
        fun bind(header: EventItem.HeaderNoEventsItem) {
            binding.noEventsText.text = header.title
        }
    }

}

class EventInfoDiffCallback(
    private val oldItems: List<EventItem>,
    private val newItems: List<EventItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return if (oldItem is EventItem.HeaderItem && newItem is EventItem.HeaderItem) {
            oldItem.tournament.id == newItem.tournament.id
        } else if (oldItem is EventItem.EventInfoItem && newItem is EventItem.EventInfoItem) {
            oldItem.eventInfo.id == newItem.eventInfo.id
        } else if (oldItem is EventItem.HeaderNoEventsItem && newItem is EventItem.HeaderNoEventsItem) {
            oldItem.title == newItem.title
        }else false

    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem == newItem
    }
}

sealed class EventItem {

    data class EventInfoItem(val eventInfo: Event, val listOfLogos: MutableList<Bitmap?>) : EventItem()

    data class HeaderItem(val tournament: Tournament, val tournamentLogo: Bitmap) : EventItem()

    data class HeaderNoEventsItem(var title: String) : EventItem()


}