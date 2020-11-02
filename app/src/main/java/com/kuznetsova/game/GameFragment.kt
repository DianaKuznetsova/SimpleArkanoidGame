package com.kuznetsova.game

import android.content.res.Resources
import android.hardware.SensorManager
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GameFragment : Fragment(), GameManager.OnGameEventListener {

    private lateinit var gameManager: GameManager
    private  lateinit var deviceRotationManager: DeviceRotationManager
    private lateinit var levelTV: TextView
    private var level: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.game_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        levelTV = requireView().findViewById<TextView>(R.id.level)
        levelTV.text = getString(R.string.start_level, level)
        val gameFieldView: GameFieldView = view.findViewById(R.id.gameFieldView)

            gameManager = GameManager(this, gameFieldView)
            deviceRotationManager = DeviceRotationManager(requireActivity(), gameManager)


    }

    override fun onResume() {
        super.onResume()

        if (this::gameManager.isInitialized) {
            gameManager.start()
        }
        if(this::deviceRotationManager.isInitialized) {
            deviceRotationManager.start()
        }

    }

    override fun onPause() {
        super.onPause()

        deviceRotationManager.pause()
        gameManager.pause()
    }

    override fun onGameLost() {
        findNavController().navigate(R.id.action_gameFragment_to_gameOverFragment)
    }

    override fun onLevelChange() {
        level++
        GlobalScope.launch(Dispatchers.Main) {
            levelTV.text = getString(R.string.start_level, level)
        }
    }
}