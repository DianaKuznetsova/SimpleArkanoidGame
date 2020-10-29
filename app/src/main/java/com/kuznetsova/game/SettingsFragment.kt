package com.kuznetsova.game

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import java.util.*


class SettingsFragment : Fragment() {

    val APP_PREFERENCES = "mysettings"
    var mSettings: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null



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
        val radioButtonDark: RadioButton= view.findViewById(R.id.dark)
        mSettings =  requireContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        var currentTheme: String? = ""
        currentTheme = mSettings?.getString("Theme", "")
        if(currentTheme.equals("") || currentTheme.equals(ThemeEnum.LIGHT.toString())){
            radioButtonLight.isChecked = true
        }else{
            if(currentTheme.equals(ThemeEnum.DARK.toString())){
                radioButtonDark.isChecked = true
            }
        }
        radioGroupTheme.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.light -> {

                    editor = mSettings?.edit()
                    editor?.putString("Theme", ThemeEnum.LIGHT.toString());
                    editor?.apply();
                }
                R.id.dark -> {

                    editor = mSettings?.edit()
                    editor?.putString("Theme", ThemeEnum.DARK.toString());
                    editor?.apply();
                }
            }

            activity?.recreate()
        })



    }



}