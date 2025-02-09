package com.example.dicodingevent.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.example.dicodingevent.databinding.ItemEventVerticalBinding
import com.example.dicodingevent.models.Event

class VerticalEventAdapter(
    private val events: List<Event>,
    private val listener: (id: Int) -> Unit
) :
    RecyclerView.Adapter<VerticalEventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding =
            ItemEventVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    class EventViewHolder(private val binding: ItemEventVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.ivEvent.load(event.mediaCover)
            binding.tvName.text = event.name
            binding.tvSummary.text = event.summary
        }
    }
}
