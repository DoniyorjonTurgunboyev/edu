package uz.smartarena.edu.data.remote

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import uz.smartarena.edu.data.local.EncryptedLocalStorage
import uz.smartarena.edu.model.User

class FirebaseRemote {
    private val database = FirebaseDatabase.getInstance()
    private val users = database.getReference("users")
    val auth = Firebase.auth
    private val storage = EncryptedLocalStorage.getInstance()


    companion object {
        private var instance: FirebaseRemote? = null
        fun getInstance(): FirebaseRemote {
            if (instance == null) instance = FirebaseRemote()
            return instance!!
        }
    }

    fun createUser(user: User, callback: () -> Unit) {
        val map = HashMap<String, Any>()
        map["name"] = user.name!!
        map["surname"] = user.surname!!
        map["number"] = user.number!!
        users.child(auth.uid?:"notAuthenticate").setValue(map).addOnSuccessListener {
            callback.invoke()
        }
    }

    fun getUser(uid: String, callback: (User) -> Unit) {
        if (storage.uid == "") return
        val id = if (uid == "") auth.uid!! else uid
        users.child(id).get().addOnSuccessListener { snapshot ->
            if (snapshot.value == null) {
                storage.uid = ""
                return@addOnSuccessListener
            }
            val map = snapshot.value as HashMap<*, *>
            val user = User()
            user.name = map["name"].toString()
            user.surname = map["surname"].toString()
            user.number = map["number"].toString()
            callback.invoke(user)
        }
    }

    fun checkUser(uid: String, callback1: (Boolean) -> Unit) {
        users.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                callback1.invoke(snapshot.exists())
            }

            override fun onCancelled(error: DatabaseError) {
                callback1.invoke(false)
            }
        })
    }
}