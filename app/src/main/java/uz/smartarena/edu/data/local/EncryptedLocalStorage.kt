package uz.smartarena.edu.data.local

import android.content.Context
import android.content.SharedPreferences
import uz.smartarena.edu.utils.get
import uz.smartarena.edu.utils.save

class EncryptedLocalStorage private constructor() {
    companion object {
        private lateinit var sharedPref: SharedPreferences
        private lateinit var instance: EncryptedLocalStorage

        fun init(context: Context) {
            sharedPref = context.getSharedPreferences("Storage", Context.MODE_PRIVATE)
            instance = EncryptedLocalStorage()
        }

        fun getInstance() = instance
    }

    var uid: String
        get() = sharedPref.get("uid", "")
        set(value) = sharedPref.save("uid", value)

    var numberF: String
        get() = sharedPref.get("numberF", "")
        set(value) = sharedPref.save("numberF", value)

    var number: String
        get() = sharedPref.get("number", "")
        set(value) = sharedPref.save("number", value)

    var settings: Boolean
        get() = sharedPref.get("password", false)
        set(value) = sharedPref.save("password", value)
    var fullName: String
        get() = sharedPref.get("fullName", "")
        set(value) = sharedPref.save("fullName", value)

    var token: String
        get() = sharedPref.get("token", "")
        set(value) = sharedPref.save("token", value)
}