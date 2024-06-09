package com.example.sofascore_zavrsni_projekt.ui.adapter
import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sofascore_zavrsni_projekt.data.miniSofa_models.Event
import com.example.sofascore_zavrsni_projekt.databinding.TournamentMatchesHeaderItemBinding
import com.example.sofascore_zavrsni_projekt.databinding.EventItemBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TournamentMatchesAdapter : PagingDataAdapter<MatchesItem, ViewHolder>(MatchesComparator) {

    companion object {
        const val VIEW_TYPE_MATCHES_INFO = 0
        const val VIEW_TYPE_HEADER_INFO = 1
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is MatchesItem.MatchInfoItem -> (holder as MatchesItemViewHolder).bind(item)
            is MatchesItem.HeaderInfoItem -> (holder as HeaderItemViewHolder).bind(item)
            null -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_MATCHES_INFO -> {
                val context = parent.context
                val layoutInflater = LayoutInflater.from(context)
                val binding = EventItemBinding.inflate(layoutInflater, parent, false)
                MatchesItemViewHolder(binding)
            }
            VIEW_TYPE_HEADER_INFO -> {
                val context = parent.context
                val layoutInflater = LayoutInflater.from(context)
                val binding = TournamentMatchesHeaderItemBinding.inflate(layoutInflater, parent, false)
                HeaderItemViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MatchesItem.MatchInfoItem -> VIEW_TYPE_MATCHES_INFO
            is MatchesItem.HeaderInfoItem -> VIEW_TYPE_HEADER_INFO
            null -> throw IllegalArgumentException("Invalid view type")
        }
    }


    class MatchesItemViewHolder(private val binding: EventItemBinding) : ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: MatchesItem.MatchInfoItem) {
            binding.homeTeamName.text = item.matchInfo.homeTeam.name
            binding.awayTeamName.text = item.matchInfo.awayTeam.name
            binding.homeTeamScore.text = item.matchInfo.homeScore.total.toString()
            binding.awayTeamScore.text = item.matchInfo.awayScore.total.toString()
            val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
            val outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yy")
            val formattedTime = LocalDateTime.parse(item.matchInfo.startDate, formatter)
                .format(outputFormatter)
            binding.eventTime.text = formattedTime
            if (item.matchInfo.status == "notstarted") {
                binding.eventStatus.text = "-"
                binding.homeTeamScore.visibility = View.GONE
                binding.awayTeamScore.visibility = View.GONE
            } else if (item.matchInfo.status == "finished") {
                binding.eventStatus.text = "FT"
            }
        }
    }

    class HeaderItemViewHolder(private val binding: TournamentMatchesHeaderItemBinding) : ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: MatchesItem.HeaderInfoItem) {
            binding.textRound.text = "Round " + item.header
        }
    }

    object MatchesComparator : DiffUtil.ItemCallback<MatchesItem>() {
        override fun areItemsTheSame(oldItem: MatchesItem, newItem: MatchesItem): Boolean {
            return if (oldItem is MatchesItem.MatchInfoItem && newItem is MatchesItem.MatchInfoItem) {
                oldItem.matchInfo.id == newItem.matchInfo.id
            } else if (oldItem is MatchesItem.HeaderInfoItem && newItem is MatchesItem.HeaderInfoItem) {
                oldItem.header == newItem.header
            } else false
        }

        override fun areContentsTheSame(oldItem: MatchesItem, newItem: MatchesItem): Boolean {
            return oldItem == newItem
        }
    }
}

sealed class MatchesItem {

    data class MatchInfoItem(val matchInfo: Event) : MatchesItem()

    data class HeaderInfoItem(val header: String) : MatchesItem()


}