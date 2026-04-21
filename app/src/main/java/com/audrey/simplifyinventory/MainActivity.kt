package com.audrey.simplifyinventory

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.audrey.simplifyinventory.helpers.LoginHelpers
import java.util.jar.Manifest

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        granted: Boolean ->
            Log.d("Permission", "SMS Permission granted: $granted")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (!hasSMSPermission()) requestSMSPermission()
    }

    override fun onResume() {
        super.onResume()
        navigateToPage()
    }

    private fun navigateToPage() {
        val loggedIn = LoginHelpers.isLoggedIn(this)
        if (loggedIn) {
            //go to grid
            startActivity(Intent(this, InventoryActivity::class.java))
        } else {
            // go to login
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun requestSMSPermission() {
        requestPermissionLauncher.launch(android.Manifest.permission.SEND_SMS)
    }

    private fun hasSMSPermission(): Boolean {
       return ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
    }
}

