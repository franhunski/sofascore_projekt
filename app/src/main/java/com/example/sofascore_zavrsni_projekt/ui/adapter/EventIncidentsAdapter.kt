package com.example.sofascore_zavrsni_projekt.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sofascore_zavrsni_projekt.R
import com.example.sofascore_zavrsni_projekt.data.miniSofa_models.Incident
import com.example.sofascore_zavrsni_projekt.databinding.HeaderItemNoIncidentsBinding
import com.example.sofascore_zavrsni_projekt.databinding.PeriodIncidentLayoutBinding
import com.example.sofascore_zavrsni_projekt.databinding.CardIncidentLayoutBinding
import com.example.sofascore_zavrsni_projekt.databinding.GoalIncidentLayoutBinding
import com.example.sofascore_zavrsni_projekt.ui.tournament_details.TournamentDetailsActivity

class EventIncidentsAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var items = emptyList<IncidentItem>()

    companion object {
        const val VIEW_TYPE_INCIDENT_CARD_INFO = 0
        const val VIEW_TYPE_INCIDENT_GOAL_INFO = 1
        const val VIEW_TYPE_INCIDENT_PERIOD_INFO = 2
        const val VIEW_TYPE_NO_INCIDENTS = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_INCIDENT_CARD_INFO -> {
                val context = parent.context
                val layoutInflater = LayoutInflater.from(context)
                val binding = CardIncidentLayoutBinding.inflate(layoutInflater, parent, false)
                CardIncidentInfoViewHolder(binding)
            }
            VIEW_TYPE_INCIDENT_GOAL_INFO -> {
                val context = parent.context
                val layoutInflater = LayoutInflater.from(context)
                val binding = GoalIncidentLayoutBinding.inflate(layoutInflater, parent, false)
                GoalIncidentItemViewHolder(binding)
            }
            VIEW_TYPE_INCIDENT_PERIOD_INFO -> {
                val context = parent.context
                val layoutInflater = LayoutInflater.from(context)
                val binding = PeriodIncidentLayoutBinding.inflate(layoutInflater, parent, false)
                PeriodIncidentItemViewHolder(binding)
            }
            VIEW_TYPE_NO_INCIDENTS -> {
                val context = parent.context
                val layoutInflater = LayoutInflater.from(context)
                val binding = HeaderItemNoIncidentsBinding.inflate(layoutInflater, parent, false)
                HeaderItemNoIncidentsViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = items[position]) {
            is IncidentItem.CardIncidentItem -> (holder as CardIncidentInfoViewHolder).bind(item)
            is IncidentItem.GoalIncidentItem -> (holder as GoalIncidentItemViewHolder).bind(item)
            is IncidentItem.PeriodIncidentItem -> (holder as PeriodIncidentItemViewHolder).bind(item)
            is IncidentItem.HeaderNoIncidentsItem -> (holder as HeaderItemNoIncidentsViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is IncidentItem.CardIncidentItem -> VIEW_TYPE_INCIDENT_CARD_INFO
            is IncidentItem.GoalIncidentItem -> VIEW_TYPE_INCIDENT_GOAL_INFO
            is IncidentItem.PeriodIncidentItem -> VIEW_TYPE_INCIDENT_PERIOD_INFO
            is IncidentItem.HeaderNoIncidentsItem -> VIEW_TYPE_NO_INCIDENTS
        }
    }

    fun updateItems(newItems: List<IncidentItem>) {
        val diffResult = DiffUtil.calculateDiff(IncidentsInfoDiffCallback(items, newItems))
        diffResult.dispatchUpdatesTo(this)
        items = newItems
    }

    class CardIncidentInfoViewHolder(private val binding: CardIncidentLayoutBinding) : ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(incidentInfoItem: IncidentItem.CardIncidentItem) {
            // Reset visibility of all elements to avoid leftover states
            binding.incidentImageHome.visibility = View.VISIBLE
            binding.incidentMinuteHome.visibility = View.VISIBLE
            binding.incidentPlayerHome.visibility = View.VISIBLE
            binding.incidentDescriptionHome.visibility = View.VISIBLE
            binding.verticalLineHome.visibility = View.VISIBLE

            binding.incidentImageAway.visibility = View.VISIBLE
            binding.incidentMinuteAway.visibility = View.VISIBLE
            binding.incidentPlayerAway.visibility = View.VISIBLE
            binding.incidentDescriptionAway.visibility = View.VISIBLE
            binding.verticalLineAway.visibility = View.VISIBLE

            val cardColor = incidentInfoItem.incidentInfo.color

            if (incidentInfoItem.incidentInfo.teamSide == "home") {
                when (cardColor) {
                    "yellow" -> binding.incidentImageHome.setImageResource(R.mipmap.yellow_card_icon)
                    "yellowred" -> binding.incidentImageHome.setImageResource(R.mipmap.red_card_icon)
                    "red" -> binding.incidentImageHome.setImageResource(R.mipmap.red_card_icon)
                }
                binding.incidentMinuteHome.text = "${incidentInfoItem.incidentInfo.time}'"
                binding.incidentPlayerHome.text = incidentInfoItem.incidentInfo.player.name
                binding.incidentDescriptionHome.text = "Foul"

                // Hide away elements
                binding.incidentImageAway.visibility = View.GONE
                binding.incidentMinuteAway.visibility = View.GONE
                binding.incidentPlayerAway.visibility = View.GONE
                binding.incidentDescriptionAway.visibility = View.GONE
                binding.verticalLineAway.visibility = View.GONE
            } else if (incidentInfoItem.incidentInfo.teamSide == "away") {
                when (cardColor) {
                    "yellow" -> binding.incidentImageAway.setImageResource(R.mipmap.yellow_card_icon)
                    "yellowred" -> binding.incidentImageAway.setImageResource(R.mipmap.red_card_icon)
                    "red" -> binding.incidentImageAway.setImageResource(R.mipmap.red_card_icon)
                }
                binding.incidentMinuteAway.text = "${incidentInfoItem.incidentInfo.time}'"
                binding.incidentPlayerAway.text = incidentInfoItem.incidentInfo.player.name
                binding.incidentDescriptionAway.text = "Foul"

                // Hide home elements
                binding.incidentImageHome.visibility = View.GONE
                binding.incidentMinuteHome.visibility = View.GONE
                binding.incidentPlayerHome.visibility = View.GONE
                binding.incidentDescriptionHome.visibility = View.GONE
                binding.verticalLineHome.visibility = View.GONE
            }
        }
    }
    class PeriodIncidentItemViewHolder(private val binding: PeriodIncidentLayoutBinding) : ViewHolder(binding.root) {
        fun bind(incidentInfoItem: IncidentItem.PeriodIncidentItem) {
            if (incidentInfoItem.status == "inprogress") {
                binding.textViewTournamentLive.text = incidentInfoItem.incidentInfo.text
            } else {
                binding.textViewTournament.text = incidentInfoItem.incidentInfo.text
            }
        }

    }

    class GoalIncidentItemViewHolder(private val binding: GoalIncidentLayoutBinding) : ViewHolder(binding.root) {
        fun bind(incidentInfoItem: IncidentItem.GoalIncidentItem) {
            binding.incidentImageHome.visibility = View.GONE
            binding.incidentMinuteHome.visibility = View.GONE
            binding.verticalLineHome.visibility = View.GONE
            binding.scoreHomeSideRight.visibility = View.GONE
            binding.scoreHomeSideLine.visibility = View.GONE
            binding.scoreHomeSideLeft.visibility = View.GONE
            binding.incidentGoalPlayerHome.visibility = View.GONE

            binding.incidentImageAway.visibility = View.GONE
            binding.incidentMinuteAway.visibility = View.GONE
            binding.verticalLineAway.visibility = View.GONE
            binding.scoreAwaySideRight.visibility = View.GONE
            binding.scoreAwaySideLine.visibility = View.GONE
            binding.scoreAwaySideLeft.visibility = View.GONE
            binding.incidentGoalPlayerAway.visibility = View.GONE
            if (incidentInfoItem.sport == "basketball") {
                binding.basketballTime.text = incidentInfoItem.incidentInfo.time.toString()
            } else{
                when (incidentInfoItem.incidentInfo.scoringTeam) {
                    "home" -> {
                        binding.incidentMinuteHome.visibility = View.VISIBLE
                        binding.incidentMinuteHome.text = incidentInfoItem.incidentInfo.time.toString()
                        binding.incidentGoalPlayerHome.visibility = View.VISIBLE
                        binding.incidentGoalPlayerHome.text = incidentInfoItem.incidentInfo.player.name
                    }
                    "away" -> {
                        binding.incidentMinuteAway.visibility = View.VISIBLE
                        binding.incidentMinuteAway.text = incidentInfoItem.incidentInfo.time.toString()
                        binding.incidentGoalPlayerAway.visibility = View.VISIBLE
                        binding.incidentGoalPlayerAway.text = incidentInfoItem.incidentInfo.player.name
                    }
                }

            }

            val goalType = incidentInfoItem.incidentInfo.goalType
            if (incidentInfoItem.incidentInfo.scoringTeam == "home") {
                binding.verticalLineHome.visibility = View.VISIBLE
                binding.scoreHomeSideLine.visibility = View.VISIBLE
                binding.scoreHomeSideLine.text = "-"
                binding.scoreHomeSideLeft.visibility = View.VISIBLE
                binding.scoreHomeSideLeft.text = incidentInfoItem.incidentInfo.homeScore.toString()
                binding.scoreHomeSideRight.visibility = View.VISIBLE
                binding.scoreHomeSideRight.text = incidentInfoItem.incidentInfo.awayScore.toString()
                // Set home team goal icon
                when (goalType) {
                    "regular" -> binding.incidentImageHome.setImageResource(R.mipmap.icon_football_ball)
                    "owngoal" -> binding.incidentImageHome.setImageResource(R.mipmap.own_goal_icon)
                    "penalty" -> binding.incidentImageHome.setImageResource(R.mipmap.penalty_goal_icon)
                    "onepoint" -> binding.incidentImageHome.setImageResource(R.mipmap.ic_num_basketball_incident_1)
                    "twopoint" -> binding.incidentImageHome.setImageResource(R.mipmap.ic_num_basketball_incident_2)
                    "threepoint" -> binding.incidentImageHome.setImageResource(R.mipmap.ic_num_basketball_incident_3)
                    "touchdown" -> binding.incidentImageHome.setImageResource(R.mipmap.ic_touchdown)
                    "safety" -> binding.incidentImageHome.setImageResource(R.mipmap.ic_rogue)
                    "fieldgoal" -> binding.incidentImageHome.setImageResource(R.mipmap.ic_field_goal)
                    "extrapoint" -> binding.incidentImageHome.setImageResource(R.mipmap.ic_extra_point)
                }
                binding.incidentImageHome.visibility = View.VISIBLE
            } else if (incidentInfoItem.incidentInfo.scoringTeam == "away") {

                binding.verticalLineAway.visibility = View.VISIBLE
                binding.scoreAwaySideLine.visibility = View.VISIBLE
                binding.scoreAwaySideLine.text = "-"
                binding.scoreAwaySideLeft.visibility = View.VISIBLE
                binding.scoreAwaySideLeft.text = incidentInfoItem.incidentInfo.homeScore.toString()
                binding.scoreAwaySideRight.visibility = View.VISIBLE
                binding.scoreAwaySideRight.text = incidentInfoItem.incidentInfo.awayScore.toString()
                // Set away team goal icon
                when (goalType) {
                    "regular" -> binding.incidentImageAway.setImageResource(R.mipmap.icon_football_ball)
                    "owngoal" -> binding.incidentImageAway.setImageResource(R.mipmap.own_goal_icon)
                    "penalty" -> binding.incidentImageAway.setImageResource(R.mipmap.penalty_goal_icon)
                    "onepoint" -> binding.incidentImageAway.setImageResource(R.mipmap.ic_num_basketball_incident_1)
                    "twopoint" -> binding.incidentImageAway.setImageResource(R.mipmap.ic_num_basketball_incident_2)
                    "threepoint" -> binding.incidentImageAway.setImageResource(R.mipmap.ic_num_basketball_incident_3)
                    "touchdown" -> binding.incidentImageAway.setImageResource(R.mipmap.ic_touchdown)
                    "safety" -> binding.incidentImageAway.setImageResource(R.mipmap.ic_rogue)
                    "fieldgoal" -> binding.incidentImageAway.setImageResource(R.mipmap.ic_field_goal)
                    "extrapoint" -> binding.incidentImageAway.setImageResource(R.mipmap.ic_extra_point)
                }
                binding.incidentImageAway.visibility = View.VISIBLE

            }

        }

    }


    class HeaderItemNoIncidentsViewHolder(private val binding: HeaderItemNoIncidentsBinding) : ViewHolder(binding.root) {
        fun bind(header: IncidentItem.HeaderNoIncidentsItem) {
            binding.textNoResult.text = header.title
            binding.viewTournamentLayout.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, TournamentDetailsActivity::class.java).apply {
                    putExtra("tournamentId", header.id.toString())
                }
                context.startActivity(intent)
            }
        }
    }

}

