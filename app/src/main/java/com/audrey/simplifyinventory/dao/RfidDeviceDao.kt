package com.audrey.simplifyinventory.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.audrey.simplifyinventory.entity.RfidDevice

@Dao
interface RfidDeviceDao {

    // create a new RFID receiver with a unique ID
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDevice(device: RfidDevice): Long

    // get a list of the user's devices
    @Query("SELECT * FROM rfid_devices WHERE userId = :userId")
    suspend fun getDevicesForUser(userId: Int): List<RfidDevice>

    // look up RFID device by ID
    @Query("SELECT * FROM rfid_devices WHERE hardwareId = :id LIMIT 1")
    suspend fun getDeviceByIdentifier(id: String): RfidDevice?

    // record device's activity
    @Query("""
        UPDATE rfid_devices 
        SET lastSeenAt = :time 
        WHERE hardwareId = :id
    """)
    suspend fun updateLastSeen(
        id: String,
        time: Long = System.currentTimeMillis()
    )

    // enable or disable scanner
    @Query("""
        UPDATE rfid_devices 
        SET isActive = :active 
        WHERE hardwareId = :id
    """)
    suspend fun setDeviceActive(id: String, active: Boolean)
}