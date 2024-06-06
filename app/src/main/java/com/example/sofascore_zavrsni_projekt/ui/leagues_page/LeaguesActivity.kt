package com.example.sofascore_zavrsni_projekt.ui.leagues_page

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sofascore_zavrsni_projekt.databinding.LeaguesFragmentBinding

class LeaguesActivity: AppCompatActivity() {
    private lateinit var binding: LeaguesFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LeaguesFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIcon.setOnClickListener {
            finish()
        }

    }
}