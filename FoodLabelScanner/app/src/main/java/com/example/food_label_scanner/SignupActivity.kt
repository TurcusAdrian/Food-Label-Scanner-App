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
    private lateinit var databaseHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DBHelper(this)

        binding.signupButton.setOnClickListener{
            val signupUsername = binding.signupUsername.text.toString()
            val signupPassword = binding.signupPassword.text.toString()
            signupDatabase(signupUsername,signupPassword)
        }

        binding.loginRedirect.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signupDatabase(username:String, password:String){
        val insertedRowId = databaseHelper.insertUser(username,password)
        if(insertedRowId != -1L){
            Toast.makeText(this,"SignUp Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            Toast.makeText(this, "SignUp failed", Toast.LENGTH_SHORT).show()
        }
    }


}