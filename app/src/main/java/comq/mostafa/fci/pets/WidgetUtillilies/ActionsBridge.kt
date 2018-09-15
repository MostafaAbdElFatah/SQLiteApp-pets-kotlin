package comq.mostafa.fci.pets.WidgetUtillilies

import android.app.IntentService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast

import comq.mostafa.fci.pets.EditActivity
import comq.mostafa.fci.pets.R
import comq.mostafa.fci.pets.data.PetContract.PetEntry


class ActionsBridge : IntentService("ActionsBridge") {

    override fun onHandleIntent(intent: Intent?) {

        val it = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        if (intent == null) return

        val uri = intent.data
        if (uri != null)
            Log.v("ActionBridge", "uri:" + uri.toString())
        val actionCode = intent.getIntExtra(ACTION_CODE, 0)

        when (actionCode) {
            ACTION_DELETE_ALL -> {
                deleteAllPets()
                return
            }
            ACTION_EDIT -> {
                editPet(uri)
                return
            }
            else -> return
        }
    }


    private fun deleteAllPets() {
        val count = contentResolver.delete(PetEntry.CONTENT_URI, null, null)
        if (count == -1) {
            val text = resources.getString(R.string.editor_delete_pets_failed)
            Handler(Looper.getMainLooper()).post(DisplayToast(text))
        } else if (count == 0) {
            val text = resources.getString(R.string.editor_nothing_delete_pets)
            Handler(Looper.getMainLooper()).post(DisplayToast(text))
        } else {
            ActionsBridge.updateWidget(applicationContext)
            val text = resources.getString(R.string.editor_delete_pets_successful)
            Handler(Looper.getMainLooper()).post(DisplayToast(text))
        }
    }

    private fun editPet(uri: Uri?) {
        Log.v("ActionBridge", "editPet function")
        val intent = Intent(this, EditActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setData(uri)
        startActivity(intent)
    }

    private inner class DisplayToast(internal var mText: String) : Runnable {

        override fun run() {
            Toast.makeText(applicationContext, mText, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        val ACTION_EDIT = 3
        val ACTION_DELETE_ALL = 1
        val ACTION_CODE = "com.catchingnow.tinyclipboardmanager.actionCode"

        fun updateWidget(context: Context) {
            // update the list view items only
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisWidget = ComponentName(context, PetsAppWidget::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_main_view)
        }
    }

}
