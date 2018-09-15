package comq.mostafa.fci.pets

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import comq.mostafa.fci.pets.WidgetUtillilies.ActionsBridge
import comq.mostafa.fci.pets.data.ActivityListener
import comq.mostafa.fci.pets.data.FragmentListener
import comq.mostafa.fci.pets.data.PetContract.PetEntry
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.edit_fragment.*
import kotlinx.android.synthetic.main.edit_fragment.view.*


class EditFragment : Fragment(), ActivityListener.CallBack {

    var mCurrentUri: Uri? = null
    private var mPetHasChanged = false
    private lateinit var mNameEditView:EditText
    private lateinit var mBreedEditView:EditText
    private lateinit var  mWeightEditView:EditText
    private lateinit var mGenderSpinner:Spinner
    private var mGender = PetEntry.GENDER_UNKNOWN

    private lateinit var callBack: FragmentListener.CallBack


    private val mTouchListener = View.OnTouchListener { view, motionEvent ->
        mPetHasChanged = true
        callBack.onDataChange(mPetHasChanged)
        false
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callBack = context as FragmentListener.CallBack
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.edit_fragment, container, false)

        mNameEditView = view.findViewById<EditText>(R.id.mNameEditView)
        mBreedEditView = view.findViewById<EditText>(R.id.mBreedEditView)
        mWeightEditView = view.findViewById<EditText>(R.id.mWeightEditView)
        mGenderSpinner = view.findViewById<Spinner>(R.id.mGenderSpinner)

        setupSpinner()
        mCurrentUri?.let {
            setDataToTexts()
        }

        mNameEditView.setOnTouchListener(mTouchListener)
        mBreedEditView.setOnTouchListener(mTouchListener)
        mWeightEditView.setOnTouchListener(mTouchListener)
        mGenderSpinner.setOnTouchListener(mTouchListener)

        return view
    }


    private fun setDataToTexts() {

        mCurrentUri?.let {
            val cursor:Cursor? = context!!.contentResolver.query(mCurrentUri, null, null, null, null)
            cursor?.let {
                val nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME)
                val breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED)
                val weightColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT)
                val genderColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER)

                cursor.moveToFirst()
                val petName = cursor.getString(nameColumnIndex)
                val petBreed = cursor.getString(breedColumnIndex)
                val petWeight = cursor.getInt(weightColumnIndex)
                val petGender = cursor.getInt(genderColumnIndex)

                mNameEditView.setText(petName)
                mBreedEditView.setText(petBreed)
                mWeightEditView.setText(petWeight.toString())
                mGenderSpinner.setSelection(petGender, true)
            }
        }

    }

    private fun setupSpinner() {

        val genderSpinnerAdapter = ArrayAdapter.createFromResource(context!!,
                R.array.array_gender_options, R.layout.spinner_text)
        genderSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
        mGenderSpinner.adapter = genderSpinnerAdapter

        mGenderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selection = parent.getItemAtPosition(position) as String
                if (!TextUtils.isEmpty(selection)) {
                    if (selection == getString(R.string.gender_male)) {
                        mGender = PetEntry.GENDER_MALE
                    } else if (selection == getString(R.string.gender_female)) {
                        mGender = PetEntry.GENDER_FEMALE
                    } else {
                        mGender = PetEntry.GENDER_UNKNOWN
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            override fun onNothingSelected(parent: AdapterView<*>) {
                mGender = PetEntry.GENDER_UNKNOWN
            }
        }
    }


    private fun insertPet(): Boolean {
        val nameString = mNameEditView.text.toString()
        val breedString = mBreedEditView.text.toString()
        val weightString = mWeightEditView.text.toString()
        if (nameString == "" || weightString == "") {
            Toast.makeText(context, "Please set Name or Weight...", Toast.LENGTH_SHORT).show()
            return false
        }
        val weight = Integer.parseInt(weightString)

        val values = ContentValues()
        values.put(PetEntry.COLUMN_PET_NAME, nameString)
        values.put(PetEntry.COLUMN_PET_BREED, breedString)
        values.put(PetEntry.COLUMN_PET_GENDER, mGender)
        values.put(PetEntry.COLUMN_PET_WEIGHT, weight)
        val newUri = context!!.contentResolver.insert(PetEntry.CONTENT_URI, values)
        if (newUri == null) {
            Toast.makeText(context, getString(R.string.editor_insert_pet_failed),
                    Toast.LENGTH_SHORT).show()
            return false
        } else {
            Toast.makeText(context, getString(R.string.editor_insert_pet_successful),
                    Toast.LENGTH_SHORT).show()
            ActionsBridge.updateWidget(context!!)
            return true
        }
    }

    private fun updatePet() {
        val nameString = mNameEditView.text.toString()
        val breedString = mBreedEditView.text.toString()
        val weightString = mWeightEditView.text.toString()

        val weight = Integer.parseInt(weightString)

        val values = ContentValues()
        values.put(PetEntry.COLUMN_PET_NAME, nameString)
        values.put(PetEntry.COLUMN_PET_BREED, breedString)
        values.put(PetEntry.COLUMN_PET_GENDER, mGender)
        values.put(PetEntry.COLUMN_PET_WEIGHT, weight)

        mCurrentUri?.let {
            val count = context!!.contentResolver.update(mCurrentUri, values, null, null)
            if (count == -1)
                Toast.makeText(context, getString(R.string.editor_update_pet_failed),
                        Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(context, getString(R.string.editor_update_pet_successful),
                        Toast.LENGTH_SHORT).show()
            ActionsBridge.updateWidget(context!!)
        }

    }

    private fun deletePet() {

        mCurrentUri?.let {
            val count = context!!.contentResolver.delete(mCurrentUri, null, null)
            if (count == -1)
                Toast.makeText(context, getString(R.string.editor_delete_pet_failed),
                        Toast.LENGTH_SHORT).show()
            else {
                Toast.makeText(context, getString(R.string.editor_delete_pet_successful),
                        Toast.LENGTH_SHORT).show()
                ActionsBridge.updateWidget(context!!)
            }
        }

    }

    override fun onSavePet() {
        if (insertPet())
            activity!!.finish()
    }

    override fun onUpdatePet() {
        updatePet()
        activity!!.finish()
    }

    override fun onDeletePet() {
        deletePet()
        activity!!.finish()
    }

}
