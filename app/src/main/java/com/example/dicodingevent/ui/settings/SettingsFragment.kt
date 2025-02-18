package com.example.dicodingevent.ui.settings

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.dicodingevent.databinding.FragmentSettingsBinding
import com.example.dicodingevent.utils.Constants
import kotlinx.coroutines.launch
import java.util.Calendar

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
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

        viewModel =
            viewModels<SettingsViewModel> { SettingsViewModelFactory.getInstance(requireActivity()) }.value

        val themePrefs = requireActivity().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val isDarkMode = themePrefs.getBoolean("is_dark_mode", false)
        binding.switchDarkMode.isChecked = isDarkMode
        setTheme(isDarkMode)
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            setTheme(isChecked)
            themePrefs.edit().putBoolean("is_dark_mode", isChecked).apply()
        }

        alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), ReminderBroadcastReceiver::class.java)
        intent.action = "com.dicodingevent.REMINDER_ACTION"
        pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            Constants.REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

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
            lifecycleScope.launch {
                try {
                    val event = viewModel.getNewEvent()
                    if (event != null) {
                        val calendar = Calendar.getInstance().apply {
                            this.set(Calendar.HOUR_OF_DAY, 8)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)

                           if (timeInMillis <= System.currentTimeMillis()) {
                               add(Calendar.DAY_OF_YEAR, 1)
                           }
                        }

                        alarmManager.setRepeating(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            AlarmManager.INTERVAL_DAY,
                            pendingIntent
                        )

                        val editor = sharedPrefs.edit()
                        editor.putString("event_name", event.name)
                        editor.putString("event_time", event.beginTime)
                        editor.apply()

                    } else {
                        binding.switchDailyReminder.isChecked = false
                        sharedPrefs.edit().putBoolean("is_reminder_enabled", false).apply()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    binding.switchDailyReminder.isChecked = false
                    sharedPrefs.edit().putBoolean("is_reminder_enabled", false).apply()
                }
            }
        } else {
            alarmManager.cancel(pendingIntent)
        }
    }

    private fun setTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}