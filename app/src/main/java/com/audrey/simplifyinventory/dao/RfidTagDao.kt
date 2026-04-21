package com.audrey.simplifyinventory.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.audrey.simplifyinventory.entity.RfidTag

@Dao
interface RfidTagDao {

    // register a new tag (force it to be unique)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTag(tag: RfidTag): Long

    // get tag by ID (when it's scanned)
    @Query("SELECT * FROM rfid_tags WHERE tagUid = :uid LIMIT 1")
    suspend fun getTagByUid(uid: String): RfidTag?

    // list a user's tags
    @Query("SELECT * FROM rfid_tags WHERE userId = :userId")
    suspend fun getTagsForUser(userId: Int): List<RfidTag>

    // assign tag to item & activate tag
    @Query("""
        UPDATE rfid_tags 
        SET inventoryItemId = :itemId, assignedAt = :time, isActive = 1
        WHERE tagUid = :uid
    """)
    suspend fun assignTagToItem(uid: String, itemId: Int, time: Long = System.currentTimeMillis())

    // unassign tag from item & deactivate tag
    @Query("""
        UPDATE rfid_tags 
        SET inventoryItemId = NULL, isActive = 0
        WHERE tagUid = :uid
    """)
    suspend fun unassignTag(uid: String)
}