package comq.mostafa.fci.pets.data

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import comq.mostafa.fci.pets.R
import comq.mostafa.fci.pets.data.PetContract.PetEntry

class PetCursorAdapter(context: Context, c: Cursor) : CursorAdapter(context, c, 0) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.item_pet, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {

        val nameTextView = view.findViewById<TextView>(R.id.name_textView)
        val breedTextView = view.findViewById<TextView>(R.id.breed_textView)

        val nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME)
        val breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED)
        val petName = cursor.getString(nameColumnIndex)
        val petBreed = cursor.getString(breedColumnIndex)
        nameTextView.text = petName
        breedTextView.text = petBreed
    }
}