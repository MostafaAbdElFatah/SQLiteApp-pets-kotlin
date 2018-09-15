package comq.mostafa.fci.pets

import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.Toast
import comq.mostafa.fci.pets.WidgetUtillilies.ActionsBridge
import comq.mostafa.fci.pets.data.AlterDialog
import comq.mostafa.fci.pets.data.OnAPPModeChangedListener
import comq.mostafa.fci.pets.data.PetCursorAdapter
import comq.mostafa.fci.pets.data.PetContract.PetEntry
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mPetCursorAdapter: PetCursorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        if (AlterDialog.getMode(this) == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.DarkTheme)
        else
            setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        AlterDialog.setOnAPPModeChangedListener(object : OnAPPModeChangedListener {
            override fun onModeChanged(mode: Int) {
                AppCompatDelegate.setDefaultNightMode(mode)
                recreate()
            }
        })

        var mCurrentURI:Uri? = intent.data
        mCurrentURI?.let {
            showSetting()
            intent.data = null
            recreate()
        }

        fab.setOnClickListener { startActivity(Intent(this@MainActivity, EditActivity::class.java)) }

        listview.emptyView = empty_view
        listview.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            val intent = Intent(this@MainActivity, EditActivity::class.java)
            val uri = ContentUris.withAppendedId(PetEntry.CONTENT_URI, id)
            intent.data = uri
            startActivity(intent)
        }

        displayDatabaseInfo()
    }

    override fun onStart() {
        super.onStart()
        displayDatabaseInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)
        menu.removeItem(R.id.action_done)
        menu.removeItem(R.id.action_delete)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.action_dummy_data) {
            insertPet()
            displayDatabaseInfo()
            return true
        } else if (id == R.id.action_delete_all) {
            deleteAllPets()
            displayDatabaseInfo()
            return true
        } else if (id == R.id.action_setting) {
            showSetting()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSetting() {
        AlterDialog.setup(this).create().show()
    }

    private fun deleteAllPets() {

        val count = contentResolver.delete(PetEntry.CONTENT_URI,
                null, null)
        if (count == -1)
            Toast.makeText(this, getString(R.string.editor_delete_pet_failed),
                    Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this, getString(R.string.editor_delete_pet_successful),
                    Toast.LENGTH_SHORT).show()
    }

    private fun insertPet() {

        val values = ContentValues()
        values.put(PetEntry.COLUMN_PET_NAME, "Tummy")
        values.put(PetEntry.COLUMN_PET_BREED, "Pomeranian")
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE)
        values.put(PetEntry.COLUMN_PET_WEIGHT, 4)
        val newUri = contentResolver.insert(PetEntry.CONTENT_URI, values)
        displayDatabaseInfo()
    }

    private fun displayDatabaseInfo() {
        ActionsBridge.updateWidget(this)
        val cursor = contentResolver.query(
                PetEntry.CONTENT_URI, null, null, null, null)
        try {

            mPetCursorAdapter = PetCursorAdapter(this, cursor!!)
            listview.adapter = mPetCursorAdapter
        } catch (e: Exception) {
        }

    }

}