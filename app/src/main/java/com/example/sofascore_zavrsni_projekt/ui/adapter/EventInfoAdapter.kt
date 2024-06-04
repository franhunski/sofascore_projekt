package com.example.sofascore_zavrsni_projekt.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sofascore_zavrsni_projekt.databinding.EventItemBinding
import com.example.sofascore_zavrsni_projekt.databinding.HeaderItemBinding
import com.example.sofascore_zavrsni_projekt.databinding.HeaderItemNoEventsBinding

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

    class EventInfoViewHolder(private val eventItemBinding: EventItemBinding) :
        ViewHolder(eventItemBinding.root) {
        fun bind(eventInfoItem: EventItem.EventInfoItem) {

        }

    }
    class HeaderItemViewHolder(private val binding: HeaderItemBinding) : ViewHolder(binding.root) {
        fun bind(header: EventItem.HeaderItem) {

        }
    }

    class HeaderItemNoEventsViewHolder(private val binding: HeaderItemNoEventsBinding) : ViewHolder(binding.root) {
        fun bind(header: EventItem.HeaderNoEventsItem) {

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
            oldItem.title == newItem.title
        } else if (oldItem is EventItem.EventInfoItem && newItem is EventItem.EventInfoItem) {
            oldItem.weatherInfo.cityName == newItem.weatherInfo.cityName
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

    data class EventInfoItem(val weatherInfo: WeatherInfo) : EventItem()

    data class HeaderItem(val title: String) : EventItem()

    data class HeaderNoEventsItem(val title: String) : EventItem()


}