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

    var algebra: Double
        get() = sharedPref.get("algebra", 0.0)
        set(value) = sharedPref.save("algebra", value)

    var bir: Double
        get() = sharedPref.get("bir", 0.0)
        set(value) = sharedPref.save("bir", value)

    var ikki: Double
        get() = sharedPref.get("ikki", 0.0)
        set(value) = sharedPref.save("ikki", value)

    var uch: Double
        get() = sharedPref.get("uch", 0.0)
        set(value) = sharedPref.save("uch", value)

    var tort: Double
        get() = sharedPref.get("tort", 0.0)
        set(value) = sharedPref.save("tort", value)

    var besh: Double
        get() = sharedPref.get("besh", 0.0)
        set(value) = sharedPref.save("besh", value)

    var current: Int
        get() = sharedPref.get("current", 1)
        set(value) = sharedPref.save("current", value)
}