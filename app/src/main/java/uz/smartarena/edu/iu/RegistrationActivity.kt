package uz.smartarena.edu.iu

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.niwattep.materialslidedatepicker.SlideDatePickerDialog
import com.niwattep.materialslidedatepicker.SlideDatePickerDialogCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.smartarena.edu.R
import uz.smartarena.edu.data.local.EncryptedLocalStorage
import uz.smartarena.edu.data.remote.FirebaseRemote
import uz.smartarena.edu.databinding.ActivitySettingsBinding
import uz.smartarena.edu.iu.adapter.LocationAdapter
import uz.smartarena.edu.model.User
import uz.smartarena.edu.utils.resIdByName
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RegistrationActivity : AppCompatActivity(), SlideDatePickerDialogCallback {
    private lateinit var binding: ActivitySettingsBinding
    private var bRegion = ""
    private val storage = EncryptedLocalStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = resources.getColor(R.color.main)
        window.navigationBarColor = resources.getColor(R.color.main)
        clickLocations()
        binding.number.setText(storage.numberF)
        val c = Calendar.getInstance()
        c.set(2000, 0, 1)
        val datePicker =
            MaterialDatePicker.Builder.datePicker().setTitleText("Select date").setSelection(c.timeInMillis)
                .setTheme(R.style.ThemeOverlay_App_DatePicker).build()
        datePicker.addOnPositiveButtonClickListener {
            val trlocale = Locale("uz-UZ")
            val simple = SimpleDateFormat("dd.MM.yyyy", trlocale)
            val calendar = Calendar.getInstance(trlocale)
            val month: String =
                calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) as String
            Toast.makeText(this, month, Toast.LENGTH_SHORT).show()
            val result = Date(it)
            binding.birthday.text = simple.format(result)
            binding.birthday.setTextColor(resources.getColor(R.color.black))
        }
        binding.fiald3.setOnClickListener {
            datePicker()
        }
        binding.save.setOnClickListener {
            saveInfo()
        }
    }

    private fun clickLocations() {
        binding.address.setOnClickListener {
            val list = resources.getStringArray(R.array.regions).toList()
            showBottomSheetDialog(list, 1)
        }
    }

    private fun clickDistricts() {
        if (bRegion != "") {
            val s = bRegion.trim().replace("'", "").replace(" ", "").toLowerCase(Locale.ROOT)
            val list = resources.getStringArray(this.resIdByName(s, "array")).toList()
            showBottomSheetDialog(list, 2)
        }
    }

    private fun datePicker() {
        val endDate = Calendar.getInstance()
        endDate[Calendar.YEAR] = 2015
        val startDate = Calendar.getInstance()
        startDate[Calendar.YEAR] = 1950
        val selectedDate = Calendar.getInstance()
        selectedDate[Calendar.YEAR] = 2000
        val builder = SlideDatePickerDialog.Builder()
        builder.setEndDate(endDate)
        builder.setStartDate(startDate)
        builder.setPreselectedDate(selectedDate)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            builder.setThemeColor(getColor(R.color.main))
        }
        val dialog: SlideDatePickerDialog = builder.build()
        dialog.show(supportFragmentManager, "Dialog")
    }

    private fun showBottomSheetDialog(list: List<String>, type: Int) {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setContentView(R.layout.bottomsheetdialog)
        val rv = bottomSheetDialog.findViewById<RecyclerView>(R.id.rvLocation)!!
        val title = bottomSheetDialog.findViewById<TextView>(R.id.textView2)!!
        val adapter = LocationAdapter()
        rv.layoutManager = LinearLayoutManager(this)
        adapter.submitList(list)
        if (type == 1) {
            title.text = "Viloyatni tanlang"
        } else {
            title.text = "Tumanni tanlang"
        }
        rv.adapter = adapter
        adapter.setListener {
            when (type) {
                1 -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        runOnUiThread {
                            binding.home.text = "$it,"
                        }
                        bRegion = it
                        delay(200)
                        clickDistricts()
                    }
                }

                2 -> {
                    binding.home.apply {
                        text = binding.home.text.toString() + " " + it
                        setTextColor(resources.getColor(R.color.black))
                        error = null
                    }
                }
            }
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    private fun saveInfo() {
        binding.name.apply {
            if (text.toString().trim().isEmpty()) {
                error = "Ismingizni kiriting"
                return
            }
        }
        binding.surname.apply {
            if (text.toString().trim().isEmpty()) {
                error = "Familiyangizni kiriting"
                return
            }
        }
        binding.birthday.apply {
            if (text.toString() == "") {
                error = "Tug'ilgan sanangizni kiriting"
                return
            }
        }
        binding.home.apply {
            if (text.toString() == "") {
                error = "Tug'ilgan joyingizni kiriting"
                return
            }
        }
        val name = binding.name.text.toString().trim()
        val surname = binding.surname.text.toString().trim()
        FirebaseRemote.getInstance().createUser(
            User(
                name,
                surname,
                binding.birthday.text.toString(),
                binding.number.text.toString(),
                binding.home.text.toString()
            )
        ) {
            startActivity(Intent(this, SplashActivity::class.java))
            storage.fullName = "$name $surname"
            storage.settings = true
            finishAffinity()
        }
    }

    override fun onPositiveClick(day: Int, month: Int, year: Int, calendar: Calendar) {
        val format = SimpleDateFormat("dd.MM.yyyy", Locale("uz", "UZ"))
        binding.birthday.text = format.format(calendar.time)
        binding.birthday.error = null
    }
}