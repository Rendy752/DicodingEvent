package com.example.dicodingevent.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.dicodingevent.databinding.FragmentSettingsBinding

class SettingsFragment: Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPrefs = requireActivity().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()

        val isDarkMode = sharedPrefs.getBoolean("is_dark_mode", false)
        binding.switchDarkMode.isChecked = isDarkMode
        setTheme(isDarkMode)

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            setTheme(isChecked)
            editor.putBoolean("is_dark_mode", isChecked).apply()
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