package comq.mostafa.fci.pets.data

import android.database.Cursor
import java.io.Serializable
import comq.mostafa.fci.pets.data.PetContract.PetEntry



class Pet(var id: Int, var name: String?, var breed: String?, var gender: Int, var weight: Int) : Serializable{

    companion object {
        fun getPetsList(cursor: Cursor): ArrayList<Pet> {
            val pets = ArrayList<Pet>()

            val idColumnIndex = cursor.getColumnIndex(PetEntry._ID)
            val nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME)
            val breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED)
            val genderColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER)
            val weightColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT)

            while (cursor.moveToNext()) {
                val currentID = cursor.getInt(idColumnIndex)
                val currentName = cursor.getString(nameColumnIndex)
                val currentBreed = cursor.getString(breedColumnIndex)
                val currentGender = cursor.getInt(genderColumnIndex)
                val currentWeight = cursor.getInt(weightColumnIndex)
                val pet = Pet(currentID, currentName, currentBreed, currentGender, currentWeight)
                pets.add(pet)
            }
            return pets
        }
    }
}
