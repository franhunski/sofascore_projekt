package com.example.sofascore_zavrsni_projekt.ui.tournament_details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.sofascore_zavrsni_projekt.R
import com.example.sofascore_zavrsni_projekt.databinding.TournamentDetailsFragmentBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.activity.viewModels

private const val NUM_PAGES = 2
class TournamentDetailsActivity: AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var binding: TournamentDetailsFragmentBinding
    private val viewModel by viewModels<TournamentDetailsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = TournamentDetailsFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = binding.pager

        val tournamentId = intent.getStringExtra("tournamentId")
        val pagerAdapter = tournamentId?.let { ScreenSlidePagerAdapter(this, it) }
        viewPager.adapter = pagerAdapter

        setupTabs()

        viewModel.fetchTournamentDetails(tournamentId!!.toInt())
        viewModel.fetchTournamentLogo(tournamentId.toInt())
        viewModel.tournamentDetails.observe(this) {
            binding.tournamentCountryName.text = it.country.name
            binding.tournamentName.text = it.name
        }
        viewModel.tournamentLogo.observe(this) {
            binding.tournamentIcon.setImageBitmap(it)
        }

        binding.backIcon.setOnClickListener {
            finish()
        }

    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity, private val tournamentId: String) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            val fragment = when (position) {
                0 -> TournamentDetailsFragmentMatches()
                1 -> TournamentDetailsFragmentStandings()
                else -> throw IllegalArgumentException("Invalid position")
            }
            fragment.arguments = Bundle().apply {
                putString("tournamentId", tournamentId)
            }
            return fragment
        }
    }

    private fun setupTabs() {
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Matches"
                1 -> "Standings"
                else -> "Tab ${position + 1}"
            }
        }.attach()
    }
}