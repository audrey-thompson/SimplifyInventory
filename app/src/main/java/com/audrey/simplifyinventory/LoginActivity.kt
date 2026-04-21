package com.audrey.simplifyinventory

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.audrey.simplifyinventory.helpers.LoginHelpers

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
    }

    fun Login(view: View) {
        val username = findViewById<EditText>(R.id.username).text.toString().trim()
        val password = findViewById<EditText>(R.id.password).text.toString()

        val success = LoginHelpers.logIn(this, username, password)

        if (!success) {
            findViewById<TextView>(R.id.result).text = "Login Failed!"
        } else {
            finish()
        }
    }

    fun CreateUser(view: View) {
        startActivity(Intent(this, MakeAccountActivity::class.java))

    }
}