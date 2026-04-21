package com.audrey.simplifyinventory.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.audrey.simplifyinventory.entity.ItemType

@Dao
interface ItemTypeDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertItemType(itemType: ItemType): Long

    @Update
    suspend fun updateItemType(itemType: ItemType)

    @Delete
    suspend fun deleteItemType(itemType: ItemType)

    // get a list of the user's items (type, not individual)
    @Query("SELECT * FROM item_types WHERE userId = :userId")
    suspend fun getItemTypesForUser(userId: Int): List<ItemType>

    // search an item type by ID
    @Query("SELECT * FROM item_types WHERE id = :id LIMIT 1")
    suspend fun getItemTypeById(id: Int): ItemType?

    // search an item type by name
    @Query("SELECT * FROM item_types WHERE name = :name LIMIT 1")
    suspend fun getItemTypeByName(name: String): ItemType?

    // get count
    @Query("""
    SELECT SUM(quantity) 
    FROM inventory_items 
    WHERE itemTypeId = :itemTypeId AND userId = :userId
    """)

    suspend fun getItemCount(userId: Int, itemTypeId: Int): Int?

    @Query("""
      SELECT it.* FROM item_types it
      LEFT JOIN (SELECT itemTypeId, SUM(quantity) as total
                 FROM inventory_items WHERE userId = :userId GROUP BY itemTypeId) inv
      ON it.id = inv.itemTypeId
      WHERE it.userId = :userId
      ORDER BY inv.total DESC
  """)
    suspend fun getItemTypesSortedByQuantity(userId: Int): List<ItemType>

    @Query("SELECT * FROM item_types WHERE userId = :userId ORDER BY name ASC")
    suspend fun getItemTypesSortedByName(userId: Int): List<ItemType>

    @Query("""
      SELECT it.* FROM item_types it
      LEFT JOIN (SELECT itemTypeId, MIN(createdAt) as firstAdded
                 FROM inventory_items WHERE userId = :userId GROUP BY itemTypeId) inv
      ON it.id = inv.itemTypeId
      WHERE it.userId = :userId
      ORDER BY inv.firstAdded DESC
  """)
    suspend fun getItemTypesSortedByRecentlyAdded(userId: Int): List<ItemType>
}