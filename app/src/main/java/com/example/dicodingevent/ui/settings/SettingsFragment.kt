package com.example.dicodingevent.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.dicodingevent.data.remote.response.Event
import com.example.dicodingevent.databinding.FragmentSettingsBinding
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

class SettingsFragment: Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsViewModel
    private val sharedPrefs by lazy {
        requireActivity().getSharedPreferences(
            "reminder_prefs",
            Context.MODE_PRIVATE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = viewModels<SettingsViewModel> {
            SettingsViewModelFactory.getInstance(requireActivity())
        }.value

        setupTheme()
        setupReminder()
    }

    private fun setupTheme() {
        val themePrefs = requireActivity().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val isDarkMode = themePrefs.getBoolean("is_dark_mode", false)
        binding.switchDarkMode.isChecked = isDarkMode
        setTheme(isDarkMode)
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            setTheme(isChecked)
            themePrefs.edit().putBoolean("is_dark_mode", isChecked).apply()
        }
    }

    private fun setupReminder() {
        val isReminderEnabled = sharedPrefs.getBoolean("is_reminder_enabled", false)
        binding.switchDailyReminder.isChecked = isReminderEnabled
        setReminder(isReminderEnabled)

        binding.switchDailyReminder.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putBoolean("is_reminder_enabled", isChecked).apply()
            setReminder(isChecked)
        }
    }

    private fun setReminder(isEnabled: Boolean) {
        if (isEnabled) {
            enableReminder()
        } else {
            disableReminder()
        }
    }

    private fun enableReminder() {
        lifecycleScope.launch {
            try {
                val event = viewModel.getNewEvent()
                if (event!= null) {
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .setTriggerContentMaxDelay(15, TimeUnit.MINUTES)
                        .build()

                    val reminderRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
                        1, TimeUnit.DAYS
                    )
                        .setConstraints(constraints)
                        .setInitialDelay(getInitialDelay(), TimeUnit.MILLISECONDS)
                        .build()

                    WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
                        "daily_reminder",
                        ExistingPeriodicWorkPolicy.UPDATE,
                        reminderRequest
                    )

                    saveEventDetails(event)
                } else {
                    disableReminderAndSwitch()
                }
            } catch (e: Exception) {
                handleReminderException(e)
            }
        }
    }

    private fun getInitialDelay(): Long {
        val now = Calendar.getInstance()
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        if (now.after(targetTime)) {
            targetTime.add(Calendar.DAY_OF_YEAR, 1)
        }

        return targetTime.timeInMillis - now.timeInMillis
    }

    private fun saveEventDetails(event: Event) {
        with(sharedPrefs.edit()) {
            putString("event_name", event.name)
            putString("event_time", event.beginTime)
            apply()
        }
    }

    private fun disableReminderAndSwitch() {
        binding.switchDailyReminder.isChecked = false
        sharedPrefs.edit().putBoolean("is_reminder_enabled", false).apply()
    }

    private fun handleReminderException(e: Exception) {
        e.printStackTrace()
        disableReminderAndSwitch()
    }

    private fun disableReminder() {
        WorkManager.getInstance(requireContext()).cancelUniqueWork("daily_reminder")
    }

    private fun setTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}