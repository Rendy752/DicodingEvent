package com.example.dicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dicodingevent.data.ApiConfig
import com.example.dicodingevent.databinding.FragmentUpcomingBinding
import com.example.dicodingevent.repository.EventRepository
import com.example.dicodingevent.ui.common.VerticalEventAdapter

class UpcomingFragment : Fragment() {

    private lateinit var rvVerticalEvents: RecyclerView
    private lateinit var verticalEventAdapter: VerticalEventAdapter
    private lateinit var binding: FragmentUpcomingBinding
    private val viewModel: UpcomingViewModel by viewModels {
        UpcomingViewModelFactory(EventRepository(ApiConfig.apiService))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvVerticalEvents = binding.rvVerticalEvents
        rvVerticalEvents.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        viewModel.events.observe(viewLifecycleOwner) { events ->
            verticalEventAdapter = VerticalEventAdapter(events)
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