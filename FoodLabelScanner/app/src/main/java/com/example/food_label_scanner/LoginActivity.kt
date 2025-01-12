package com.example.food_label_scanner


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.food_label_scanner.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var databaseHelper: DBHelper
    private var id: Int ?=null
    // private val viewModel: UserViewModel by viewModels()

    //    fun setUserId(userId: Int) {
//        viewModel.userId = userId
//    }
//
//    fun getUserId(): Int? {
//        return viewModel.userId
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DBHelper(this)
        //id = intent.getIntExtra("id", -1)
        binding.loginButton.setOnClickListener{
            val loginUsername = binding.loginUsername.text.toString()
            val loginPassword = binding.loginPassword.text.toString()
            loginDatabase(loginUsername, loginPassword)
        }

        binding.signupRedirect.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun loginDatabase(username:String, password:String){
        val userExists = databaseHelper.readUser(username, password)
        if(userExists){
            id= databaseHelper.getUserId(username)
            //viewModel.userId=id
            Toast.makeText(this,"Login Successful", Toast.LENGTH_SHORT).show()
            val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putInt("userId", id!!)
                putString("user_email", username)
                apply()
            }
            val userId= databaseHelper.getUserId(username) // Get the selected room
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

}