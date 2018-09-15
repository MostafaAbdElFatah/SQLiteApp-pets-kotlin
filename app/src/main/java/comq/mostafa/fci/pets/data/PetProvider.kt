package comq.mostafa.fci.pets.data

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import comq.mostafa.fci.pets.data.PetContract.PetEntry

class PetProvider : ContentProvider() {

    private var mPetDBHelper: PetDBHelper? = null

    override fun onCreate(): Boolean {
        mPetDBHelper = PetDBHelper(context!!)
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?,
                       sortOrder: String?): Cursor? {
        var selection = selection
        var selectionArgs = selectionArgs
        val mSqLiteDatabase = mPetDBHelper!!.readableDatabase
        var cursor: Cursor? = null
        val match = sUriMatcher.match(uri)
        when (match) {
            PETS -> cursor = mSqLiteDatabase.query(PetEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            PET_ID -> {
                selection = PetEntry._ID + "=?"
                selectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                cursor = mSqLiteDatabase.query(PetEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            }
            else -> throw IllegalArgumentException("Cannot query unknown URI $uri")
        }
        return cursor
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val match = sUriMatcher.match(uri)
        when (match) {
            PETS -> return insertPet(uri, contentValues)
            else -> throw IllegalArgumentException("Insertion is not supported for $uri")
        }
    }

    private fun insertPet(uri: Uri, values: ContentValues?): Uri? {
        val database = mPetDBHelper!!.writableDatabase
        val id = database.insert(PetEntry.TABLE_NAME, null, values)
        return if ( id.toInt() == -1 ) {
            null
        } else ContentUris.withAppendedId(uri, id)
    }

    override fun update(uri: Uri, contentValues: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        var selection = selection
        var selectionArgs = selectionArgs
        val match = sUriMatcher.match(uri)
        when (match) {
            PETS -> return updatePet(uri, contentValues, selection, selectionArgs)
            PET_ID -> {
                selection = PetEntry._ID + "=?"
                selectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                return updatePet(uri, contentValues, selection, selectionArgs)
            }
            else -> throw IllegalArgumentException("Update is not supported for $uri")
        }
    }

    private fun updatePet(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val database = mPetDBHelper!!.writableDatabase
        return database.update(PetEntry.TABLE_NAME, values, selection, selectionArgs)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var selection = selection
        var selectionArgs = selectionArgs
        val match = sUriMatcher.match(uri)
        when (match) {
            PETS -> return deletePet(uri, selection, selectionArgs)
            PET_ID -> {
                selection = PetEntry._ID + "=?"
                selectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                return deletePet(uri, selection, selectionArgs)
            }
            else -> throw IllegalArgumentException("Update is not supported for $uri")
        }
    }

    private fun deletePet(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val database = mPetDBHelper!!.writableDatabase
        return database.delete(PetEntry.TABLE_NAME, selection, selectionArgs)
    }

    override fun getType(uri: Uri): String? {
        val match = sUriMatcher.match(uri)
        when (match) {
            PETS -> return PetEntry.CONTENT_LIST_TYPE
            PET_ID -> return PetEntry.CONTENT_ITEM_TYPE
            else -> throw IllegalStateException("Unknown URI $uri with match $match")
        }
    }

    companion object {
        val LOG_TAG = PetProvider::class.java.simpleName
        val PETS = 100
        val PET_ID = 101
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS, PETS)
            sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS + "/#", PET_ID)
        }
    }
}

