package com.audrey.simplifyinventory.entity
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "item_types",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId", "name"], unique = true)] // prevent duplicate item types
)
data class ItemType(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val name: String,
    val description: String? = null,
    val defaultPhotoUri: String? = null,
    val category: String? = null
)