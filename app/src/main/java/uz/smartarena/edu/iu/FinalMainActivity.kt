package uz.smartarena.edu.iu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import uz.smartarena.edu.R
import uz.smartarena.edu.databinding.ActivityFinalMainBinding

class FinalMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinalMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = resources.getColor(R.color.main, theme)
        window.navigationBarColor = resources.getColor(R.color.main, theme)
        binding = ActivityFinalMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}