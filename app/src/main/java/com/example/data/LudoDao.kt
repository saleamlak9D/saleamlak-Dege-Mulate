package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LudoDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfileFlow(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(profile: UserProfile)

    @Query("SELECT * FROM match_history ORDER BY timestamp DESC")
    fun getMatchHistoryFlow(): Flow<List<MatchHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatchHistory(match: MatchHistory)

    @Query("SELECT * FROM unlocked_skin")
    fun getUnlockedSkinsFlow(): Flow<List<UnlockedSkin>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnlockedSkin(skin: UnlockedSkin)

    @Query("UPDATE unlocked_skin SET isEquipped = 0 WHERE skinType = :skinType")
    suspend fun unequipAllSkinsOfType(skinType: String)

    @Query("UPDATE unlocked_skin SET isEquipped = 1 WHERE skinType = :skinType AND skinName = :skinName")
    suspend fun equipSkin(skinType: String, skinName: String)
}
