package com.example.sofascore_zavrsni_projekt.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sofascore_zavrsni_projekt.R


class SettingsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_container)

        if (savedInstanceState == null) {
            val fragment = SettingsFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit()
        }
    }
}