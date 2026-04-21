package com.audrey.simplifyinventory

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.audrey.simplifyinventory.dao.UserDao
import com.audrey.simplifyinventory.entity.User
import com.audrey.simplifyinventory.helpers.LoginHelpers
import org.mindrot.jbcrypt.BCrypt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MakeAccountActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_makeaccount)
    }

    fun MakeAccount(view: View) {
        val username = findViewById<TextView>(R.id.username).text.toString().trim()
        val password = findViewById<TextView>(R.id.password).text.toString()
        val email = "testemail@test.com"

        if(username.isBlank() || password.isBlank()) {
            return
        }

        val passwordHash = BCrypt.hashpw(password, BCrypt.gensalt())
        val user = User(username = username, passwordHash = passwordHash, email = email)

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            db.userDao().insertUser(user)
            finish()
        }

    }
}