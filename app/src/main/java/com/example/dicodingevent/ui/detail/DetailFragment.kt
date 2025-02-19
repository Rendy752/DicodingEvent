package com.example.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil3.load
import com.example.dicodingevent.R
import com.example.dicodingevent.databinding.FragmentDetailBinding
import com.example.dicodingevent.utils.Date

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: DetailViewModel by viewModels {
            DetailViewModelFactory.getInstance(requireActivity())
        }
        fetchEventDetail(viewModel)
        observeEventDetail(viewModel)
        setupFavoriteButton(viewModel)
    }

    private fun fetchEventDetail(viewModel: DetailViewModel) {
        val eventId = arguments?.getString("id")
        if (eventId != null) {
            viewModel.loadEvents(eventId)
        }
    }

    private fun observeEventDetail(viewModel: DetailViewModel) {
        viewModel.event.observe(viewLifecycleOwner) { event ->
            if (event != null) {
                with(binding) {
                    emptyItem.root.visibility = View.VISIBLE
                    ivDetail.load(event.mediaCover)
                    tvName.text = event.name
                    tvSummary.text = event.summary
                    tvDescription.text = HtmlCompat.fromHtml(
                        event.description,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                    tvOwner.text = getString(R.string.event_owner, event.ownerName)
                    val remainingQuota = event.quota - event.registrants
                    tvRemainingQuota.text = getString(R.string.remaining_quota, remainingQuota)
                    tvTime.text =
                        getString(R.string.event_time, Date.formatDate(event.beginTime))
                    btnRegister.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(event.link)
                        }
                        startActivity(intent)
                    }
                }
            } else {
                binding.emptyItem.root.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.loading.root.visibility = View.VISIBLE
                binding.emptyItem.root.visibility = View.GONE
            } else {
                binding.loading.root.visibility = View.GONE
                binding.emptyItem.root.visibility = View.GONE
            }
        }
    }

    private fun setupFavoriteButton(viewModel: DetailViewModel) {
        viewModel.event.observe(viewLifecycleOwner) { event ->
            if (event != null) {
                viewModel.isFavorite(event.id)

                viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
                    updateFavoriteButtonIcon(isFavorite)
                }

                binding.fabFavorite.setOnClickListener {
                    val latestEvent = viewModel.event.value ?: return@setOnClickListener

                    if (viewModel.isFavorite.value == true) {
                        viewModel.deleteFavoriteEvent(latestEvent)
                    } else {
                        viewModel.insertFavoriteEvent(latestEvent)
                    }
                }
            }
        }
    }

    private fun updateFavoriteButtonIcon(isFavorite: Boolean) {
        val icon = if (isFavorite) {
            R.drawable.ic_favorite_red_24dp
        } else {
            R.drawable.ic_favorite_border_black_24dp
        }
        binding.fabFavorite.setImageResource(icon)
    }
}