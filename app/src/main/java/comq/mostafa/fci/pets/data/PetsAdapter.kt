package comq.mostafa.fci.pets.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.util.ArrayList
import comq.mostafa.fci.pets.R
import kotlinx.android.synthetic.main.item_pet.view.*

class PetsAdapter(context: Context, pets: ArrayList<Pet>) : ArrayAdapter<Pet>(context, R.layout.item_pet, pets) {

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View? {
        var listItemView:View? = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                    R.layout.item_pet, parent, false)
        }

        listItemView?.let {

            val pet = getItem(position)
            pet?.let {
                listItemView.name_textView.text = pet.name
                listItemView.breed_textView.text = pet.breed
            }
            return listItemView

        }

        return listItemView
    }
}
