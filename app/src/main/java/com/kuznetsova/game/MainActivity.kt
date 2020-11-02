package com.kuznetsova.game


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        PreferencesManager.init(this)
        setTheme(
            when (PreferencesManager.theme) {
                ThemeEnum.LIGHT -> R.style.AppTheme
                ThemeEnum.DARK -> R.style.DarkTheme
            }
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }


}