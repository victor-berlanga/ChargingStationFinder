package victor.berlanga.chargingstationfinder

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import victor.berlanga.chargingstationfinder.databinding.ActivityRegisterBinding
import victor.berlanga.chargingstationfinder.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = getSharedPreferences("user_session", MODE_PRIVATE)
        if (preferences.getBoolean("registered", false)) {
            openMainActivity()
            return
        }

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnContinue.setOnClickListener {
            registerViewModel.saveUser(
                binding.edtName.text.toString(),
                binding.edtEmail.text.toString()
            )
        }

        registerViewModel.errorMessage.observe(this) { message ->
            binding.txtRegisterMessage.text = message.orEmpty()
        }

        registerViewModel.registrationSaved.observe(this) { saved ->
            if (saved) {
                preferences.edit().putBoolean("registered", true).apply()
                openMainActivity()
            }
        }
    }

    private fun openMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
