package comq.mostafa.fci.pets.data

class ActivityListener {
    interface CallBack {
        fun onSavePet()
        fun onUpdatePet()
        fun onDeletePet()
    }
}
