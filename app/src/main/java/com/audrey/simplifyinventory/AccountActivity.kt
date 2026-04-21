package com.audrey.simplifyinventory

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.audrey.simplifyinventory.helpers.LoginHelpers

class AccountActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        val username = findViewById<TextView>(R.id.usernameText)
        username.text = LoginHelpers.getLoggedInUser(this)

        findViewById<Button>(R.id.nav_home).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        findViewById<Button>(R.id.nav_inventory).setOnClickListener {
            startActivity(Intent(this, InventoryActivity::class.java))
        }

        findViewById<Button>(R.id.nav_shop).setOnClickListener {
            startActivity(Intent(this, ShopActivity::class.java))
        }

        findViewById<Button>(R.id.nav_settings).setOnClickListener {
        }

        setupRow(R.id.passwordRow, "Password")
        setupRow(R.id.notificationsRow, "Notifications")
        setupRow(R.id.privacyRow, "Privacy")
        setupRow(R.id.securityRow, "Security")
        setupRow(R.id.aboutRow, "About")

        findViewById<View>(R.id.logoutRow).setOnClickListener {
            LoginHelpers.logOut(this)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun setupRow(rowId: Int, label: String) {
        findViewById<View>(rowId).setOnClickListener {
            Toast.makeText(this, "$label TO DO", Toast.LENGTH_SHORT).show()
        }
    }
}