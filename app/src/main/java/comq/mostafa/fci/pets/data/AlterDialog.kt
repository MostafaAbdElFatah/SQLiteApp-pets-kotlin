package comq.mostafa.fci.pets.data

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDelegate
import android.support.v7.view.ContextThemeWrapper

import java.util.ArrayList

import comq.mostafa.fci.pets.R

import android.content.Context.MODE_PRIVATE
import android.view.View.MeasureSpec.getMode
import android.widget.Toast

object AlterDialog {

    private val setting = arrayOf("Night Mode")
    private lateinit var checked:BooleanArray
    private val listeners = ArrayList<OnAPPModeChangedListener>()

    fun setup(context: Context): AlertDialog.Builder {

        var mode:Boolean = false
        if (getMode(context) == AppCompatDelegate.MODE_NIGHT_YES ) {
            mode = true
        }
        checked = booleanArrayOf(mode)

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.action_setting)

        builder.setMultiChoiceItems(setting, checked) { dialog, which, isChecked ->
            if (isChecked)
                saveData(context,AppCompatDelegate.MODE_NIGHT_YES)
            else
                saveData(context,AppCompatDelegate.MODE_NIGHT_NO)
            for (listener:OnAPPModeChangedListener in listeners)
                listener.onModeChanged(getMode(context))
            dialog?.dismiss()
        }

        return builder
    }

    private fun saveData(context: Context, mode: Int) {
        val sharedPreferences = context.getSharedPreferences("Setting", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("mode", mode)
        editor.apply()
    }

    fun getMode(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences("Setting", MODE_PRIVATE)
        return sharedPreferences.getInt("mode", AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun setOnAPPModeChangedListener(listener: OnAPPModeChangedListener) {
        listeners.add(listener)
    }
}
