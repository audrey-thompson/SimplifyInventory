package com.audrey.simplifyinventory.entity
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "rfid_devices",
    foreignKeys = [
        ForeignKey( // RFID receivers are owned by a user
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE // delete scanner data when owner account is deleted
        )
    ],
    indices = [Index("userId"), Index(value = ["hardwareId"], unique = true)]
)
data class RfidDevice(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val hardwareId: String,
    val nickname: String? = null,
    val deviceType: String? = null,
    val isActive: Boolean = true, // turn receiver on and off
    val lastSeenAt: Long? = null // check connectivity or activity
)