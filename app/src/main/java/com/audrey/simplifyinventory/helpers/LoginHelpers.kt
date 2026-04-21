package com.audrey.simplifyinventory.helpers

import android.content.Context
import android.content.SharedPreferences
import android.telephony.SmsManager
import androidx.core.content.edit
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import com.audrey.simplifyinventory.AppDatabase
import com.audrey.simplifyinventory.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.mindrot.jbcrypt.BCrypt

object LoginHelpers {
    private const val loginPrefs = "LoginPrefs"
    private const val loggedInKey = "isLoggedIn"
    private const val loggedInUserKey = "loggedInUser"

    private const val loggedInUserIdKey = "loggedInUserId"

    fun isLoggedIn(context: Context): Boolean {
        return getLoginPrefs(context)
            .getBoolean(loggedInKey, false)
    }
    fun getLoggedInUser(context: Context): String? {
        return getLoginPrefs(context)
            .getString(loggedInUserKey, "")
    }

    fun getLoggedInUserId(context: Context): Int {
        return getLoginPrefs(context)
            .getInt(loggedInUserIdKey, -1)
    }

    fun logIn(context: Context, username: String, password: String): Boolean {
        val db = AppDatabase.getDatabase(context)
        val user = runBlocking { db.userDao().getUserByName(username) }
        val loggedIn = runBlocking {
            if (user == null) {
                return@runBlocking false
            } else if (!BCrypt.checkpw(password, user.passwordHash)) {
                return@runBlocking false
            }
            return@runBlocking true
        }
        if (loggedIn && user != null) {
            getLoginPrefs(context).edit(commit = true) { putBoolean(loggedInKey, true) }
            getLoginPrefs(context).edit(commit = true) { putString(loggedInUserKey, username) }
            getLoginPrefs(context).edit(commit = true) { putInt(loggedInUserIdKey, user.id)}
        }
        return loggedIn
    }

    fun createUser(
        context: Context,
        username: String,
        password: String,
        email: String?
    ) {
        val user = User(
            username = username,
            passwordHash = password,
            email = email,
            createdAt = System.currentTimeMillis(),
            isActive = true
        )
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(context)
            db.userDao().insertUser(user)
        }
        getLoginPrefs(context).edit(commit = true) {
            putBoolean(loggedInKey, true)
            putString(loggedInUserKey, username)
        }
    }

    fun logOut(context: Context) {
        getLoginPrefs(context).edit(commit = true) { putBoolean(loggedInKey, false) }
        getLoginPrefs(context).edit(commit = true) { putString(loggedInUserKey, "") }
    }

    private fun getLoginPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(loginPrefs, Context.MODE_PRIVATE)
    }

    fun sendSMSAlert(context: Context, message: String) {
        try {
            val smsManager: SmsManager = context.getSystemService(SmsManager::class.java)
            smsManager.sendTextMessage("5551234567", null, message, null, null)
        } catch (e: Exception) {

        }
    }
}









