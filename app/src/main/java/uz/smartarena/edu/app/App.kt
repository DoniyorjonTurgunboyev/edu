package uz.smartarena.edu.app

import android.app.Application
import uz.smartarena.edu.data.local.EncryptedLocalStorage
import uz.smartarena.edu.model.User

class App : Application() {
    companion object{
        lateinit var instance: App
        lateinit var user: User
        fun userIsInitialized(): Boolean = this::user.isInitialized
    }

    override fun onCreate() {
        super.onCreate()
        EncryptedLocalStorage.init(this)
        instance=this
    }
}