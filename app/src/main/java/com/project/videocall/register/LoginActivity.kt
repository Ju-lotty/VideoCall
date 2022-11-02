package com.project.videocall.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.project.videocall.MainActivity
import com.project.videocall.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        buttoninit()
    }




    private fun buttoninit() = with(binding) {
        loginButton.setOnClickListener {
            val email = emailValue.text.toString()
            val password = passwordValue.text.toString()
            lifecycleScope.launch(Dispatchers.IO) {
                signInWithEmailAndPassword(mAuth, email, password)
            }
        }
    }

    private suspend fun signInWithEmailAndPassword(firebaseAuth: FirebaseAuth, email: String, password: String): AuthResult? {

        return try {

            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()

            withContext(Dispatchers.Main) {
                Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                Log.d("결과!!", "${result.user!!.email}")
            }
            result
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@LoginActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                Log.d("결과", "${e.message}")
            }
            null
        }
    }
}