package uz.smartarena.edu.data.remote

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import uz.smartarena.edu.app.App
import uz.smartarena.edu.data.local.EncryptedLocalStorage
import uz.smartarena.edu.model.ThemeData
import uz.smartarena.edu.model.User

class FirebaseRemote {
    private val database = FirebaseDatabase.getInstance()
    private val users = database.getReference("users")
    private val classes = database.getReference("sinflar")
    val auth = Firebase.auth
    private lateinit var user:User
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
        users.child(auth.uid ?: "notAuthenticate").setValue(map).addOnSuccessListener {
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

    fun getAcceptanceByTheme(uid: String, className: String, themeName: String, callback: (Int, Double) -> Unit) {
        var acceptanceCount: Int
        users.child(uid).child("acceptance").child(className).child(themeName).get().addOnSuccessListener { snapshot ->
            acceptanceCount = snapshot.childrenCount.toInt()
            var sum = 0.0
            snapshot.children.forEach { snapshot ->
                sum += snapshot.value.toString().toInt()
            }
            callback.invoke(acceptanceCount, if (acceptanceCount == 0) 0.0 else sum / acceptanceCount)
        }.addOnFailureListener { callback.invoke(0, 0.0) }
    }

    fun getThemesByClassName(className: String, callback: (List<ThemeData>) -> Unit) {
        user = App.user
        val list = arrayListOf<ThemeData>()
        classes.child(className).get().addOnSuccessListener { snapshot ->
            var i = 0
            snapshot.children.forEach { snapshot ->
                val map = snapshot.value as HashMap<*, *>
                list.add(
                    ThemeData(
                        map["info"].toString(),
                        map["theme"].toString(),
                        map["url"].toString(),
                        user.acceptance[i++]
                    )
                )
            }
            callback.invoke(list)
        }
    }


}