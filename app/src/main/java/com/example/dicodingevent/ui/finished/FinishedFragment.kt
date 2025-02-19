package com.example.dicodingevent.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dicodingevent.R
import com.example.dicodingevent.databinding.FragmentFinishedBinding
import com.example.dicodingevent.ui.common.VerticalEventAdapter
import com.example.dicodingevent.utils.Debounce
import com.example.dicodingevent.utils.Navigation
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

class FinishedFragment: Fragment() {
    private lateinit var rvVerticalEvents: RecyclerView
    private lateinit var verticalEventAdapter: VerticalEventAdapter
    private lateinit var binding: FragmentFinishedBinding
    private lateinit var debounce: Debounce

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: FinishedViewModel by viewModels {
            FinishedViewModelFactory.getInstance(requireActivity())
        }
        rvVerticalEvents = binding.rvVerticalEvents
        rvVerticalEvents.layoutManager = LinearLayoutManager(context)

        debounce = Debounce(lifecycleScope, viewModel = viewModel)

        setupSearchBar()
        observeEvents(viewModel)
    }

    private fun setupSearchBar() {
        binding.searchBar.svEvents.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                debounce.query(newText?: "")
                return true
            }
        })
    }

    private fun observeEvents(viewModel: FinishedViewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collectLatest { events ->
                    verticalEventAdapter = VerticalEventAdapter(events) { eventId ->
                        Navigation.navigateToEventDetail(
                            this@FinishedFragment,
                            eventId.toString(),
                            R.id.action_navigation_finished_to_navigation_detail
                        )
                    }
                    rvVerticalEvents.adapter = verticalEventAdapter

                    binding.emptyItem.root.visibility =
                        if (events.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loading.root.visibility = if (isLoading) View.VISIBLE else View.GONE
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