package com.example.sofascore_zavrsni_projekt.ui.event_details

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.sofascore_zavrsni_projekt.R

class EventDetailsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_container)

        val eventId = intent.getStringExtra("eventId")
        Log.d("activity", "activity=${eventId}")

        if (savedInstanceState == null) {
            val fragment = EventDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString("eventId", eventId)
                }
            }
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit()
        }
    }
}