package uz.smartarena.edu.data

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import uz.smartarena.edu.model.User

class FirebaseRemote {
    private val database = FirebaseDatabase.getInstance()
    private val users = database.getReference("users")
    val auth = Firebase.auth

    companion object {
        private var instance: FirebaseRemote? = null
        fun getInstance(): FirebaseRemote {
            if (instance == null) instance = FirebaseRemote()
            return instance!!
        }
    }

    fun createUser(user: User) {
        val map = HashMap<String, Any>()
        map["name"] = user.name!!
        map["surname"] = user.surname!!
        map["number"] = user.number!!
        map["token"] = user.token!!
        users.child(auth.uid!!).setValue(map).addOnSuccessListener {
            Log.d("TTT", "createUser: ${user.token}")
        }
    }


}