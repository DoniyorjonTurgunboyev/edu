package uz.smartarena.edu.iu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uz.smartarena.edu.R
import uz.smartarena.edu.app.App
import uz.smartarena.edu.data.local.EncryptedLocalStorage
import uz.smartarena.edu.data.remote.FirebaseRemote

class SplashActivity : AppCompatActivity() {
    private val storage = EncryptedLocalStorage.getInstance()
    private val remote = FirebaseRemote.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.statusBarColor = resources.getColor(R.color.main,theme)
        window.navigationBarColor = resources.getColor(R.color.main,theme)
        if (storage.uid == "") {
            openLogin()
        } else {
            if (!storage.settings) {
                openSettings()
            } else {
                remote.checkUser(storage.uid){
                    if (it){
                        remote.getUser(storage.uid) { user ->
                            App.user = user
                            openMainActivity()
                        }
                    }else{
                        openLogin()
                    }
                }
            }
        }
    }

    private fun openLogin() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }

    private fun openSettings() {
        startActivity(Intent(this@SplashActivity, RegistrationActivity::class.java))
        finish()
    }

    private fun openMainActivity() {
        startActivity(Intent(this@SplashActivity, FinalMainActivity::class.java))
        finish()
    }
}