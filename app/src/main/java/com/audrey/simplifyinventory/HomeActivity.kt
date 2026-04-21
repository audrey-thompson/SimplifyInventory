package com.audrey.simplifyinventory

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import com.audrey.simplifyinventory.helpers.LoginHelpers

class HomeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        findViewById<Button>(R.id.nav_home).setOnClickListener {
        }

        findViewById<Button>(R.id.nav_inventory).setOnClickListener {
            startActivity(Intent(this, InventoryActivity::class.java))
        }

        findViewById<Button>(R.id.nav_shop).setOnClickListener {
            startActivity(Intent(this, ShopActivity::class.java))
        }

        findViewById<Button>(R.id.nav_settings).setOnClickListener {
            startActivity(Intent(this, AccountActivity::class.java))
        }

    }

}