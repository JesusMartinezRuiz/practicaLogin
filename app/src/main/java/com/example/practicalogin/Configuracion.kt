package com.example.practicalogin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Switch
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate

class Configuracion : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion)

        chooseThemeDialog()

    }

    class MyPreferences(context: Context?) {

        companion object {
            private const val DARK_STATUS = "io.github.manuelernesto.DARK_STATUS"
        }

        private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

        var darkMode = preferences.getInt(DARK_STATUS, 0)
            set(value) = preferences.edit().putInt(DARK_STATUS, value).apply()

    }

    private fun chooseThemeDialog() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.choose_theme_text))
        val styles = arrayOf("Light", "Dark", "System default")
        val checkedItem = MyPreferences(this).darkMode

        builder.setSingleChoiceItems(styles, checkedItem) { dialog, which ->

            when (which) {
                0 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    MyPreferences(this).darkMode = 0
                    delegate.applyDayNight()
                    dialog.dismiss()
                    val actividad = Intent(applicationContext,MainActivity::class.java)
                    startActivity (actividad)
                }
                1 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    MyPreferences(this).darkMode = 1
                    delegate.applyDayNight()
                    dialog.dismiss()
                    val actividad = Intent(applicationContext,MainActivity::class.java)
                    startActivity (actividad)
                }
                2 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    MyPreferences(this).darkMode = 2
                    delegate.applyDayNight()
                    dialog.dismiss()
                    val actividad = Intent(applicationContext,MainActivity::class.java)
                    startActivity (actividad)
                }

            }
        }

        val dialog = builder.create()
        dialog.show()
    }

}