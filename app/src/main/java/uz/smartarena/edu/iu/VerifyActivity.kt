package uz.smartarena.edu.iu

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.util.Log
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.widget.doOnTextChanged
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import uz.smartarena.edu.R
import uz.smartarena.edu.data.local.EncryptedLocalStorage
import uz.smartarena.edu.data.remote.FirebaseRemote
import uz.smartarena.edu.databinding.ActivityVerifyBinding
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class VerifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyBinding
    private val storage = EncryptedLocalStorage.getInstance()
    private val remote = FirebaseRemote.getInstance()
    private val pins = ArrayList<EditText>()
    private lateinit var number: String
    private lateinit var numberF: String
    private val auth = FirebaseAuth.getInstance()
    private lateinit var codeBySystem: String
    private lateinit var countDownTimer: CountDownTimer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        number = storage.number
        window.statusBarColor = resources.getColor(R.color.main)
        window.navigationBarColor = resources.getColor(R.color.main)
        numberF = storage.numberF

        sendVerificationCode(number)
        loadViews()
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)
            .setTimeout(90L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            Log.d("TTT", "onCodeSent: $p0 \n $p1")
            codeBySystem = p0
        }

        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            val code = p0.smsCode
            if (code != null) {
                setCode(code)
            }
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Toast.makeText(this@VerifyActivity, "${p0.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verifyCode(code: String) {
        if (this::codeBySystem.isInitialized) {
            val phoneAuthCredential = PhoneAuthProvider.getCredential(codeBySystem, code)
            signInUseCredential(phoneAuthCredential)
        }
    }

    private fun signInUseCredential(phoneAuthCredential: PhoneAuthCredential) {
        auth.signInWithCredential(phoneAuthCredential)
            .addOnSuccessListener {
                Toast.makeText(this, "Muvaffaqiyatli", Toast.LENGTH_SHORT).show()
                remote.checkUser(auth.uid!!) { hasUser ->
                    storage.uid = auth.uid!!
                    if (hasUser) {
                        EncryptedLocalStorage.getInstance().settings = true
                        val intent = Intent(this, SplashActivity::class.java)
                        finishAffinity()
                        startActivity(intent)
                    } else {
                        val setIntent = Intent(this, RegistrationActivity::class.java)
                        finishAffinity()
                        startActivity(setIntent)
                    }
                }

            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun setCode(code: String) {
        val executor = Executors.newSingleThreadExecutor()
        for (i in 0..5) {
            executor.execute {
                Thread.sleep(200)
                runOnUiThread {
                    pins[i].setText(code[i].toString())
                    if (i == 5) {
                        verifyCode(getPin())
                        checkEnter()
                    }
                }
            }
        }
    }

    private fun loadTimer() {
        countDownTimer = object : CountDownTimer(90000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var second = millisUntilFinished / 1000
                val minut = second / 60
                second %= 60
                binding.count.text = String.format("%02d:%02d", minut, second)
            }

            override fun onFinish() {
                binding.resend.apply {
                    isClickable = true
                    setTextColor(resources.getColor(R.color.main))
                }
                binding.count.setTextColor(resources.getColor(R.color.seriy))
            }
        }
        countDownTimer.start()
        binding.count.setTextColor(resources.getColor(R.color.main))
    }

    private fun clickListeners() {
        binding.back.setOnClickListener { onBackPressed() }
        binding.enter.setOnClickListener { enter() }
        binding.resend.apply {
            setOnClickListener {
                isClickable = false
                setTextColor(resources.getColor(R.color.seriy))
                sendVerificationCode(number)
                countDownTimer.start()
                binding.count.setTextColor(resources.getColor(R.color.main))
            }
        }
    }

    private fun numberTextChanged() {
        for (i in 0..5) {
            pins[i].doOnTextChanged { text, start, before, count ->
                if (count == 1 && i != 5) pins[i + 1].apply {
                    requestFocus()
                    setSelection(length())
                }
                if (count == 0 && i != 0) pins[i - 1].apply {
                    requestFocus()
                    setSelection(length())
                }
                if (i == 5 && count == 1) {
                    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(pins[5].windowToken, 0)
                }
                if (count == 1) binding.pin.getChildAt(i).setBackgroundResource(R.drawable.number_oval2)
                else binding.pin.getChildAt(i).setBackgroundResource(R.drawable.number_oval)
                checkEnter()
            }
        }
    }

    private fun checkEnter() {
        if (getPin().length == 6) {
            binding.enter.isClickable = true
            binding.enter.setBackgroundResource(R.drawable.back_buttons)
            binding.next.setTextColor(resources.getColor(R.color.white))
        } else {
            binding.enter.isClickable = false
            binding.enter.setBackgroundResource(R.drawable.back_buttons1)
            binding.next.setTextColor(resources.getColor(R.color.seriy))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadViews() {
        loadTimer()
        clickListeners()
        val text = "<b><font color=#000000>$numberF</font></b> " + getString(R.string.telefon_raq)
        binding.telefonRaq.text = Html.fromHtml(text)
        for (a in binding.pin.children) {
            pins.add((a as ViewGroup).getChildAt(0) as EditText)
        }
        pins[0].requestFocus()
        numberTextChanged()
    }

    private fun enter() {
        verifyCode(getPin())
    }

    private fun getPin(): String {
        val string = StringBuilder()
        for (i in 0..5) {
            string.append(pins[i].text.toString())
        }
        return string.toString()
    }
}