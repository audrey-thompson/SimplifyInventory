package com.audrey.simplifyinventory

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.audrey.simplifyinventory.dao.InventoryItemDao
import com.audrey.simplifyinventory.dao.ItemTypeDao
import com.audrey.simplifyinventory.dao.RfidDeviceDao
import com.audrey.simplifyinventory.dao.RfidTagDao
import com.audrey.simplifyinventory.dao.UserDao
import com.audrey.simplifyinventory.entity.InventoryItem
import com.audrey.simplifyinventory.entity.ItemType
import com.audrey.simplifyinventory.entity.RfidDevice
import com.audrey.simplifyinventory.entity.RfidTag
import com.audrey.simplifyinventory.entity.User

@Database(
    entities = [
        User::class,
        RfidDevice::class,
        ItemType::class,
        InventoryItem::class,
        RfidTag::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun rfidDeviceDao(): RfidDeviceDao
    abstract fun itemTypeDao(): ItemTypeDao
    abstract fun inventoryItemDao(): InventoryItemDao
    abstract fun rfidTagDao(): RfidTagDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "simplify_inventory_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}