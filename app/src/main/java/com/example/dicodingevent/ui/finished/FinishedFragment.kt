package com.example.dicodingevent.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dicodingevent.data.ApiConfig
import com.example.dicodingevent.databinding.FragmentFinishedBinding
import com.example.dicodingevent.repository.EventRepository
import com.example.dicodingevent.ui.common.VerticalEventAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FinishedFragment : Fragment() {
    private lateinit var rvVerticalEvents: RecyclerView
    private lateinit var verticalEventAdapter: VerticalEventAdapter
    private lateinit var binding: FragmentFinishedBinding
    private val viewModel: FinishedViewModel by viewModels {
        FinishedViewModelFactory(EventRepository(ApiConfig.apiService))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvVerticalEvents = binding.rvVerticalEvents
        rvVerticalEvents.layoutManager = LinearLayoutManager(context)

        viewModel.events.observe(viewLifecycleOwner) { events ->
            verticalEventAdapter = VerticalEventAdapter(events)
            rvVerticalEvents.adapter = verticalEventAdapter
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                // Show loading indicator
            } else {
                // Hide loading indicator
            }
        }
    }
}