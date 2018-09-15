package comq.mostafa.fci.pets

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.content.DialogInterface
import android.net.Uri
import android.support.v4.app.NavUtils
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem

import comq.mostafa.fci.pets.data.ActivityListener
import comq.mostafa.fci.pets.data.AlterDialog
import comq.mostafa.fci.pets.data.FragmentListener
import comq.mostafa.fci.pets.data.OnAPPModeChangedListener


class EditActivity : AppCompatActivity(), FragmentListener.CallBack {

    internal var mCurrentUri: Uri? = null
    internal var mPetHasChanged = false
    private var callBack: ActivityListener.CallBack? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        if (AlterDialog.getMode(this) == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.DarkTheme)
        else
            setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        AlterDialog.setOnAPPModeChangedListener(object : OnAPPModeChangedListener {
            override fun onModeChanged(mode: Int) {
                AppCompatDelegate.setDefaultNightMode(mode)
                recreate()
            }
        })

        mCurrentUri = intent.data
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = EditFragment()
        fragment.mCurrentUri = mCurrentUri
        fragmentTransaction.add(R.id.fragContainer, fragment)
                .commit()
        callBack = fragment
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.removeItem(R.id.action_delete_all)
        menu.removeItem(R.id.action_dummy_data)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_done) {
            if (mCurrentUri == null) {
                callBack!!.onSavePet()
            } else {
                callBack!!.onUpdatePet()
            }
            return true
        } else if (id == R.id.action_delete) {
            callBack!!.onDeletePet()
            return true
        } else if (id == android.R.id.home) {
            if (!mPetHasChanged) {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
            showUnsavedChangesDialog(true)
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


    override fun onBackPressed() {

        if (!mPetHasChanged) {
            super.onBackPressed()
            return
        }
        showUnsavedChangesDialog(false)
    }

    private fun showUnsavedChangesDialog(actionHome: Boolean) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.unsaved_changes_dialog_msg)
                .setPositiveButton(R.string.discard) { dialogInterface, i ->
                    if (actionHome)
                        NavUtils.navigateUpFromSameTask(this)
                    else
                        finish()
                }
                .setNegativeButton(R.string.keep_editing) { dialog, id ->
                    dialog?.dismiss()
                }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onDataChange(petHasChanged: Boolean) {
        mPetHasChanged = petHasChanged
    }

}
