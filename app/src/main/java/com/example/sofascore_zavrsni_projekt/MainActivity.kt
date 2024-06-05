package com.example.sofascore_zavrsni_projekt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sofascore_zavrsni_projekt.databinding.MainActivityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity: AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val navView: BottomNavigationView = binding.topAppBar
        //val navController = findNavController(R.id.nav_host_fragment_activity_main)
        //navView.setupWithNavController(navController)

    }
}