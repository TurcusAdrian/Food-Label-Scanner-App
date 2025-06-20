package com.example.food_label_scanner


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.food_label_scanner.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var databaseHelper: DBHelper
    private var id: Int ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DBHelper(this)

        if (isUserLoggedIn()) {
            navigateToMainActivity()
        }

        binding.loginButton.setOnClickListener{
            val loginUsername = binding.loginUsername.text.toString()
            val loginPassword = binding.loginPassword.text.toString()
            val keepLoggedIn = binding.keepLoggedInCheckBox.isChecked
            loginDatabase(loginUsername, loginPassword, keepLoggedIn)
        }

        binding.signupRedirect.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun loginDatabase(username:String, password:String, keepLoggedIn: Boolean){
        val userExists = databaseHelper.readUser(username, password)
        if(userExists){
            id = databaseHelper.getUserId(username)
            Toast.makeText(this,"Login Successful", Toast.LENGTH_SHORT).show()
            val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

            saveLoginState(username, password, keepLoggedIn)

            with(sharedPref.edit()) {
                putInt("userId", id!!)
                putString("username", username)
                apply()
            }
            val userId = databaseHelper.getUserId(username)
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("userId", userId)
                Log.d("UserId", "Retrieved userId: $userId")
            }
            startActivity(intent)
            finish()
        }
        else{
            Toast.makeText(this, "Login failed",Toast.LENGTH_SHORT).show()
        }
    }


    private fun saveLoginState(username: String, password: String, keepLoggedIn: Boolean) {
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("keepLoggedIn", keepLoggedIn)
            putString("username", username)
            putString("user_password", password)
            apply()
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("keepLoggedIn", false)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("userId", id)
        }
        startActivity(intent)
        finish()
    }

}