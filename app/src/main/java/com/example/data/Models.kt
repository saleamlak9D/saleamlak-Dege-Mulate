package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val username: String = "LudoMaster",
    val avatarResName: String = "avatar_1",
    val level: Int = 1,
    val xp: Int = 150,
    val coins: Int = 10000,
    val gems: Int = 150,
    val wins: Int = 12,
    val losses: Int = 5,
    val winRate: Float = 70.5f,
    val country: String = "United States",
    val language: String = "English",
    val isSoundEnabled: Boolean = true,
    val isMusicEnabled: Boolean = true,
    val isNotificationsEnabled: Boolean = true
) {
    val totalMatches: Int get() = wins + losses
}

@Entity(tableName = "match_history")
data class MatchHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val gameMode: String,
    val result: String, // "VICTORY" or "DEFEAT"
    val coinsGained: Int,
    val xpGained: Int,
    val duration: String
)

@Entity(tableName = "unlocked_skin")
data class UnlockedSkin(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val skinType: String, // "dice", "board"
    val skinName: String,
    val isEquipped: Boolean = false
)
