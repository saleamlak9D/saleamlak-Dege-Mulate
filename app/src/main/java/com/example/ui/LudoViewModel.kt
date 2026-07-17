package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

sealed class Screen {
    object Splash : Screen()
    object Onboarding1 : Screen()
    object Onboarding2 : Screen()
    object Onboarding3 : Screen()
    object Auth : Screen()
    object Dashboard : Screen()
    object GameSetup : Screen()
    object Matchmaking : Screen()
    object RoomSetup : Screen()
    object Gameplay : Screen()
    object AfterMatch : Screen()
    object TournamentList : Screen()
    object TournamentBracket : Screen()
    object Leaderboard : Screen()
    object LuckySpin : Screen()
    object Store : Screen()
    object Wallet : Screen()
    object Friends : Screen()
    object Chat : Screen()
    object Settings : Screen()
    object HelpCenter : Screen()
    object ReportPlayer : Screen()
    object Privacy : Screen()
}

data class LudoPlayer(
    val name: String,
    val avatar: String,
    val color: String, // "RED", "GREEN", "YELLOW", "BLUE"
    val isAi: Boolean,
    val isFinished: Boolean = false
)

data class LudoToken(
    val id: Int, // 0, 1, 2, 3
    val color: String, // "RED", "GREEN", "YELLOW", "BLUE"
    val position: Int, // -1 = yard, 0..50 = track, 51..56 = home path, 57 = home (finished)
    val startOffset: Int, // offset to common board index
    val homeColorIndex: Int
)

data class LiveTournament(
    val id: Int,
    val title: String,
    val prizePool: String,
    val entryFee: String,
    val participantsCount: Int,
    val maxParticipants: Int,
    val timeRemaining: String,
    val currentRound: String, // "Quarter-Finals", "Semi-Finals", "Finals"
    val joined: Boolean = false
)

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val avatar: String,
    val country: String,
    val score: Int,
    val isCurrentUser: Boolean = false
)

data class MissionItem(
    val id: Int,
    val title: String,
    val reward: String,
    val progress: Float, // 0.0f to 1.0f
    val isClaimed: Boolean = false
)

data class ChatMessage(
    val sender: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isMe: Boolean = false
)

data class WalletTransaction(
    val id: String,
    val type: String, // "Deposit", "Withdrawal", "Reward", "Entry Fee"
    val amount: String,
    val date: String,
    val status: String // "Completed", "Pending", "Failed"
)

class LudoViewModel(application: Application) : AndroidViewModel(application) {

    private val database = LudoDatabase.getDatabase(application)
    private val repository = LudoRepository(database.ludoDao())

