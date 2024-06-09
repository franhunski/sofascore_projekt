package com.example.sofascore_zavrsni_projekt.ui.event_details

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sofascore_zavrsni_projekt.databinding.EventDetailsFragmentBinding
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofascore_zavrsni_projekt.R
import com.example.sofascore_zavrsni_projekt.ui.adapter.EventIncidentsAdapter
import com.example.sofascore_zavrsni_projekt.ui.tournament_details.TournamentDetailsActivity
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration

class EventDetailsFragment: Fragment() {
    private var _binding: EventDetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private val incidentAdapter by lazy { EventIncidentsAdapter() }

    private val eventDetailsViewModel by viewModels<EventDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EventDetailsFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.backIcon.setOnClickListener {
            requireActivity().finish()
        }

        binding.eventDetailsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = incidentAdapter
        }

        val argument = arguments?.getString("eventId")?.split(" ", limit = 3)
        val tournamentId = argument?.get(2)
        binding.infoText.setOnClickListener {
            val intent = Intent(requireContext(), TournamentDetailsActivity::class.java)
            intent.putExtra("tournamentId", tournamentId)
            startActivity(intent)
        }

        return root
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val argument = arguments?.getString("eventId")?.split(" ", limit = 3)
        val eventId = argument?.get(0)
        val sport = argument?.get(1)
        val tournamentId = argument?.get(2)
        if (eventId != null) {
            eventDetailsViewModel.fetchEventWithId(eventId.toInt())
        }
        eventDetailsViewModel.event.observe(viewLifecycleOwner) {
            eventDetailsViewModel.fetchTournamentLogo(it.tournament.id)
            val string = it.tournament.sport.name + ", " + it.tournament.country.name + ", " + it.tournament.name + ", Round " + it.round.toString()
            binding.infoText.text = string
            binding.homeTeamName.text = it.homeTeam.name
            binding.awayTeamName.text = it.awayTeam.name
            val startTime = parseDateTime(it.startDate)
            if (it.status == "notstarted") {
                binding.startDate.text = startTime.first
                binding.startTime.text = startTime.second
            } else if (it.status == "inprogress") {
                binding.homeScoreLive.text = it.homeScore.total.toString()
                binding.awayScoreLive.text = it.awayScore.total.toString()
                binding.scoreLiveLine.text = "-"
                val startDateString = "2019-08-24T14:15:22Z"
                val startDate = ZonedDateTime.parse(startDateString)
                val currentDate = ZonedDateTime.now(ZoneOffset.UTC)
                val duration = Duration.between(startDate, currentDate)
                val minutesPassed = duration.toMinutes()
                binding.scoreLiveTime.text = "$minutesPassed'"
            } else if (it.status == "finished") {
                binding.homeScoreEnd.text = it.homeScore.total.toString()
                binding.awayScoreEnd.text = it.awayScore.total.toString()
                binding.scoreEndLine.text = "-"
                binding.scoreEndTime.text = "Full Time"
                if (it.winnerCode == "home") {
                   binding.homeScoreEnd.setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface_on_surface_lv_1))
                } else if(it.winnerCode == "away") {
                    binding.awayScoreEnd.setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface_on_surface_lv_1))
                }
            }
            eventDetailsViewModel.fetchTeamLogos(it.homeTeam.id, it.awayTeam.id)
        }
        eventDetailsViewModel.tournamentLogo.observe(viewLifecycleOwner) {
            binding.tournamentIcon.setImageBitmap(it)
        }
        eventDetailsViewModel.teamLogos.observe(viewLifecycleOwner) {
            binding.homeTeamIcon.setImageBitmap(it[0])
            binding.awayTeamIcon.setImageBitmap(it[1])
        }
        if (sport != null) {
            eventDetailsViewModel.fetchEventIncidents(eventId!!.toInt(), sport, tournamentId!!.toInt())
        }
        eventDetailsViewModel.eventIncidents.observe(viewLifecycleOwner) {
            incidentAdapter.updateItems(it)

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDateTime(dateTimeStr: String): Pair<String, String> {
        val inputFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
        val zonedDateTime = ZonedDateTime.parse(dateTimeStr, inputFormatter)
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        val formattedDate = zonedDateTime.format(dateFormatter)
        val formattedTime = zonedDateTime.format(timeFormatter)

        return Pair(formattedDate, formattedTime)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}