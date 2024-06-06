package com.example.sofascore_zavrsni_projekt

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sofascore_zavrsni_projekt.databinding.ActivityMainBinding
import com.example.sofascore_zavrsni_projekt.ui.leagues_page.LeaguesActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.topAppBar
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)


        binding.leaguesIcon.setOnClickListener {
            val intent = Intent(this, LeaguesActivity::class.java)
            startActivity(intent)
        }

        //binding.settingsIcon.setOnClickListener {
        //    val intent = Intent(this, SettingsActivity::class.java)
        //    startActivity(intent)
        //}


    }
}