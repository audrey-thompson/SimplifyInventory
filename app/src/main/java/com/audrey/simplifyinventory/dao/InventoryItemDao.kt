package com.audrey.simplifyinventory.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.audrey.simplifyinventory.entity.InventoryItem

@Dao
interface InventoryItemDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertItem(item: InventoryItem): Long

    @Update
    suspend fun updateItem(item: InventoryItem)

    @Delete
    suspend fun deleteItem(item: InventoryItem)

    // get list of the user's items
    @Query("SELECT * FROM inventory_items WHERE userId = :userId")
    suspend fun getItemsForUser(userId: Int): List<InventoryItem>

    // search for item by ID
    @Query("SELECT * FROM inventory_items WHERE id = :itemId LIMIT 1")
    suspend fun getItemById(itemId: Int): InventoryItem?

    // get a list of a user's items, but only for a certain item type
    @Query("""
        SELECT * FROM inventory_items 
        WHERE itemTypeId = :itemTypeId AND userId = :userId
    """)
    suspend fun getItemsByType(userId: Int, itemTypeId: Int): List<InventoryItem>
}