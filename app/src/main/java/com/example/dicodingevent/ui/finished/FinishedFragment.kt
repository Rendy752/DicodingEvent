package com.example.dicodingevent.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dicodingevent.R
import com.example.dicodingevent.data.ApiConfig
import com.example.dicodingevent.databinding.FragmentFinishedBinding
import com.example.dicodingevent.repository.EventRepository
import com.example.dicodingevent.ui.common.VerticalEventAdapter
import com.example.dicodingevent.utils.Navigation
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FinishedFragment : Fragment() {
    private lateinit var rvVerticalEvents: RecyclerView
    private lateinit var verticalEventAdapter: VerticalEventAdapter
    private lateinit var binding: FragmentFinishedBinding
    private val viewModel: FinishedViewModel by viewModels {
        FinishedViewModelFactory(EventRepository(ApiConfig.apiService))
    }
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchBar()
        loadEvents()
    }

    private fun setupSearchBar() {
        binding.searchBar.svEvents.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    delay(1000)
                    viewModel.searchEvents(newText)
                }
                return true
            }
        })
    }

    private fun loadEvents() {
        rvVerticalEvents = binding.rvVerticalEvents
        rvVerticalEvents.layoutManager = LinearLayoutManager(context)

        viewModel.events.observe(viewLifecycleOwner) { events ->
            verticalEventAdapter = VerticalEventAdapter(events) { eventId ->
                Navigation.navigateToEventDetail(
                    this,
                    eventId.toString(),
                    R.id.action_navigation_finished_to_navigation_detail
                )
            }
            rvVerticalEvents.adapter = verticalEventAdapter

            if (events.isEmpty()) {
                binding.emptyItem.root.visibility = View.VISIBLE
            } else {
                binding.emptyItem.root.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.loading.root.visibility = View.VISIBLE
            } else {
                binding.loading.root.visibility = View.GONE
            }
        }
    }
}