    // UI States
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Splash)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _profileState = MutableStateFlow<UserProfile>(UserProfile())
    val profileState: StateFlow<UserProfile> = _profileState.asStateFlow()

    private val _matchHistory = MutableStateFlow<List<MatchHistory>>(emptyList())
    val matchHistory: StateFlow<List<MatchHistory>> = _matchHistory.asStateFlow()

    private val _unlockedSkins = MutableStateFlow<List<UnlockedSkin>>(emptyList())
    val unlockedSkins: StateFlow<List<UnlockedSkin>> = _unlockedSkins.asStateFlow()

    // Interactive UI and Game State
    val soundEnabled = MutableStateFlow(true)
    val musicEnabled = MutableStateFlow(true)
    val notificationsEnabled = MutableStateFlow(true)

    // Matchmaking State
    val matchmakingTime = MutableStateFlow(0)
    val matchmakingEstimated = MutableStateFlow(12)
    val matchmakingFound = MutableStateFlow(false)

    // Lucky Spin State
    val isSpinning = MutableStateFlow(false)
    val lastSpinReward = MutableStateFlow<String?>(null)
    val dailyRewardsClaimed = MutableStateFlow(mutableSetOf<Int>()) // indexes 0..6

    // Gameplay States
    val activePlayers = MutableStateFlow<List<LudoPlayer>>(emptyList())
    val tokens = MutableStateFlow<List<LudoToken>>(emptyList())
    val currentPlayerIndex = MutableStateFlow(0)
    val diceResult = MutableStateFlow(1)
    val hasDiceRolled = MutableStateFlow(false)
    val isDiceRolling = MutableStateFlow(false)
    val winnerPlayer = MutableStateFlow<LudoPlayer?>(null)
    val turnTimer = MutableStateFlow(30)
    val matchWinnerCoins = MutableStateFlow(1200)
    val matchWinnerXp = MutableStateFlow(450)
    val diceTheme = MutableStateFlow("Classic")
    val boardTheme = MutableStateFlow("Classic Glow")

    // Notifications
    val notifications = MutableStateFlow(listOf(
        "Welcome to Deges Play! Collect your daily reward now.",
        "Your friend LudoChamp has sent you an invite.",
        "Weekly Tournament starts in 2 hours! Register now."
    ))

    // Active Setup Properties
    val selectedMode = MutableStateFlow("Classic Ludo") // "Classic Ludo", "Quick Match", "Practice Mode"
    val selectedPlayersCount = MutableStateFlow(4) // 2 or 4

    // Tournaments List
    val tournaments = MutableStateFlow(listOf(
        LiveTournament(1, "Grand Arena Championship", "50,000 Coins", "500 Coins", 94, 128, "3h 45m", "Quarter-Finals"),
        LiveTournament(2, "Weekend Clash Royale", "150 Gems", "2,000 Coins", 12, 32, "1d 2h", "Registration Open"),
        LiveTournament(3, "Speed Star Cup", "10,000 Coins", "100 Coins", 4, 16, "12m", "Semi-Finals"),
        LiveTournament(4, "Master Series Blitz", "500 Gems", "50 Gems", 44, 64, "1d 10h", "Registration Open")
    ))

    // Missions
    val missions = MutableStateFlow(listOf(
        MissionItem(1, "Win 2 Matches", "500 Coins", 0.5f),
        MissionItem(2, "Capture 3 Tokens", "10 Gems", 0.33f),
        MissionItem(3, "Roll a 6 five times", "200 Coins", 0.8f),
        MissionItem(4, "Play 1 Tournament", "1,000 Coins", 0.0f)
    ))

    // Leaderboards
    val leaderboardTab = MutableStateFlow("Global") // "Global", "Country", "Friends"
    val globalLeaderboard = MutableStateFlow(listOf(
        LeaderboardEntry(1, "LudoKing_99", "avatar_1", "India", 145200),
        LeaderboardEntry(2, "Valkyrie", "avatar_2", "Germany", 128400),
        LeaderboardEntry(3, "LudoMaster", "avatar_1", "United States", 98500, isCurrentUser = true),
        LeaderboardEntry(4, "GamerPro", "avatar_3", "Brazil", 87900),
        LeaderboardEntry(5, "DiceCrusher", "avatar_4", "Canada", 76300),
        LeaderboardEntry(6, "LuckyStriker", "avatar_5", "United Kingdom", 69400)
    ))

    // Chat
    val activeChatFriend = MutableStateFlow("LudoChamp")
    val chatHistory = MutableStateFlow(listOf(
        ChatMessage("LudoChamp", "Hey! Let's play a private match!"),
        ChatMessage("LudoChamp", "I challenge you 1v1."),
        ChatMessage("System", "LudoChamp invited you to Join Room 48512", isMe = false)
    ))

    // Friends list
    val friendsList = MutableStateFlow(listOf(
        Pair("LudoChamp", "Online"),
        Pair("StarDice", "Offline"),
        Pair("SuperGamer", "In-Game"),
        Pair("LudoQueen", "Online")
    ))

    // Wallet states
    val walletBalance = MutableStateFlow("10,000")
    val walletGems = MutableStateFlow("150")
    val transactionsList = MutableStateFlow(listOf(
        WalletTransaction("TX-94812", "Deposit", "+$10.00", "2026-07-15", "Completed"),
        WalletTransaction("TX-94811", "Entry Fee", "-500 Coins", "2026-07-14", "Completed"),
        WalletTransaction("TX-94810", "Reward", "+1,200 Coins", "2026-07-14", "Completed"),
        WalletTransaction("TX-94809", "Withdrawal", "-$5.00", "2026-07-12", "Completed")
    ))

    init {
        // Load initial data from DB and set up baseline config
        viewModelScope.launch {
            repository.userProfile.collect { profile ->
                if (profile == null) {
                    val defaultProfile = UserProfile()
                    repository.saveUserProfile(defaultProfile)
                    _profileState.value = defaultProfile
                } else {
                    _profileState.value = profile
                    soundEnabled.value = profile.isSoundEnabled
                    musicEnabled.value = profile.isMusicEnabled
                    notificationsEnabled.value = profile.isNotificationsEnabled
                }
            }
        }

        viewModelScope.launch {
            repository.matchHistory.collect { history ->
                if (history.isEmpty()) {
                    // Prepopulate with a few matches
                    repository.addMatchHistory(MatchHistory(gameMode = "Classic Ludo", result = "VICTORY", coinsGained = 1200, xpGained = 450, duration = "12:45"))
                    repository.addMatchHistory(MatchHistory(gameMode = "Quick Match", result = "DEFEAT", coinsGained = 0, xpGained = 50, duration = "08:12"))
                } else {
                    _matchHistory.value = history
                }
            }
        }

        viewModelScope.launch {
            repository.unlockedSkins.collect { skins ->
                if (skins.isEmpty()) {
                    // Unlocked default skins
                    repository.addUnlockedSkin(UnlockedSkin(skinType = "dice", skinName = "Classic", isEquipped = true))
                    repository.addUnlockedSkin(UnlockedSkin(skinType = "board", skinName = "Classic Glow", isEquipped = true))
                } else {
                    _unlockedSkins.value = skins
                    diceTheme.value = skins.find { it.skinType == "dice" && it.isEquipped }?.skinName ?: "Classic"
                    boardTheme.value = skins.find { it.skinType == "board" && it.isEquipped }?.skinName ?: "Classic Glow"
                }
            }
        }

        // Timer controller for match
        viewModelScope.launch {
            while (true) {
                delay(1000)
                if (_currentScreen.value is Screen.Gameplay && !isDiceRolling.value) {
                    if (turnTimer.value > 0) {
                        turnTimer.value -= 1
                    } else {
                        // Switch turn automatically on timeout
                        nextTurn()
                    }
                }
            }
        }
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    // Auth flows
    fun handleGuestLogin() {
        navigateTo(Screen.Dashboard)
    }

    fun handleLogin() {
        navigateTo(Screen.Dashboard)
    }

    fun handleRegister() {
        navigateTo(Screen.Dashboard)
    }

    // Save profile changes
    fun updateProfile(username: String, avatar: String, country: String, language: String) {
        viewModelScope.launch {
            val updated = _profileState.value.copy(
                username = username,
                avatarResName = avatar,
                country = country,
                language = language
            )
            repository.saveUserProfile(updated)
            _profileState.value = updated
        }
    }

    // Wallet Recharge simulation
    fun addFundsSimulate(amount: Int) {
        viewModelScope.launch {
            val currentCoins = _profileState.value.coins
            val updated = _profileState.value.copy(coins = currentCoins + amount)
            repository.saveUserProfile(updated)
            _profileState.value = updated
            walletBalance.value = String.format("%,d", updated.coins)

            val updatedTx = transactionsList.value.toMutableList()
            updatedTx.add(0, WalletTransaction(
                id = "TX-${Random.nextInt(90000, 99999)}",
                type = "Deposit",
                amount = "+${amount} Coins",
                date = "2026-07-16",
                status = "Completed"
            ))
            transactionsList.value = updatedTx
        }
    }

    // Wallet Withdraw simulation
    fun withdrawFundsSimulate(amount: Int): Boolean {
        val currentCoins = _profileState.value.coins
        if (currentCoins < amount) return false

        viewModelScope.launch {
            val updated = _profileState.value.copy(coins = currentCoins - amount)
            repository.saveUserProfile(updated)
            _profileState.value = updated
            walletBalance.value = String.format("%,d", updated.coins)

            val updatedTx = transactionsList.value.toMutableList()
            updatedTx.add(0, WalletTransaction(
                id = "TX-${Random.nextInt(90000, 99999)}",
                type = "Withdrawal",
                amount = "-${amount} Coins",
                date = "2026-07-16",
                status = "Completed"
            ))
            transactionsList.value = updatedTx
        }
        return true
    }

    // Claim missions
    fun claimMissionReward(id: Int) {
        val updatedMissions = missions.value.map {
            if (it.id == id) it.copy(isClaimed = true) else it
        }
        missions.value = updatedMissions

        // Add 500 coins for mission completion
        addFundsSimulate(500)
    }

    // Spin Lucky Wheel
    fun spinWheel() {
        if (isSpinning.value) return
        viewModelScope.launch {
            isSpinning.value = true
            delay(3000) // Spin animation
            val rewards = listOf("500 Coins", "50 Gems", "2,000 Coins", "Jackpot 10,000 Coins", "10 Gems", "Dice Skin Classic Gold")
            val chosen = rewards.random()
            lastSpinReward.value = chosen

            if (chosen.contains("Coins")) {
                val amount = chosen.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 500
                addFundsSimulate(amount)
            } else if (chosen.contains("Gems")) {
                val amount = chosen.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 10
                val updated = _profileState.value.copy(gems = _profileState.value.gems + amount)
                repository.saveUserProfile(updated)
                _profileState.value = updated
                walletGems.value = updated.gems.toString()
            } else if (chosen.contains("Gold")) {
                repository.addUnlockedSkin(UnlockedSkin(skinType = "dice", skinName = "Classic Gold", isEquipped = false))
            }

            isSpinning.value = false
        }
    }

    // Claim Daily check-in rewards (0..6 representing Days 1 to 7)
    fun claimDailyReward(dayIndex: Int) {
        val updatedClaimed = dailyRewardsClaimed.value.toMutableSet()
        if (updatedClaimed.add(dayIndex)) {
            dailyRewardsClaimed.value = updatedClaimed
            val coinsGained = when (dayIndex) {
                0 -> 200
                1 -> 500
                2 -> 1000
                3 -> 1500
                4 -> 2000
                5 -> 3000
                else -> 5000 // Day 7
            }
            addFundsSimulate(coinsGained)
        }
    }

    // Buy Theme or Dice Skin from store
    fun purchaseSkin(skinName: String, skinType: String, cost: Int, currency: String): Boolean {
        val profile = _profileState.value
        if (currency == "COINS" && profile.coins < cost) return false
        if (currency == "GEMS" && profile.gems < cost) return false

        viewModelScope.launch {
            val updatedProfile = if (currency == "COINS") {
                profile.copy(coins = profile.coins - cost)
            } else {
                profile.copy(gems = profile.gems - cost)
            }
            repository.saveUserProfile(updatedProfile)
            _profileState.value = updatedProfile
            walletBalance.value = String.format("%,d", updatedProfile.coins)
            walletGems.value = updatedProfile.gems.toString()

            repository.addUnlockedSkin(UnlockedSkin(skinType = skinType, skinName = skinName, isEquipped = false))
        }
        return true
    }

    fun equipSkin(skinName: String, skinType: String) {
        viewModelScope.launch {
            repository.equipSkin(skinType, skinName)
        }
    }

    // Toggle Settings
    fun toggleSound() {
        val state = !soundEnabled.value
        soundEnabled.value = state
        viewModelScope.launch {
            repository.saveUserProfile(_profileState.value.copy(isSoundEnabled = state))
        }
    }

    fun toggleMusic() {
        val state = !musicEnabled.value
        musicEnabled.value = state
        viewModelScope.launch {
            repository.saveUserProfile(_profileState.value.copy(isMusicEnabled = state))
        }
    }

    fun toggleNotifications() {
        val state = !notificationsEnabled.value
        notificationsEnabled.value = state
        viewModelScope.launch {
            repository.saveUserProfile(_profileState.value.copy(isNotificationsEnabled = state))
        }
    }

    // Join Tournament simulation
    fun joinTournament(tournamentId: Int): Boolean {
        val tourney = tournaments.value.find { it.id == tournamentId } ?: return false
        if (tourney.joined) return true

        // Check entry fee (e.g. "500 Coins")
        val feeString = tourney.entryFee.replace(Regex("[^0-9]"), "")
        val feeAmount = feeString.toIntOrNull() ?: 0

        val currentProfile = _profileState.value
        if (currentProfile.coins < feeAmount) return false

        viewModelScope.launch {
            val updatedProfile = currentProfile.copy(coins = currentProfile.coins - feeAmount)
            repository.saveUserProfile(updatedProfile)
            _profileState.value = updatedProfile
            walletBalance.value = String.format("%,d", updatedProfile.coins)

            tournaments.value = tournaments.value.map {
                if (it.id == tournamentId) {
                    it.copy(joined = true, participantsCount = it.participantsCount + 1)
                } else {
                    it
                }
            }

            // Route to tournament bracket view
            navigateTo(Screen.TournamentBracket)
        }
        return true
    }

    // Matchmaking simulator
    fun startMatchmaking() {
        navigateTo(Screen.Matchmaking)
        matchmakingTime.value = 0
        matchmakingFound.value = false

        viewModelScope.launch {
            for (i in 1..8) {
                delay(1000)
                matchmakingTime.value = i
                if (i == 4) {
                    matchmakingFound.value = true
                }
            }
            // Once matchmaking succeeds, initialize and jump to the Gameplay Board screen
            initializeLudoBoard()
            navigateTo(Screen.Gameplay)
        }
    }

    // Initialize Ludo Game Board
    fun initializeLudoBoard() {
        // Clear winner
        winnerPlayer.value = null
        currentPlayerIndex.value = 0
        hasDiceRolled.value = false
        turnTimer.value = 30

        // Create 4 players (User, and 3 AIs)
        val list = listOf(
            LudoPlayer(name = _profileState.value.username, avatar = _profileState.value.avatarResName, color = "RED", isAi = false),
            LudoPlayer(name = "DiceDemon_AI", avatar = "avatar_2", color = "GREEN", isAi = true),
            LudoPlayer(name = "LudoLover_AI", avatar = "avatar_3", color = "YELLOW", isAi = true),
            LudoPlayer(name = "PawnMaster_AI", avatar = "avatar_4", color = "BLUE", isAi = true)
        )
        activePlayers.value = list

        // Create 16 tokens (4 per player)
        val tokenList = mutableListOf<LudoToken>()
        val colors = listOf("RED", "GREEN", "YELLOW", "BLUE")
        val offsets = listOf(0, 13, 26, 39) // starting board slots

        for (cIndex in 0..3) {
            val color = colors[cIndex]
            val offset = offsets[cIndex]
            for (tId in 0..3) {
                tokenList.add(
                    LudoToken(
                        id = tId,
                        color = color,
                        position = -1, // starts in yard
                        startOffset = offset,
                        homeColorIndex = cIndex
                    )
                )
            }
        }
        tokens.value = tokenList
    }

    // Dice rolling action
    fun rollDice() {
        if (isDiceRolling.value || hasDiceRolled.value) return
        viewModelScope.launch {
            isDiceRolling.value = true
            // Play a simulated quick dice sound or vibration here
            for (i in 1..6) {
                diceResult.value = Random.nextInt(1, 7)
                delay(100)
            }
            isDiceRolling.value = false
            hasDiceRolled.value = true

            // Automate AI turn decision or see if current player has any valid moves
            val activePlayer = activePlayers.value[currentPlayerIndex.value]
            if (activePlayer.isAi) {
                delay(800)
                makeAiMove()
            } else {
                // Check if user has no valid moves (e.g. rolled non-6 and all in yard)
                val validMoves = getValidTokensToMove(activePlayer.color, diceResult.value)
                if (validMoves.isEmpty()) {
                    // Flash "No valid moves!" message and auto-advance
                    delay(1200)
                    nextTurn()
                }
            }
        }
    }

    // Get valid tokens that can move
    private fun getValidTokensToMove(color: String, roll: Int): List<LudoToken> {
        val list = tokens.value.filter { it.color == color }
        val valid = mutableListOf<LudoToken>()
        for (token in list) {
            if (token.position == -1) {
                // Needs exactly a 6 to escape the yard
                if (roll == 6) {
                    valid.add(token)
                }
            } else if (token.position in 0..56) {
                // Can move if it does not exceed the home finish point (index 57 is home)
                if (token.position + roll <= 57) {
                    valid.add(token)
                }
            }
        }
        return valid
    }

    // Move player token
    fun moveToken(token: LudoToken) {
        if (!hasDiceRolled.value || isDiceRolling.value) return
        val playerColor = activePlayers.value[currentPlayerIndex.value].color
        if (token.color != playerColor) return // Not your token

        val roll = diceResult.value
        val validMoves = getValidTokensToMove(playerColor, roll)
        if (!validMoves.contains(token)) return

        // Perform move
        viewModelScope.launch {
            val updatedTokens = tokens.value.toMutableList()
            val indexInList = updatedTokens.indexOfFirst { it.id == token.id && it.color == token.color }
            if (indexInList != -1) {
                val currentPos = token.position
                val newPos = if (currentPos == -1) {
                    0 // Enter track
                } else {
                    currentPos + roll
                }

                val movedToken = token.copy(position = newPos)
                updatedTokens[indexInList] = movedToken

                // Check captures on landing (excluding yard, safe zones or home path/finished)
                // Common board coordinates conversion can be computed as:
                // (localTrackIndex + startOffset) % 52
                if (newPos in 0..50) {
                    val actualBoardIndex = (newPos + token.startOffset) % 52
                    // Safe zone check (e.g. indices divisible by 13 are starts: 0, 13, 26, 39; plus some others, let's keep start cells safe)
                    val isSafeZone = actualBoardIndex in listOf(0, 8, 13, 21, 26, 34, 39, 47)

                    if (!isSafeZone) {
                        // Look for other colored tokens on the same actual board square
                        for (otherIndex in updatedTokens.indices) {
                            val other = updatedTokens[otherIndex]
                            if (other.color != movedToken.color && other.position in 0..50) {
                                val otherBoardIndex = (other.position + other.startOffset) % 52
                                if (otherBoardIndex == actualBoardIndex) {
                                    // Send opponent back to yard!
                                    updatedTokens[otherIndex] = other.copy(position = -1)
                                    // Give an extra roll or logs
                                }
                            }
                        }
                    }
                }

                tokens.value = updatedTokens

                // Check if this player finished all 4 tokens
                val hasFinishedAll = updatedTokens.filter { it.color == playerColor }.all { it.position == 57 }
                if (hasFinishedAll) {
                    // VICTORY!
                    val winner = activePlayers.value[currentPlayerIndex.value]
                    winnerPlayer.value = winner

                    // Save match history
                    if (winner.isAi) {
                        repository.addMatchHistory(MatchHistory(gameMode = selectedMode.value, result = "DEFEAT", coinsGained = 0, xpGained = 100, duration = "15:30"))
                    } else {
                        // Gain coins
                        val gainedCoins = matchWinnerCoins.value
                        val gainedXp = matchWinnerXp.value
                        val updatedProfile = _profileState.value.copy(
                            coins = _profileState.value.coins + gainedCoins,
                            xp = _profileState.value.xp + gainedXp,
                            wins = _profileState.value.wins + 1
                        )
                        repository.saveUserProfile(updatedProfile)
                        _profileState.value = updatedProfile
                        walletBalance.value = String.format("%,d", updatedProfile.coins)

                        repository.addMatchHistory(MatchHistory(gameMode = selectedMode.value, result = "VICTORY", coinsGained = gainedCoins, xpGained = gainedXp, duration = "15:30"))
                    }

                    navigateTo(Screen.AfterMatch)
                } else {
                    // Turn finished, transition
                    if (roll == 6) {
                        // Rolled a 6! Give another roll
                        hasDiceRolled.value = false
                        turnTimer.value = 30
                        val nextPlayer = activePlayers.value[currentPlayerIndex.value]
                        if (nextPlayer.isAi) {
                            delay(800)
                            rollDice()
                        }
                    } else {
                        nextTurn()
                    }
                }
            }
        }
    }

    // Simulated AI gameplay turn decisions
    private suspend fun makeAiMove() {
        val color = activePlayers.value[currentPlayerIndex.value].color
        val roll = diceResult.value
        val valid = getValidTokensToMove(color, roll)

        if (valid.isNotEmpty()) {
            // Prioritize moving tokens out of yard, then capturing, then highest track position
            val tokenToMove = valid.maxByOrNull {
                if (it.position == -1) 100
                else it.position
            } ?: valid.random()

            delay(1000)
            moveToken(tokenToMove)
        } else {
            delay(1000)
            nextTurn()
        }
    }

    // Advance to next turn
    private fun nextTurn() {
        currentPlayerIndex.value = (currentPlayerIndex.value + 1) % 4
        hasDiceRolled.value = false
        turnTimer.value = 30

        // If next is AI, auto roll
        viewModelScope.launch {
            val activePlayer = activePlayers.value[currentPlayerIndex.value]
            if (activePlayer.isAi) {
                delay(1000)
                rollDice()
            }
        }
    }

    // Social chat simulator replying automatically to user
    fun sendChatMessage(msgText: String) {
        val currentList = chatHistory.value.toMutableList()
        currentList.add(ChatMessage("Me", msgText, isMe = true))
        chatHistory.value = currentList

        // Simulate reply from the active friend
        viewModelScope.launch {
            delay(1500)
            val replyText = when {
                msgText.lowercase().contains("play") -> "Awesome, generate the room and I will join!"
                msgText.lowercase().contains("hello") || msgText.lowercase().contains("hey") -> "Hey there! Ready to dominate the Ludo board?"
                msgText.lowercase().contains("win") -> "I had a massive win streak yesterday, let's see if you can break it."
                else -> "Haha, let's roll some dice! Invite me!"
            }
            val updated = chatHistory.value.toMutableList()
            updated.add(ChatMessage(activeChatFriend.value, replyText, isMe = false))
            chatHistory.value = updated
        }
    }
}
