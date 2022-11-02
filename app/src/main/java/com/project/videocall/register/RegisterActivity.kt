package com.project.videocall.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.project.videocall.databinding.ActivityRegisterBinding
import com.project.videocall.dto.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    var emailCheck = false
    var passwordCheck = false

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        checkinit()
        buttoninit()
    }


    private fun checkinit() = with(binding) {
        emailEditTextView.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.contains("@", true) || s.contains(".com", true)) {
                    emailCheckTextView.visibility = View.GONE
                    emailCheck = true
                    if (passwordCheck) {
                        completeRegister.isEnabled = true
                    }
                } else {
                    emailCheckTextView.visibility = View.VISIBLE
                    completeRegister.isEnabled = false
                }
            }
        })

        passwordEditTextView.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.length >= 8) {
                    passwordCheckTextView.visibility = View.GONE
                    passwordCheck = true

                    if (emailCheck) {
                        completeRegister.isEnabled = true
                    }
                } else {
                    passwordCheckTextView.visibility = View.VISIBLE
                    completeRegister.isEnabled = false
                }
            }
        })
    }

    private fun buttoninit() = with(binding) {
        completeRegister.setOnClickListener {
            val email = emailEditTextView.text.toString()
            val password = passwordEditTextView.text.toString()
            lifecycleScope.launch(Dispatchers.IO) {
                singUpEmailPassword(mAuth, email, password)

            }
        }
    }

    private suspend fun singUpEmailPassword(firebaseAuth: FirebaseAuth, email: String, password: String): AuthResult? {

        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = User(email)
            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(user).await()

            withContext(Dispatchers.Main) {
                Toast.makeText(this@RegisterActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity, StartActivity::class.java))
                Log.d("결과!!", "${result.user!!.email}")
            }
            result
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@RegisterActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                Log.d("결과", "${e.message}")
            }
            null
        }
    }
}