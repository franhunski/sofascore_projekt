package com.example.sofascore_zavrsni_projekt.ui.tournament_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.sofascore_zavrsni_projekt.databinding.TournamentDetailsMatchesLayoutBinding
import com.example.sofascore_zavrsni_projekt.ui.adapter.TournamentMatchesAdapter
import kotlinx.coroutines.launch

class TournamentDetailsFragmentMatches: Fragment() {
    private var _binding: TournamentDetailsMatchesLayoutBinding? = null
    private val binding get() = _binding!!

    private val matchesAdapter by lazy { TournamentMatchesAdapter() }
    private val viewModel by viewModels<TournamentDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TournamentDetailsMatchesLayoutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        with(binding.recyclerView) {
            layoutManager =
                object : LinearLayoutManager(TournamentDetailsActivity(), RecyclerView.VERTICAL, false) {}
            setHasFixedSize(true)
            descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            adapter = matchesAdapter
        }
        viewModel.liveData.observe(viewLifecycleOwner) {pagingData ->
            lifecycleScope.launch {
                matchesAdapter.submitData(pagingData)
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}