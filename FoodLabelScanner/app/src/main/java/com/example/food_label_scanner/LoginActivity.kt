package com.example.food_label_scanner

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.food_label_scanner.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var dataBaseHelper: DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataBaseHelper = DataBaseHelper(this)

        binding.loginButton.setOnClickListener{
            val loginemail = binding.loginEmail.text.toString()
            val loginpassword = binding.loginPassword.text.toString()
        }

        binding.signupRedirect.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun loginDatabase(email : String, password : String){
        val userExists = dataBaseHelper.readUser(email,password)

        if(userExists){
            Toast.makeText(this,"Login Successful",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this,"Login failed", Toast.LENGTH_SHORT).show()
        }

    }
}