class IncidentsInfoDiffCallback(
    private val oldItems: List<IncidentItem>,
    private val newItems: List<IncidentItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return if (oldItem is IncidentItem.CardIncidentItem && newItem is IncidentItem.CardIncidentItem) {
            oldItem.incidentInfo.id == newItem.incidentInfo.id
        } else if (oldItem is IncidentItem.GoalIncidentItem && newItem is IncidentItem.GoalIncidentItem) {
            oldItem.incidentInfo.id == newItem.incidentInfo.id
        } else if (oldItem is IncidentItem.PeriodIncidentItem && newItem is IncidentItem.PeriodIncidentItem) {
            oldItem.incidentInfo.id == newItem.incidentInfo.id
        } else if (oldItem is IncidentItem.HeaderNoIncidentsItem && newItem is IncidentItem.HeaderNoIncidentsItem) {
            oldItem.title == newItem.title
        }else false

    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem == newItem
    }
}

sealed class IncidentItem {

    data class CardIncidentItem(val incidentInfo: Incident.Card) : IncidentItem()

    data class GoalIncidentItem(val incidentInfo: Incident.Goal, val sport: String) : IncidentItem()

    data class PeriodIncidentItem(val incidentInfo: Incident.Period, val status: String) : IncidentItem()
    data class HeaderNoIncidentsItem(val title: String, val id: Int) : IncidentItem()


}