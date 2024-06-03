package com.example.sofascore_zavrsni_projekt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sofascore_zavrsni_projekt.databinding.ActivityMainBinding
import com.example.sofascore_zavrsni_projekt.ui.SportsFragment


class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SportsFragment())
                .commitNow()
        }

        //val navView: BottomNavigationView = binding.navView

        //val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //val appBarConfiguration = AppBarConfiguration(
        //    setOf(
        //        R.id.navigation_search, R.id.navigation_cities, R.id.navigation_settings
        //    )
        //)
//        setupActionBarWithNavController(navController, appBarConfiguration)
        //navView.setupWithNavController(navController)

        //permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

}