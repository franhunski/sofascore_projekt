package com.example.sofascore_zavrsni_projekt.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sofascore_zavrsni_projekt.databinding.StandingsItemLayoutBinding
import com.example.sofascore_zavrsni_projekt.databinding.StandingsHeaderItemLayoutBinding
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sofascore_zavrsni_projekt.data.miniSofa_models.StandingsRow

class TournamentStandingsAdapter:  RecyclerView.Adapter<ViewHolder>() {

    private var items = emptyList<StandingsItem>()

    companion object {
        const val VIEW_TYPE_STANDINGS_INFO = 0
        const val VIEW_TYPE_HEADER = 1
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_STANDINGS_INFO -> {
                val binding = StandingsItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                StandingsInfoViewHolder(binding)
            }
            VIEW_TYPE_HEADER -> {
                val binding = StandingsHeaderItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                StandingsHeaderItemViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = items[position]) {
            is StandingsItem.StandingsInfoItem -> (holder as StandingsInfoViewHolder).bind(item)
            is StandingsItem.StandingsHeaderItem -> (holder as StandingsHeaderItemViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<StandingsItem>) {
        val diffResult = DiffUtil.calculateDiff(StandingsInfoDiffCallback(items, newItems))
        diffResult.dispatchUpdatesTo(this)
        items = newItems
    }

    class StandingsInfoViewHolder(private val binding: StandingsItemLayoutBinding) : ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: StandingsItem.StandingsInfoItem) {
            binding.textPosition.text = item.position.toString()
            binding.textTeam.text = item.standingsInfoItem.team.name
            when (item.sport) {
                "football" -> {
                    binding.run {
                        textPlayedFootball.text = item.standingsInfoItem.played.toString()
                        textWinsFootball.text = item.standingsInfoItem.wins.toString()
                        textDrawsFootball.text = item.standingsInfoItem.draws.toString()
                        textLosesFootball.text = item.standingsInfoItem.loses.toString()
                        textGoalsFootball.text = item.standingsInfoItem.scoresFor.toString() + ":" + item.standingsInfoItem.scoresAgainst.toString()
                        textPointsFootball.text = item.standingsInfoItem.points.toString()
                    }
                }
                "basketball" -> {
                    binding.run {
                        textPlayedBasketball.text = item.standingsInfoItem.played.toString()
                        textWinsBasketball.text = item.standingsInfoItem.wins.toString()
                        textLosesBasketball.text = item.standingsInfoItem.loses.toString()
                        textDiffBasketball.text = (item.standingsInfoItem.scoresFor - item.standingsInfoItem.scoresAgainst).toString()
                        textStrBasketball.text = "0"
                        textGbBasketball.text = "0"
                        textPctBasketball.text = String.format("%.3f", item.standingsInfoItem.percentage)
                    }
                }
                "american-football" -> {
                    binding.run {
                        textPlayedAmFootball.text = item.standingsInfoItem.played.toString()
                        textWinsAmFootball.text = item.standingsInfoItem.wins.toString()
                        textDrawsAmFootball.text = item.standingsInfoItem.draws.toString()
                        textLosesAmFootball.text = item.standingsInfoItem.loses.toString()
                        textPctAmFootball.text = String.format("%.3f", item.standingsInfoItem.percentage)
                    }
                }
            }

        }
    }

    class StandingsHeaderItemViewHolder(private val binding: StandingsHeaderItemLayoutBinding) : ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(header: StandingsItem.StandingsHeaderItem) {
            if (header.sport == "football") {
                binding.run {
                    textPlayed.text = "P"
                    textWins.text = "W"
                    textDraws.text = "D"
                    textLoses.text = "L"
                    textGoals.text = "Goals"
                    textPoints.text = "PTS"
                }
            }
        }
    }


}

class StandingsInfoDiffCallback(
    private val oldItems: List<StandingsItem>,
    private val newItems: List<StandingsItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return if (oldItem is StandingsItem.StandingsInfoItem && newItem is StandingsItem.StandingsInfoItem) {
            oldItem.standingsInfoItem.id == newItem.standingsInfoItem.id
        } else if (oldItem is StandingsItem.StandingsHeaderItem && newItem is StandingsItem.StandingsHeaderItem) {
            oldItem.header == newItem.header
        }else false

    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem == newItem
    }
}

sealed class StandingsItem {

    data class StandingsInfoItem(val standingsInfoItem: StandingsRow, val sport: String, val position: Int) : StandingsItem()

    data class StandingsHeaderItem(val header: String, val sport: String) : StandingsItem()

}