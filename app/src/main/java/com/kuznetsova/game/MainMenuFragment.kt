package com.kuznetsova.game

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class MainMenuFragment : Fragment() {

    lateinit var autoPlayGameManager: AutoPlayGameManager
    private var isMenuDialogShown:Boolean = false

    private fun goToGame() {
        findNavController().navigate(R.id.action_mainMenuFragment_to_gameFragment)
    }
    private fun setting(){
        findNavController().navigate(R.id.action_mainMenuFragment_to_settingsFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val gameFieldView: GameFieldView = GameFieldView(requireContext())
        gameFieldView.post {

            val screenCenterX: Float = gameFieldView.width / 2f
            val racketCenterY: Float =
                gameFieldView.height - resources.getDimension(R.dimen.racket_bottom_margin) - resources.getDimension(
                    R.dimen.racket_height
                ) / 2f
            val screenCenterY: Float = gameFieldView.height / 2f

            val racket: DrawableObject.Racket = DrawableObject.Racket(
                resources.getDimension(R.dimen.racket_width),
                resources.getDimension(R.dimen.racket_height),
                getThemeColor(requireContext().theme, R.attr.racketColor),
                screenCenterX,
                racketCenterY
            )
            val ball: DrawableObject.Ball = DrawableObject.Ball(
                resources.getDimension(R.dimen.ball_radius),
                getThemeColor(requireContext().theme, R.attr.ballColor),
                screenCenterX,
                screenCenterY
            )
            gameFieldView.drawableObjects.add(racket)

            gameFieldView.drawableObjects.add(ball)

            gameFieldView.invalidate()
            autoPlayGameManager = AutoPlayGameManager(gameFieldView, racket, ball)
            autoPlayGameManager.start()


        }
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

    fun getThemeColor(theme: Resources.Theme, @AttrRes attributeId: Int): Int {
        val value = TypedValue()
        theme.resolveAttribute(attributeId, value, true)
        return  value.data
    }

    fun showMenuDialog() {
        if(isMenuDialogShown)return
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_layout, null, false)
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder
            .setCancelable(false)
            .setView(view)
            /*.setItems(menuList, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    when (which) {
                        0 -> goToGame()
                        1 -> setting()
                    }
                }
            })*/

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
        isMenuDialogShown=true
        dialog.setOnDismissListener {
            isMenuDialogShown = false
        }
    }

}