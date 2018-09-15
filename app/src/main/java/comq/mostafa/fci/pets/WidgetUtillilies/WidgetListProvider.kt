package comq.mostafa.fci.pets.WidgetUtillilies

import android.appwidget.AppWidgetManager
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import java.util.ArrayList;
import comq.mostafa.fci.pets.R
import comq.mostafa.fci.pets.data.Pet
import comq.mostafa.fci.pets.data.PetContract.PetEntry

class WidgetListProvider(private val context: Context, intent: Intent) : RemoteViewsService.RemoteViewsFactory {

    private val appWidgetId: Int
    private val petsList: ArrayList<Pet>

    init {
        this.petsList = ArrayList()
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID)
    }

    override fun onCreate() {

    }

    override fun onDataSetChanged() {

        petsList.clear()
        val cursor = context.contentResolver.query(
                PetEntry.CONTENT_URI, null, null, null, null)

        petsList.addAll(Pet.getPetsList(cursor))
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int {
        return petsList.size
    }

    override fun getViewAt(index: Int): RemoteViews {
        val remoteView = RemoteViews(
                context.packageName, R.layout.app_widget_item_list)
        val pet = petsList[index]
        val gender: String
        when (pet.gender) {
            PetEntry.GENDER_MALE -> gender = "Male"
            PetEntry.GENDER_FEMALE -> gender = "Female"
            else -> gender = "Unknown"
        }

        remoteView.setTextViewText(R.id.name_pet_widget, pet.name)
        remoteView.setTextViewText(R.id.breed_pet_widget, pet.breed)
        remoteView.setTextViewText(R.id.gender_pet_widget, gender)
        remoteView.setTextViewText(R.id.weight_pet_widget, pet.weight.toString())

        //Pass the ID to EditActivity
        val uri = ContentUris.withAppendedId(PetEntry.CONTENT_URI, pet.id.toLong())
        val fillInIntent = Intent()
        fillInIntent
                .setData(uri)
                .putExtra(ActionsBridge.ACTION_CODE, ActionsBridge.ACTION_EDIT)
        remoteView.setOnClickFillInIntent(R.id.widget_card_click_edit, fillInIntent)

        return remoteView
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(i: Int): Long {
        return petsList[i].id.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }
}
