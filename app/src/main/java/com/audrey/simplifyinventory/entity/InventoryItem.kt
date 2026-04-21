package com.audrey.simplifyinventory.entity
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "inventory_items",
    foreignKeys = [
        ForeignKey( // easier queries with direct ownership instead of going through ItemType
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ItemType::class, // delete if user deletes item type
            parentColumns = ["id"],
            childColumns = ["itemTypeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId"), Index("itemTypeId")]
)
data class InventoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val itemTypeId: Int,
    val customName: String? = null,  // for individual items (ex: pink uggs)
    val quantity: Int = 1, // add one at a time to connect RFID
    val photoUri: String? = null,
    val expirationDate: Long? = null,
    val purchaseDate: Long? = null,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(), // for ML recommendations
    val updatedAt: Long = System.currentTimeMillis()  // for ML recommendations
)