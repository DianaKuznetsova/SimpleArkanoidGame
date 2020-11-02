package com.kuznetsova.game

import android.content.Context
import android.content.SharedPreferences

object PreferencesManager {
    private const val APP_PREFERENCES = "themeSettings"
    private const val THEME_KEY = "Theme"


    lateinit var themeSettings: SharedPreferences

    var theme: ThemeEnum
        get() = ThemeEnum.valueOf(themeSettings.getString(THEME_KEY, ThemeEnum.LIGHT.name)!!)
        set(value) {
            themeSettings.edit()
                .putString(THEME_KEY, value.name)
                .apply()
        }

    fun init(context: Context) {
        themeSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    }
}