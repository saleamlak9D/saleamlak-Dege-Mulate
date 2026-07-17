package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

class LudoRepository(private val ludoDao: LudoDao) {

    val userProfile: Flow<UserProfile?> = ludoDao.getUserProfileFlow()
        .onStart {
            // Check if profile exists and insert a default one if null,
            // we will handle this initialization inside the ViewModel to keep it clean.
        }

    val matchHistory: Flow<List<MatchHistory>> = ludoDao.getMatchHistoryFlow()

    val unlockedSkins: Flow<List<UnlockedSkin>> = ludoDao.getUnlockedSkinsFlow()

    suspend fun saveUserProfile(profile: UserProfile) {
        ludoDao.insertUserProfile(profile)
    }

    suspend fun addMatchHistory(match: MatchHistory) {
        ludoDao.insertMatchHistory(match)
    }

    suspend fun addUnlockedSkin(skin: UnlockedSkin) {
        ludoDao.insertUnlockedSkin(skin)
    }

    suspend fun equipSkin(skinType: String, skinName: String) {
        ludoDao.unequipAllSkinsOfType(skinType)
        ludoDao.equipSkin(skinType, skinName)
    }
}
