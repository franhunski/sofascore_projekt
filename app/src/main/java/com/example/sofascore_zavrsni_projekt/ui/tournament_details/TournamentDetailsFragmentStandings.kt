package com.example.sofascore_zavrsni_projekt.ui.tournament_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofascore_zavrsni_projekt.databinding.TournamentDetailsStandingsLayoutBinding
import com.example.sofascore_zavrsni_projekt.ui.adapter.TournamentStandingsAdapter

class TournamentDetailsFragmentStandings: Fragment() {
    private var _binding: TournamentDetailsStandingsLayoutBinding? = null
    private val binding get() = _binding!!

    private val standingsAdapter by lazy { TournamentStandingsAdapter() }

    private val tournamentDetailsViewModel by viewModels<TournamentDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TournamentDetailsStandingsLayoutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = standingsAdapter
        }
        var tournamentId = ""
        arguments?.let {
            tournamentId = it.getString("tournamentId").toString()
        }
        tournamentDetailsViewModel.fetchTournamentStandings(tournamentId.toInt())
        tournamentDetailsViewModel.tournamentStandings.observe(viewLifecycleOwner) {
            standingsAdapter.updateItems(it)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}