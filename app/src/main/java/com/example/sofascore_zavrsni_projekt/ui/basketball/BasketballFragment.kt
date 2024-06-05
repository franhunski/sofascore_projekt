package com.example.sofascore_zavrsni_projekt.ui.basketball

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofascore_zavrsni_projekt.databinding.BasketballFragmentBinding
import com.example.sofascore_zavrsni_projekt.databinding.ToolbarDatesBinding
import com.example.sofascore_zavrsni_projekt.ui.SportViewModel
import com.example.sofascore_zavrsni_projekt.ui.adapter.EventInfoAdapter
import com.example.sofascore_zavrsni_projekt.ui.adapter.EventItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BasketballFragment: Fragment() {
    private var _binding: BasketballFragmentBinding? = null
    private val binding get() = _binding!!

    private var _toolbarBinding: ToolbarDatesBinding? = null
    private val toolbarBinding get() = _toolbarBinding!!

    private val eventAdapter by lazy { EventInfoAdapter() }

    private val sportViewModel by viewModels<SportViewModel>()
    private val fragmentName = "basketball"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BasketballFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.basketballRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventAdapter
        }

        _toolbarBinding = ToolbarDatesBinding.inflate(inflater, binding.root as ViewGroup, true)

        setupToolbar()


        return root
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val string = sdf.format(calendar.time)
        sportViewModel.fetchEventsForSportAndDate(fragmentName, string)
        sportViewModel.eventInfo.observe(viewLifecycleOwner) {
            eventAdapter.updateItems(it)
            binding.textNumberEvents.text = "${getNumberOfEvents(it)} Events"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun setupToolbar() {
        val sdf = SimpleDateFormat("EEE\ndd.MM", Locale.ENGLISH)
        val date = SimpleDateFormat("EEE, dd.MM.yyyy", Locale.ENGLISH)
        val sdfToday = SimpleDateFormat("dd.MM", Locale.ENGLISH)
        val calendar = Calendar.getInstance()

        binding.textDate.text = "Today"

        calendar.add(Calendar.DAY_OF_YEAR, -1)
        toolbarBinding.dateLeft1.text = sdf.format(calendar.time).toUpperCase(Locale.ENGLISH)
        val dateLeft1 = date.format(calendar.time)

        calendar.add(Calendar.DAY_OF_YEAR, -1)
        toolbarBinding.dateLeft2.text = sdf.format(calendar.time).toUpperCase(Locale.ENGLISH)
        val dateLeft2 = date.format(calendar.time)

        calendar.add(Calendar.DAY_OF_YEAR, -1)
        toolbarBinding.dateLeft3.text = sdf.format(calendar.time).toUpperCase(Locale.ENGLISH)
        val dateLeft3 = date.format(calendar.time)

        calendar.add(Calendar.DAY_OF_YEAR, 3)
        toolbarBinding.dateToday.text = "TODAY\n" + sdfToday.format(calendar.time)

        calendar.add(Calendar.DAY_OF_YEAR, 1)
        toolbarBinding.dateRight1.text = sdf.format(calendar.time).toUpperCase(Locale.ENGLISH)
        val dateRight1 = date.format(calendar.time)

        calendar.add(Calendar.DAY_OF_YEAR, 1)
        toolbarBinding.dateRight2.text = sdf.format(calendar.time).toUpperCase(Locale.ENGLISH)
        val dateRight2 = date.format(calendar.time)

        calendar.add(Calendar.DAY_OF_YEAR, 1)
        toolbarBinding.dateRight3.text = sdf.format(calendar.time).toUpperCase(Locale.ENGLISH)
        val dateRight3 = date.format(calendar.time)


        toolbarBinding.dateLeft1.setOnClickListener { selectDate(dateLeft1) }
        toolbarBinding.dateLeft2.setOnClickListener { selectDate(dateLeft2) }
        toolbarBinding.dateLeft3.setOnClickListener { selectDate(dateLeft3) }
        toolbarBinding.dateToday.setOnClickListener { selectDate(toolbarBinding.dateToday.text.toString()) }
        toolbarBinding.dateRight1.setOnClickListener { selectDate(dateRight1) }
        toolbarBinding.dateRight2.setOnClickListener { selectDate(dateRight2) }
        toolbarBinding.dateRight3.setOnClickListener { selectDate(dateRight3) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun selectDate(date: String) {
        val string : String
        if(!date.startsWith("TODAY")) {
            binding.textDate.text = date.replace("\n", " ")
            val date1 = date.split(", ")[1]
            val date2 = date1.split(".")
            string = date2[2] + "-" + date2[1] + "-" + date2[0]
        }
        else {
            binding.textDate.text = "Today"
            val calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            string = sdf.format(calendar.time)

        }

        val loadingCircle = binding.loadingCircle

        sportViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            loadingCircle.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        sportViewModel.fetchEventsForSportAndDate(fragmentName, string)

        sportViewModel.eventInfo.observe(viewLifecycleOwner) {
            eventAdapter.updateItems(it)
            val counter = getNumberOfEvents(it)
            binding.textNumberEvents.text = "$counter Events"
        }
    }

    private fun getNumberOfEvents(events: List<EventItem>) : Int {
        var counter = 0
        for (event in events) {
            if (event is EventItem.EventInfoItem) {
                counter++
            }
        }
        return counter
    }
}