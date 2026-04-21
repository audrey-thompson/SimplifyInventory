package com.audrey.simplifyinventory.entity
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["username"], unique = true)]
)
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val passwordHash: String,
    val email: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)