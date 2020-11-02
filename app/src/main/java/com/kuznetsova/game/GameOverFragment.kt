package com.kuznetsova.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController

class GameOverFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.game_over_dialog_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.settings)?.setOnClickListener {
            dismiss()
            mainMenu()
        }
        view.findViewById<View>(R.id.start)?.setOnClickListener {
            dismiss()
            goToGame()
        }
    }

    private fun goToGame() {
        findNavController().navigate(R.id.action_gameOverFragment_to_gameFragment)
    }

    private fun mainMenu() {
        findNavController().navigate(R.id.action_gameOverFragment_to_mainMenuFragment)
    }
}