package com.example.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dicodingevent.R
import com.example.dicodingevent.databinding.FragmentHomeBinding
import com.example.dicodingevent.ui.common.HorizontalEventAdapter
import com.example.dicodingevent.ui.common.VerticalEventAdapter
import com.example.dicodingevent.utils.Navigation
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var rvUpcomingEvents: RecyclerView
    private lateinit var rvFinishedEvents: RecyclerView

    private lateinit var horizontalEventAdapter: HorizontalEventAdapter
    private lateinit var verticalEventAdapter: VerticalEventAdapter

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: HomeViewModel by viewModels {
            HomeViewModelFactory.getInstance(requireActivity())
        }
        loadUpcomingEvents(viewModel)
        loadFinishedEvents(viewModel)
    }

    private fun loadUpcomingEvents(viewModel: HomeViewModel) {
        rvUpcomingEvents = binding.rvUpcomingEvents
        rvUpcomingEvents.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        viewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            horizontalEventAdapter = HorizontalEventAdapter(events) { eventId ->
                Navigation.navigateToEventDetail(
                    this,
                    eventId.toString(),
                    R.id.action_navigation_home_to_navigation_detail
                )
            }
            rvUpcomingEvents.adapter = horizontalEventAdapter

            if (events.isEmpty()) {
                binding.emptyUpcomingItem.root.visibility = View.VISIBLE
            } else {
                binding.emptyUpcomingItem.root.visibility = View.GONE
            }
        }

        viewModel.isLoadingUpcoming.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.loadingUpcoming.root.visibility = View.VISIBLE
            } else {
                binding.loadingUpcoming.root.visibility = View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorMessageUpcoming.collectLatest { errorMessage ->
                    binding.errorUpcoming.message.visibility = if (errorMessage!= null) View.VISIBLE else View.GONE
                    binding.errorUpcoming.message.text = errorMessage?: ""
                    viewModel.resetErrorMessageUpcoming()
                }
            }
        }
    }

    private fun loadFinishedEvents(viewModel: HomeViewModel) {
        rvFinishedEvents = binding.rvFinishedEvents
        rvFinishedEvents.layoutManager = LinearLayoutManager(context)

        viewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            verticalEventAdapter = VerticalEventAdapter(events) { eventId ->
                Navigation.navigateToEventDetail(
                    this,
                    eventId.toString(),
                    R.id.action_navigation_home_to_navigation_detail
                )
            }
            rvFinishedEvents.adapter = verticalEventAdapter

            if (events.isEmpty()) {
                binding.emptyFinishedItem.root.visibility = View.VISIBLE
            } else {
                binding.emptyFinishedItem.root.visibility = View.GONE
            }
        }

        viewModel.isLoadingFinished.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.loadingFinished.root.visibility = View.VISIBLE
            } else {
                binding.loadingFinished.root.visibility = View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorMessageFinished.collectLatest { errorMessage ->
                    binding.errorFinished.message.visibility = if (errorMessage!= null) View.VISIBLE else View.GONE
                    binding.errorFinished.message.text = errorMessage?: ""
                    viewModel.resetErrorMessageFinished()
                }
            }
        }
    }
}