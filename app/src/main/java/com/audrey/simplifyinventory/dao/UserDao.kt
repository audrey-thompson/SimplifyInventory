package com.audrey.simplifyinventory.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.audrey.simplifyinventory.entity.User

@Dao
interface UserDao {

    // create new user and force unique username
    @Insert(onConflict = OnConflictStrategy.ABORT) // unique usernames only
    suspend fun insertUser(user: User)

    // search user by username
    @Query("SELECT * FROM users WHERE username = :name LIMIT 1")
    suspend fun getUserByName(name: String): User?

    @Query("SELECT * FROM users")
    suspend fun getUsers(): List<User>
}