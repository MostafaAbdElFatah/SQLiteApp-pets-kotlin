package comq.mostafa.fci.pets.data

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns
import comq.mostafa.fci.pets.data.PetContract.BASE_CONTENT_URI
import comq.mostafa.fci.pets.data.PetContract.CONTENT_AUTHORITY
import comq.mostafa.fci.pets.data.PetContract.PATH_PETS

object PetContract {
         val CONTENT_AUTHORITY = "com.example.android.pets"
         val BASE_CONTENT_URI = Uri.parse("content://$CONTENT_AUTHORITY")
         val PATH_PETS = "pets"

    object PetEntry : BaseColumns {

            val CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS)
            val CONTENT_LIST_TYPE =
                    ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS
            val CONTENT_ITEM_TYPE =
                    ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS

            val TABLE_NAME = "pets"

            val _ID = BaseColumns._ID
            val COLUMN_PET_NAME = "name"
            val COLUMN_PET_BREED = "breed"
            val COLUMN_PET_GENDER = "gender"
            val COLUMN_PET_WEIGHT = "weight"
            val GENDER_UNKNOWN = 0
            val GENDER_MALE = 1
            val GENDER_FEMALE = 2
    }

}