package com.project.videocall.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.videocall.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerinit()
        logininit()
    }

    private fun registerinit() = with(binding) {
        registerButton.setOnClickListener {
            val intent = Intent(this@StartActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun logininit() = with(binding) {
        gotoLogin.setOnClickListener {
            val intent = Intent(this@StartActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}