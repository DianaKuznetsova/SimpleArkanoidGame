package com.kuznetsova.game

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class MainMenuFragment : Fragment() {

    private lateinit var autoPlayGameManager: AutoPlayGameManager
    private var isMenuDialogShown: Boolean = false

    private fun goToGame() {
        findNavController().navigate(R.id.action_mainMenuFragment_to_gameFragment)
    }

    private fun setting() {
        findNavController().navigate(R.id.action_mainMenuFragment_to_settingsFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val gameFieldView: GameFieldView = GameFieldView(requireContext())

        autoPlayGameManager = AutoPlayGameManager(gameFieldView)

        return gameFieldView
    }


    override fun onStart() {
        super.onStart()
        showMenuDialog()
        if (this::autoPlayGameManager.isInitialized) {
            autoPlayGameManager.start()
        }

    }

    override fun onStop() {
        super.onStop()
        autoPlayGameManager.pause()
    }

    private fun showMenuDialog() {
        if (isMenuDialogShown) return
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_layout, null, false)
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder
            .setCancelable(false)
            .setView(view)

        val dialog = builder.create()
        view.findViewById<View>(R.id.settings)?.setOnClickListener {
            setting()
            dialog.dismiss()
        }
        view.findViewById<View>(R.id.start)?.setOnClickListener {
            goToGame()
            dialog.dismiss()
        }
        dialog.show()
        isMenuDialogShown = true
        dialog.setOnDismissListener {
            isMenuDialogShown = false
        }
    }

}