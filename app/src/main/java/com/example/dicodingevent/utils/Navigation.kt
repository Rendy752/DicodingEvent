package com.example.dicodingevent.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.dicodingevent.R

object Navigation {
    fun navigateToEventDetail(fragment: Fragment, eventId: String, actionId: Int) {
        val bundle = Bundle().apply {
            putString("id", eventId)
        }
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.navigation_detail, true)
            .build()

        fragment.findNavController().navigate(
            actionId,
            bundle,
            navOptions
        )
    }
}