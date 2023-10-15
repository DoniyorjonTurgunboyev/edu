package uz.smartarena.edu.data.local

import uz.smartarena.edu.R
import uz.smartarena.edu.model.Subjects

class SubjectList {
    companion object {
        private lateinit var storage: EncryptedLocalStorage
        fun getBookList(): List<Subjects> {
            storage = EncryptedLocalStorage.getInstance()
            return arrayListOf(
                Subjects(R.drawable.algebra, "Algebra", 42, storage.current - 1, 0.0),
                Subjects(R.drawable.adabiyot, "Adabiyot", 42, 0, 0.0),
                Subjects(R.drawable.fizika, "Fizika", 42, 0, 0.0),
                Subjects(R.drawable.geometriya, "Geometriya", 42, 0, 0.0),
                Subjects(R.drawable.informatika, "Informatika", 42, 0, 0.0),
                Subjects(R.drawable.tarix, "O'zbekiston tarixi", 42, 0, 0.0),
            )
        }
    }
}