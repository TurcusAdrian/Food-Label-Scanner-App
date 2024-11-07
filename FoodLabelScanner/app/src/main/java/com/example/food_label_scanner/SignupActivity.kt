package com.example.food_label_scanner

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.food_label_scanner.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySignupBinding
    private lateinit var dataBaseHelper: DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_signup)
        setContentView(binding.root)

        dataBaseHelper = DataBaseHelper(this)

        binding.signupButton.setOnClickListener{
            val signupemail = binding.signupEmail.text.toString()
            val signuppassword = binding.signupPassword.text.toString()
            signUpDatabase(signupemail,signuppassword)

        }

        binding.loginRedirect.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signUpDatabase(email: String, password: String){

        val insertedRowId = dataBaseHelper.insertUser(email, password)

        if(insertedRowId != -1L){
            Toast.makeText(this,"Signup Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this,"Signup Failed", Toast.LENGTH_SHORT).show()

        }
    }

}