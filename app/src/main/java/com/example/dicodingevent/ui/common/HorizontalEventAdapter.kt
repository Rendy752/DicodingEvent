package com.example.dicodingevent.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.example.dicodingevent.databinding.ItemEventHorizontalBinding
import com.example.dicodingevent.data.remote.response.Event

class HorizontalEventAdapter(
    private val events: List<Event>,
    private val listener: (id: Int) -> Unit
) :
    RecyclerView.Adapter<HorizontalEventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding =
            ItemEventHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
        holder.itemView.setOnClickListener {
            listener(event.id)
        }
    }

    override fun getItemCount(): Int = events.size

    class EventViewHolder(private val binding: ItemEventHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.apply {
                ivEvent.load(event.mediaCover)
                tvName.text = event.name
            }
        }
    }
}