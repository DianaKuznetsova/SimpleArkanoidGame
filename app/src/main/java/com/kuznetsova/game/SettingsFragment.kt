package com.kuznetsova.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment


class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val radioGroupTheme: RadioGroup = view.findViewById(R.id.radioGroupTheme)
        val radioButtonLight: RadioButton = view.findViewById(R.id.light)
        val radioButtonDark: RadioButton = view.findViewById(R.id.dark)
        when (PreferencesManager.theme) {
            ThemeEnum.LIGHT -> radioButtonLight.isChecked = true
            ThemeEnum.DARK -> radioButtonDark.isChecked = true
        }
        radioGroupTheme.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.light -> {

                    PreferencesManager.theme = ThemeEnum.LIGHT
                }
                R.id.dark -> {

                    PreferencesManager.theme = ThemeEnum.DARK
                }
            }
            activity?.recreate()
        })
    }
}