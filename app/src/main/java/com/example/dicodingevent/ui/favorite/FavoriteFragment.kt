package com.example.dicodingevent.ui.favorite

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
import com.example.dicodingevent.R
import com.example.dicodingevent.data.local.entity.toEvent
import com.example.dicodingevent.databinding.FragmentFavoriteBinding
import com.example.dicodingevent.ui.common.VerticalEventAdapter
import com.example.dicodingevent.utils.Navigation
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

class FavoriteFragment: Fragment() {
    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: FavoriteViewModel by viewModels {
            FavoriteViewModelFactory.getInstance(requireActivity())
        }

        setupFavoriteEvents(viewModel)
    }

    private fun setupFavoriteEvents(viewModel: FavoriteViewModel) {
        val rvFavoriteEvents = binding.rvFavoriteEvents
        rvFavoriteEvents.layoutManager = LinearLayoutManager(context)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favoriteEvents.collectLatest { events ->
                    val verticalEventAdapter = VerticalEventAdapter(events.map { it.toEvent() }) { eventId ->
                        Navigation.navigateToEventDetail(
                            this@FavoriteFragment,
                            eventId.toString(),
                            R.id.action_navigation_favorite_to_navigation_detail
                        )
                    }
                    rvFavoriteEvents.adapter = verticalEventAdapter

                    binding.emptyItem.root.visibility = if (events.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }
}