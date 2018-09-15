package comq.mostafa.fci.pets.WidgetUtillilies

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews

import comq.mostafa.fci.pets.EditActivity
import comq.mostafa.fci.pets.MainActivity
import comq.mostafa.fci.pets.R
import comq.mostafa.fci.pets.data.PetContract.PetEntry


class PetsAppWidget : AppWidgetProvider() {


    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {


        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {

            val views = RemoteViews(context.packageName, R.layout.pets_app_widget)

            //set LinearLayout view
            var mainIntent = PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.widget_LinearLayout_view, mainIntent)

            // set empty view open  Main View
            mainIntent = PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.widget_empty_view, mainIntent)

            //set Add Button
            val addIntent = PendingIntent.getActivity(
                    context,
                    1,
                    Intent(context, EditActivity::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.widget_insert_dummy, addIntent)

            //set delete view
            val deleteIntent = PendingIntent.getService(
                    context,
                    1,
                    Intent(context, ActionsBridge::class.java)
                            .putExtra(ActionsBridge.ACTION_CODE,
                                    ActionsBridge.ACTION_DELETE_ALL),
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.widget_Delete_all_pets, deleteIntent)

            //set setting view
            val settingIntent = PendingIntent.getActivity(
                    context,
                    3,
                    Intent(context, MainActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                            .setData(PetEntry.CONTENT_URI),
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.widget_setting, settingIntent)

            //set main view list
            val intent = Intent(context, AppWidgetService::class.java)
                    .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
            views.setRemoteAdapter(R.id.widget_main_view, intent)
            views.setEmptyView(R.id.widget_main_view, R.id.widget_empty_view)
            views.setPendingIntentTemplate(
                    R.id.widget_main_view,
                    PendingIntent.getService(
                            context,
                            8,
                            Intent(context, ActionsBridge::class.java),
                            PendingIntent.FLAG_UPDATE_CURRENT
                    ))

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }


}

