package com.example.sofascore_zavrsni_projekt.ui.leagues_page

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sofascore_zavrsni_projekt.MainActivity
import com.example.sofascore_zavrsni_projekt.databinding.LeaguesFragmentBinding

class LeaguesFragment: Fragment() {
    private var _binding: LeaguesFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LeaguesFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.backIcon.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}