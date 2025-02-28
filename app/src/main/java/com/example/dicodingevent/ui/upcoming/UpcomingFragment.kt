package com.example.dicodingevent.ui.upcoming

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
import com.example.dicodingevent.databinding.FragmentUpcomingBinding
import com.example.dicodingevent.ui.common.VerticalEventAdapter
import com.example.dicodingevent.utils.Navigation
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UpcomingFragment : Fragment() {

    private lateinit var rvVerticalEvents: RecyclerView
    private lateinit var verticalEventAdapter: VerticalEventAdapter
    private lateinit var binding: FragmentUpcomingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: UpcomingViewModel by viewModels {
            UpcomingViewModelFactory.getInstance(requireActivity())
        }
        loadEvents(viewModel)
    }

    private fun loadEvents(viewModel: UpcomingViewModel) {
        rvVerticalEvents = binding.rvVerticalEvents
        rvVerticalEvents.layoutManager = LinearLayoutManager(context)

        viewModel.events.observe(viewLifecycleOwner) { events ->
            verticalEventAdapter = VerticalEventAdapter(events) { eventId ->
                Navigation.navigateToEventDetail(
                    this,
                    eventId.toString(),
                    R.id.action_navigation_upcoming_to_navigation_detail
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

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorMessage.collectLatest { errorMessage ->
                    binding.error.message.visibility = if (errorMessage!= null) View.VISIBLE else View.GONE
                    binding.error.message.text = errorMessage?: ""
                    viewModel.resetErrorMessage()
                }
            }
        }
    }
}