package com.example.sofascore_zavrsni_projekt.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sofascore_zavrsni_projekt.databinding.FragmentSportsBinding

class SportsFragment: Fragment() {

    private var _binding: FragmentSportsBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel by viewModels<SportViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSportsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Example usage of the ViewModel
        binding.chooseUnit.text = "Example Text"

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
