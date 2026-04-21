package com.audrey.simplifyinventory.entity
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "rfid_tags",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = InventoryItem::class,
            parentColumns = ["id"],
            childColumns = ["inventoryItemId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("userId"),
        Index("inventoryItemId"),
        Index(value = ["tagUid"], unique = true)
    ]
)
data class RfidTag(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val tagUid: String,
    val inventoryItemId: Int? = null,
    val assignedAt: Long? = null,
    val isActive: Boolean = true // ignore tag? (option for inactive until reassigned)
)