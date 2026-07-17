package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.random.Random

// Beautiful color palette
object PremiumTheme {
    val BgStart = Color(0xFF0F172A) // Dark Navy
    val BgEnd = Color(0xFF020617)   // Deep black
    
    val Red = Color(0xFFEF4444)
    val Green = Color(0xFF16A34A)
    val Yellow = Color(0xFFFACC15)
    val Blue = Color(0xFF2563EB)
    
    val RedGlow = Color(0xFFFCA5A5)
    val GreenGlow = Color(0xFF86EFAC)
    val YellowGlow = Color(0xFFFEF08A)
    val BlueGlow = Color(0xFF93C5FD)

    val CardBg = Color(0x2AFFFFFF) // Glassmorphism
    val CardBorder = Color(0x1EFFFFFF)
    
    val TextPrimary = Color(0xFFF8FAFC)
    val TextSecondary = Color(0xFF94A3B8)
}

@Composable
fun LudoMainApp(viewModel: LudoViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val profile by viewModel.profileState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = PremiumTheme.BgEnd
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(PremiumTheme.BgStart, PremiumTheme.BgEnd)
                    )
                )
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "ScreenTransition"
            ) { screen ->
                when (screen) {
                    Screen.Splash -> SplashScreen(viewModel)
                    Screen.Onboarding1 -> OnboardingScreen(viewModel, 1)
                    Screen.Onboarding2 -> OnboardingScreen(viewModel, 2)
                    Screen.Onboarding3 -> OnboardingScreen(viewModel, 3)
                    Screen.Auth -> AuthScreen(viewModel)
                    Screen.Dashboard -> DashboardScreen(viewModel)
                    Screen.GameSetup -> GameSetupScreen(viewModel)
                    Screen.Matchmaking -> MatchmakingScreen(viewModel)
                    Screen.RoomSetup -> RoomSetupScreen(viewModel)
                    Screen.Gameplay -> GameplayScreen(viewModel)
                    Screen.AfterMatch -> AfterMatchScreen(viewModel)
                    Screen.TournamentList -> TournamentListScreen(viewModel)
                    Screen.TournamentBracket -> TournamentBracketScreen(viewModel)
                    Screen.Leaderboard -> LeaderboardScreen(viewModel)
                    Screen.LuckySpin -> LuckySpinScreen(viewModel)
                    Screen.Store -> StoreScreen(viewModel)
                    Screen.Wallet -> WalletScreen(viewModel)
                    Screen.Friends -> FriendsScreen(viewModel)
                    Screen.Chat -> ChatScreen(viewModel)
                    Screen.Settings -> SettingsScreen(viewModel)
                    Screen.HelpCenter -> HelpCenterScreen(viewModel)
                    Screen.ReportPlayer -> ReportPlayerScreen(viewModel)
                    Screen.Privacy -> PrivacyScreen(viewModel)
                }
            }
        }
    }
}

// ==========================================
// 1. SPLASH SCREEN
// ==========================================
@Composable
fun SplashScreen(viewModel: LudoViewModel) {
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000)
        viewModel.navigateTo(Screen.Onboarding1)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Dice Logo Icon with Glow
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .shadow(24.dp, shape = RoundedCornerShape(24.dp), ambientColor = PremiumTheme.Blue, spotColor = PremiumTheme.Blue)
                    .background(
                        Brush.radialGradient(
                            listOf(PremiumTheme.Blue, Color.Transparent)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Casino,
                    contentDescription = "Deges Play Logo",
                    tint = PremiumTheme.TextPrimary,
                    modifier = Modifier.size(80.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "DEGES PLAY",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = PremiumTheme.TextPrimary,
                letterSpacing = 2.sp
            )
            Text(
                text = "Ultimate Cross-Platform Experience",
                fontSize = 14.sp,
                color = PremiumTheme.TextSecondary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(64.dp))
            CircularProgressIndicator(
                color = PremiumTheme.Blue,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "v1.0.4",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

// ==========================================
// 2. ONBOARDING SCREEN
// ==========================================
@Composable
fun OnboardingScreen(viewModel: LudoViewModel, step: Int) {
    val title = when (step) {
        1 -> "Welcome to Deges Play"
        2 -> "Compete Worldwide"
        else -> "Claim Daily Rewards"
    }
    val description = when (step) {
        1 -> "Experience the best of Ludo King and Monopoly GO combined with modern gaming visual animations."
        2 -> "Join multi-tier online tournaments and win huge prize pools. Master brackets in real time!"
        else -> "Collect coins, spin the lucky wheel, and climb global leaderboards to become the Dice King!"
    }
    val icon = when (step) {
        1 -> Icons.Outlined.Group
        2 -> Icons.Outlined.EmojiEvents
        else -> Icons.Outlined.Redeem
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .navigationBarsPadding()
            .statusBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "STEP $step / 3",
                color = PremiumTheme.TextSecondary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            TextButton(
                onClick = { viewModel.navigateTo(Screen.Auth) },
                modifier = Modifier.testTag("onboarding_skip")
            ) {
                Text("Skip", color = PremiumTheme.BlueGlow)
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Glassmorphism graphic placeholder
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .shadow(16.dp, RoundedCornerShape(40.dp))
                    .background(PremiumTheme.CardBg, RoundedCornerShape(40.dp))
                    .border(1.dp, PremiumTheme.CardBorder, RoundedCornerShape(40.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PremiumTheme.Blue,
                    modifier = Modifier.size(90.dp)
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PremiumTheme.TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = description,
                fontSize = 15.sp,
                color = PremiumTheme.TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Button(
            onClick = {
                if (step < 3) {
                    viewModel.navigateTo(if (step == 1) Screen.Onboarding2 else Screen.Onboarding3)
                } else {
                    viewModel.navigateTo(Screen.Auth)
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = PremiumTheme.Blue),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("onboarding_next")
        ) {
            Text(
                text = if (step == 3) "GET STARTED" else "NEXT",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

// ==========================================
// 3. AUTHENTICATION SCREEN
// ==========================================
@Composable
fun AuthScreen(viewModel: LudoViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var agreed by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "DEGES PLAY",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = PremiumTheme.TextPrimary,
            letterSpacing = 1.sp
        )
        Text(
            text = "Enter the Arena",
            fontSize = 16.sp,
            color = PremiumTheme.TextSecondary,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = PremiumTheme.TextPrimary,
                unfocusedTextColor = PremiumTheme.TextPrimary,
                focusedBorderColor = PremiumTheme.Blue,
                unfocusedBorderColor = PremiumTheme.TextSecondary,
                focusedLabelColor = PremiumTheme.BlueGlow,
                unfocusedLabelColor = PremiumTheme.TextSecondary
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_email")
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = PremiumTheme.TextPrimary,
                unfocusedTextColor = PremiumTheme.TextPrimary,
                focusedBorderColor = PremiumTheme.Blue,
                unfocusedBorderColor = PremiumTheme.TextSecondary,
                focusedLabelColor = PremiumTheme.BlueGlow,
                unfocusedLabelColor = PremiumTheme.TextSecondary
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_password")
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = agreed,
                onCheckedChange = { agreed = it },
                colors = CheckboxDefaults.colors(checkedColor = PremiumTheme.Blue)
            )
            Text(
                text = "I accept the Terms and Privacy Policy",
                color = PremiumTheme.TextSecondary,
                fontSize = 12.sp,
                modifier = Modifier.clickable { viewModel.navigateTo(Screen.Privacy) }
            )
        }

        Button(
            onClick = { if (agreed) viewModel.handleLogin() },
            colors = ButtonDefaults.buttonColors(containerColor = PremiumTheme.Blue),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("auth_login")
        ) {
            Text("LOGIN", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { viewModel.handleGuestLogin() },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = PremiumTheme.BlueGlow),
            border = BorderStroke(1.dp, PremiumTheme.Blue),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("auth_guest")
        ) {
            Text("CONTINUE AS GUEST", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Or Sign In With", color = PremiumTheme.TextSecondary, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Social Providers
            listOf(
                Pair(Icons.Filled.Group, "Google"),
                Pair(Icons.Filled.AccountBox, "Facebook")
            ).forEach { item ->
                OutlinedButton(
                    onClick = { viewModel.handleGuestLogin() },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PremiumTheme.TextPrimary),
                    border = BorderStroke(1.dp, PremiumTheme.CardBorder),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(item.first, contentDescription = item.second)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(item.second)
                }
            }
        }
    }
}

// ==========================================
// 4. MAIN DASHBOARD SCREEN
// ==========================================
@Composable
fun DashboardScreen(viewModel: LudoViewModel) {
    val profile by viewModel.profileState.collectAsState()
    var activeTab by remember { mutableStateOf("home") } // "home", "tournament", "friends", "wallet", "profile"

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = PremiumTheme.BgStart,
                contentColor = PremiumTheme.TextPrimary,
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                listOf(
                    Triple("home", Icons.Filled.Home, "Home"),
                    Triple("tournament", Icons.Filled.EmojiEvents, "Tourney"),
                    Triple("friends", Icons.Filled.People, "Friends"),
                    Triple("wallet", Icons.Filled.AccountBalanceWallet, "Wallet"),
                    Triple("profile", Icons.Filled.Person, "Profile")
                ).forEach { tab ->
                    NavigationBarItem(
                        selected = activeTab == tab.first,
                        onClick = { activeTab = tab.first },
                        icon = { Icon(tab.second, contentDescription = tab.third) },
                        label = { Text(tab.third, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = Color.White,
                            indicatorColor = PremiumTheme.Blue,
                            unselectedIconColor = PremiumTheme.TextSecondary,
                            unselectedTextColor = PremiumTheme.TextSecondary
                        )
                    )
                }
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
        ) {
            // Dynamic Top Info Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { activeTab = "profile" }
                ) {
                    // Avatar Placeholder
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(PremiumTheme.Blue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = profile.username.take(2).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(profile.username, color = PremiumTheme.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Lvl ${profile.level}", color = PremiumTheme.Yellow, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Coins Chip
                    Box(
                        modifier = Modifier
                            .background(PremiumTheme.CardBg, RoundedCornerShape(16.dp))
                            .border(1.dp, PremiumTheme.CardBorder, RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .clickable { activeTab = "wallet" }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.MonetizationOn, contentDescription = "Coins", tint = PremiumTheme.Yellow, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(String.format("%,d", profile.coins), color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Gems Chip
                    Box(
                        modifier = Modifier
                            .background(PremiumTheme.CardBg, RoundedCornerShape(16.dp))
                            .border(1.dp, PremiumTheme.CardBorder, RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .clickable { activeTab = "wallet" }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Diamond, contentDescription = "Gems", tint = PremiumTheme.BlueGlow, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(profile.gems.toString(), color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Settings Icon
                    IconButton(onClick = { viewModel.navigateTo(Screen.Settings) }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = PremiumTheme.TextPrimary)
                    }
                }
            }

            // Body Area based on Tab
            Box(modifier = Modifier.weight(1f)) {
                when (activeTab) {
                    "home" -> HomeView(viewModel)
                    "tournament" -> TournamentListScreen(viewModel)
                    "friends" -> FriendsScreen(viewModel)
                    "wallet" -> WalletScreen(viewModel)
                    "profile" -> ProfileView(viewModel)
                }
            }
        }
    }
}

@Composable
fun HomeView(viewModel: LudoViewModel) {
    var showDailyCheckin by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        // Banner Slider Promotion Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(
                    Brush.horizontalGradient(listOf(PremiumTheme.Blue, PremiumTheme.Green)),
                    RoundedCornerShape(20.dp)
                )
                .shadow(8.dp)
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column {
                Text("SUMMER TOURNAMENT IS LIVE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Join for 500 Coins and win up to 50,000 Coins!", color = Color.White.copy(0.85f), fontSize = 13.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { viewModel.navigateTo(Screen.TournamentList) },
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumTheme.Yellow),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("JOIN NOW", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grid Menu Options
        Text("SELECT GAME MODE", color = PremiumTheme.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(vertical = 8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DashboardItem(
                title = "Play Online",
                subtitle = "Global Matchmaking",
                icon = Icons.Filled.Language,
                color = PremiumTheme.Blue,
                modifier = Modifier.weight(1f)
            ) {
                viewModel.selectedMode.value = "Classic Ludo"
                viewModel.startMatchmaking()
            }
            DashboardItem(
                title = "Play With Friends",
                subtitle = "Private Rooms",
                icon = Icons.Filled.Share,
                color = PremiumTheme.Green,
                modifier = Modifier.weight(1f)
            ) {
                viewModel.navigateTo(Screen.RoomSetup)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DashboardItem(
                title = "Local Pass & Play",
                subtitle = "Multiplayer Offline",
                icon = Icons.Filled.PhonelinkRing,
                color = PremiumTheme.Yellow,
                titleColor = Color.Black,
                modifier = Modifier.weight(1f)
            ) {
                viewModel.initializeLudoBoard()
                viewModel.navigateTo(Screen.Gameplay)
            }
            DashboardItem(
                title = "Practice with AI",
                subtitle = "Offline Practice Mode",
                icon = Icons.Filled.Computer,
                color = PremiumTheme.Red,
                modifier = Modifier.weight(1f)
            ) {
                viewModel.selectedMode.value = "Practice Mode"
                viewModel.initializeLudoBoard()
                viewModel.navigateTo(Screen.Gameplay)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Mini Games and Rewards section
        Text("MINI GAMES & BONUSES", color = PremiumTheme.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(vertical = 8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { viewModel.navigateTo(Screen.LuckySpin) },
                border = BorderStroke(1.dp, PremiumTheme.CardBorder),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.Casino, contentDescription = "Lucky Spin", tint = PremiumTheme.Red)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Lucky Spin")
            }

            OutlinedButton(
                onClick = { showDailyCheckin = true },
                border = BorderStroke(1.dp, PremiumTheme.CardBorder),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.Redeem, contentDescription = "Daily Reward", tint = PremiumTheme.Yellow)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Daily Check-In")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showDailyCheckin) {
        DailyCheckinDialog(viewModel) { showDailyCheckin = false }
    }
}

@Composable
fun DashboardItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    titleColor: Color = Color.White,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(115.dp)
            .background(color, RoundedCornerShape(16.dp))
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = titleColor.copy(0.2f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(56.dp)
        )
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = titleColor)
            Text(subtitle, fontSize = 11.sp, color = titleColor.copy(0.75f))
        }
    }
}

// ==========================================
// 5. PROFILE SCREEN VIEW
// ==========================================
@Composable
fun ProfileView(viewModel: LudoViewModel) {
    val profile by viewModel.profileState.collectAsState()
    val matches by viewModel.matchHistory.collectAsState()
    val missionsList by viewModel.missions.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PremiumTheme.CardBg, RoundedCornerShape(20.dp))
                    .border(1.dp, PremiumTheme.CardBorder, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(PremiumTheme.Blue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(profile.username.take(2).uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(profile.username, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Country: ${profile.country}", color = PremiumTheme.TextSecondary, fontSize = 13.sp)
                    Text("XP Points: ${profile.xp}", color = PremiumTheme.BlueGlow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        item {
            // Stats Panel
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf(
                    Pair("Wins", profile.wins.toString()),
                    Pair("Losses", profile.losses.toString()),
                    Pair("Win Rate", "${profile.winRate}%"),
                    Pair("Total Matches", profile.totalMatches.toString())
                ).forEach { stat ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(PremiumTheme.CardBg, RoundedCornerShape(12.dp))
                            .border(1.dp, PremiumTheme.CardBorder, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(stat.first, color = PremiumTheme.TextSecondary, fontSize = 11.sp)
                            Text(stat.second, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }
                }
            }
        }

        item {
            Text("DAILY MISSIONS", color = PremiumTheme.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        items(missionsList) { mission ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PremiumTheme.CardBg, RoundedCornerShape(12.dp))
                    .border(1.dp, PremiumTheme.CardBorder, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(mission.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Reward: ${mission.reward}", color = PremiumTheme.Yellow, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { mission.progress },
                            color = PremiumTheme.Blue,
                            trackColor = Color.Gray.copy(0.3f),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    if (mission.isClaimed) {
                        Text("Claimed", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Button(
                            onClick = { viewModel.claimMissionReward(mission.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = PremiumTheme.Blue),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp)
                        ) {
                            Text("CLAIM", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Text("RECENT MATCHES", color = PremiumTheme.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        if (matches.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Match History", color = PremiumTheme.TextSecondary)
                }
            }
        } else {
            items(matches) { match ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PremiumTheme.CardBg, RoundedCornerShape(12.dp))
                        .border(1.dp, PremiumTheme.CardBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        if (match.result == "VICTORY") PremiumTheme.Green else PremiumTheme.Red,
                                        CircleShape
                                    )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(match.gameMode, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(match.duration, color = PremiumTheme.TextSecondary, fontSize = 11.sp)
                            }
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                match.result,
                                color = if (match.result == "VICTORY") PremiumTheme.Green else PremiumTheme.Red,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            if (match.coinsGained > 0) {
                                Text("+${match.coinsGained} Coins", color = PremiumTheme.Yellow, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 6. ROOM SCREEN SETUP / INVITE FRIENDS
// ==========================================
@Composable
fun RoomSetupScreen(viewModel: LudoViewModel) {
    var code by remember { mutableStateOf("48512") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text("Private Game Room", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.width(48.dp))
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .background(PremiumTheme.CardBg, RoundedCornerShape(16.dp))
                    .border(1.dp, PremiumTheme.CardBorder, RoundedCornerShape(16.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("ROOM CODE", color = PremiumTheme.TextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(code, color = PremiumTheme.Yellow, fontSize = 36.sp, fontWeight = FontWeight.Bold, letterSpacing = 4.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Share this code with friends to let them join", color = PremiumTheme.TextSecondary, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Player slots preview
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf(
                    Pair("You", true),
                    Pair("LudoChamp", true),
                    Pair("Empty", false),
                    Pair("Empty", false)
                ).forEach { slot ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .background(PremiumTheme.CardBg, RoundedCornerShape(12.dp))
                            .border(1.dp, PremiumTheme.CardBorder, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                if (slot.second) Icons.Filled.AccountCircle else Icons.Filled.Add,
                                contentDescription = null,
                                tint = if (slot.second) PremiumTheme.BlueGlow else Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(slot.first, color = if (slot.second) Color.White else Color.Gray, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                viewModel.initializeLudoBoard()
                viewModel.navigateTo(Screen.Gameplay)
            },
            colors = ButtonDefaults.buttonColors(containerColor = PremiumTheme.Green),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("START MATCH", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
        }
    }
}

// ==========================================
// 7. MATCHMAKING SCREEN
// ==========================================
@Composable
fun MatchmakingScreen(viewModel: LudoViewModel) {
    val duration by viewModel.matchmakingTime.collectAsState()
    val estTime by viewModel.matchmakingEstimated.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("FINDING PLAYERS", color = PremiumTheme.TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .background(PremiumTheme.CardBg, CircleShape)
                    .border(2.dp, PremiumTheme.Blue, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Spinning Dice Icon simulation
                Icon(
                    imageVector = Icons.Filled.Casino,
                    contentDescription = null,
                    tint = PremiumTheme.Yellow,
                    modifier = Modifier.size(72.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Elapsed: 00:0$duration", color = Color.White, fontSize = 16.sp)
            Text("Estimated: 00:$estTime", color = PremiumTheme.TextSecondary, fontSize = 13.sp)
        }

        OutlinedButton(
            onClick = { viewModel.navigateTo(Screen.Dashboard) },
            border = BorderStroke(1.dp, PremiumTheme.Red),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = PremiumTheme.Red),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("CANCEL MATCHMAKING", fontWeight = FontWeight.Bold)
        }
    }
}

// ==========================================
// 8. GAMEPLAY SCREEN (BOARD AND CONTROLS)
// ==========================================
@Composable
fun GameplayScreen(viewModel: LudoViewModel) {
    val players by viewModel.activePlayers.collectAsState()
    val tokens by viewModel.tokens.collectAsState()
    val currentTurnIndex by viewModel.currentPlayerIndex.collectAsState()
    val diceVal by viewModel.diceResult.collectAsState()
    val hasRolled by viewModel.hasDiceRolled.collectAsState()
    val isRolling by viewModel.isDiceRolling.collectAsState()
    val activeTimer by viewModel.turnTimer.collectAsState()

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Game Controls Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                Icon(Icons.Filled.ExitToApp, contentDescription = "Exit Game", tint = PremiumTheme.Red)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("MATCH CODE: 48512", color = PremiumTheme.TextSecondary, fontSize = 11.sp)
                Text("Prize Pool: 1,200 Coins", color = PremiumTheme.Yellow, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Box(
                modifier = Modifier
                    .background(PremiumTheme.Red, RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text("${activeTimer}s", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        // The Gorgeous 15x15 Interactive Board
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(12.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .border(2.dp, Color(0xFF1E293B), RoundedCornerShape(8.dp))
        ) {
            // Drawn Ludo Grid (Using simple Canvas or layout-boxes)
            LudoBoardDrawing()

            // Tokens Overlay on board
            tokens.forEach { token ->
                val coords = getCoordinatesForToken(token)
                if (coords != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(1f / 15f)
                            .offset(
                                x = (12.dp * coords.first), // Simplified proportional placing
                                y = (12.dp * coords.second)
                            )
                            .padding(2.dp)
                            .background(
                                color = when (token.color) {
                                    "RED" -> PremiumTheme.Red
                                    "GREEN" -> PremiumTheme.Green
                                    "YELLOW" -> PremiumTheme.Yellow
                                    else -> PremiumTheme.Blue
                                },
                                shape = CircleShape
                            )
                            .border(1.dp, Color.White, CircleShape)
                            .clickable {
                                viewModel.moveToken(token)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "♟",
                            color = if (token.color == "YELLOW") Color.Black else Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Info Cards / Active Turn Displays
        Column(modifier = Modifier.padding(16.dp)) {
            if (players.size > currentTurnIndex) {
                val p = players[currentTurnIndex]
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PremiumTheme.CardBg, RoundedCornerShape(12.dp))
                        .border(
                            2.dp,
                            if (p.color == "RED") PremiumTheme.Red else if (p.color == "GREEN") PremiumTheme.Green else if (p.color == "YELLOW") PremiumTheme.Yellow else PremiumTheme.Blue,
                            RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(PremiumTheme.Blue),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(p.name.take(2).uppercase(), color = Color.White, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(p.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(if (p.isAi) "AI Bot" else "Your Turn", color = PremiumTheme.TextSecondary, fontSize = 11.sp)
                            }
                        }

                        // Dice / Rolling Panel
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color.White, RoundedCornerShape(8.dp))
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                    .clickable {
                                        if (!p.isAi && !hasRolled && !isRolling) {
                                            viewModel.rollDice()
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isRolling) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = PremiumTheme.Blue, strokeWidth = 2.dp)
                                } else {
                                    Text(diceVal.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            if (!p.isAi && !hasRolled) {
                                Button(
                                    onClick = { viewModel.rollDice() },
                                    colors = ButtonDefaults.buttonColors(containerColor = PremiumTheme.Blue)
                                ) {
                                    Text("ROLL")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Draw the beautiful classic Ludo 15x15 board color fields dynamically
@Composable
fun LudoBoardDrawing() {
    Column(modifier = Modifier.fillMaxSize()) {
        for (row in 0..14) {
            Row(modifier = Modifier.weight(1f)) {
                for (col in 0..14) {
                    val color = when {
                        // Home center
                        row in 6..8 && col in 6..8 -> Color(0xFFE2E8F0)
                        
                        // Red yard top-left
                        row in 0..5 && col in 0..5 -> PremiumTheme.Red.copy(0.85f)
                        // Green yard top-right
                        row in 0..5 && col in 9..14 -> PremiumTheme.Green.copy(0.85f)
                        // Blue yard bottom-left
                        row in 9..14 && col in 0..5 -> PremiumTheme.Blue.copy(0.85f)
                        // Yellow yard bottom-right
                        row in 9..14 && col in 9..14 -> PremiumTheme.Yellow.copy(0.85f)
                        
                        // Home paths
                        row == 7 && col in 1..5 -> PremiumTheme.Red
                        row == 7 && col in 9..13 -> PremiumTheme.Yellow
                        col == 7 && row in 1..5 -> PremiumTheme.Green
                        col == 7 && row in 9..13 -> PremiumTheme.Blue
                        
                        // Default tracks
                        else -> Color(0xFFF1F5F9)
                    }
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(color)
                            .border(0.5.dp, Color.Gray.copy(0.4f))
                    )
                }
            }
        }
    }
}

fun getCoordinatesForToken(token: LudoToken): Pair<Float, Float>? {
    // Basic approximate mapping coordinates for token visualization to fit inside the board
    val startCoords = when (token.color) {
        "RED" -> Pair(2f, 2f)
        "GREEN" -> Pair(11f, 2f)
        "YELLOW" -> Pair(11f, 11f)
        else -> Pair(2f, 11f)
    }
    
    if (token.position == -1) return startCoords
    
    // Simplistic track coordinates
    return Pair(7f, 7f) // placed in intermediate positions
}

// ==========================================
// 9. AFTER MATCH / VICTORY SCREEN
// ==========================================
@Composable
fun AfterMatchScreen(viewModel: LudoViewModel) {
    val winner by viewModel.winnerPlayer.collectAsState()
    val coinsWon by viewModel.matchWinnerCoins.collectAsState()
    val xpWon by viewModel.matchWinnerXp.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("MATCH ENDED", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .background(PremiumTheme.Yellow, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = Color.White, modifier = Modifier.size(72.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = if (winner?.isAi == false) "CONGRATULATIONS!" else "GAME OVER",
                color = PremiumTheme.Yellow,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (winner?.isAi == false) "You won the Match!" else "${winner?.name ?: "AI"} wins!",
                color = Color.White,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Gained rewards boxes
            if (winner?.isAi == false) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(
                        modifier = Modifier
                            .background(PremiumTheme.CardBg, RoundedCornerShape(12.dp))
                            .border(1.dp, PremiumTheme.CardBorder, RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Coins Won", color = PremiumTheme.TextSecondary, fontSize = 12.sp)
                            Text("+$coinsWon", color = PremiumTheme.Yellow, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .background(PremiumTheme.CardBg, RoundedCornerShape(12.dp))
                            .border(1.dp, PremiumTheme.CardBorder, RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("XP Gained", color = PremiumTheme.TextSecondary, fontSize = 12.sp)
                            Text("+$xpWon", color = PremiumTheme.BlueGlow, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { viewModel.navigateTo(Screen.Dashboard) },
                colors = ButtonDefaults.buttonColors(containerColor = PremiumTheme.Blue),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("CONTINUE TO HOME", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ==========================================
// 10. TOURNAMENTS LIST & DETAILS
// ==========================================
@Composable
fun TournamentListScreen(viewModel: LudoViewModel) {
    val tourneys by viewModel.tournaments.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("ACTIVE TOURNAMENTS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(bottom = 12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(tourneys) { t ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PremiumTheme.CardBg, RoundedCornerShape(16.dp))
                        .border(1.dp, PremiumTheme.CardBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(t.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Box(
                                modifier = Modifier
                                    .background(PremiumTheme.Blue.copy(0.3f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(t.currentRound, color = PremiumTheme.BlueGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Prize Pool", color = PremiumTheme.TextSecondary, fontSize = 11.sp)
                                Text(t.prizePool, color = PremiumTheme.Yellow, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Column {
                                Text("Entry Fee", color = PremiumTheme.TextSecondary, fontSize = 11.sp)
                                Text(t.entryFee, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Column {
                                Text("Participants", color = PremiumTheme.TextSecondary, fontSize = 11.sp)
                                Text("${t.participantsCount}/${t.maxParticipants}", color = Color.White, fontSize = 14.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { viewModel.joinTournament(t.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = if (t.joined) PremiumTheme.Green else PremiumTheme.Blue),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (t.joined) "VIEW BRACKET" else "JOIN TOURNAMENT", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TournamentBracketScreen(viewModel: LudoViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text("Championship Bracket", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.width(48.dp))
        }

        // Bracket Flow Visual Board
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(PremiumTheme.CardBg, RoundedCornerShape(16.dp))
                .border(1.dp, PremiumTheme.CardBorder, RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("TOURNAMENT BRACKET PROGRESS", color = PremiumTheme.Yellow, fontWeight = FontWeight.Bold)
                
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Quarter-Finals", color = PremiumTheme.TextSecondary, fontSize = 12.sp)
                        BracketNode("User", "AI_Bot1", true)
                        BracketNode("AI_Bot2", "AI_Bot3", false)
                    }
                    Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color.White)
                    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                        Text("Semi-Finals", color = PremiumTheme.TextSecondary, fontSize = 12.sp)
                        BracketNode("User", "AI_Bot2", true)
                    }
                }
            }
        }

        Button(
            onClick = {
                viewModel.initializeLudoBoard()
                viewModel.navigateTo(Screen.Gameplay)
            },
            colors = ButtonDefaults.buttonColors(containerColor = PremiumTheme.Green),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("PLAY NEXT BRACKET MATCH", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun BracketNode(player1: String, player2: String, userWon: Boolean) {
    Box(
        modifier = Modifier
            .background(Color(0xFF1E293B), RoundedCornerShape(8.dp))
            .border(1.dp, PremiumTheme.CardBorder, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .width(100.dp)
    ) {
        Column {
            Text(player1, color = if (userWon) PremiumTheme.Green else Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Divider(color = Color.Gray, modifier = Modifier.padding(vertical = 4.dp))
            Text(player2, color = if (!userWon) PremiumTheme.Green else Color.White, fontSize = 12.sp)
        }
    }
}

// ==========================================
// 11. LEADERBOARD SCREEN
// ==========================================
@Composable
fun LeaderboardScreen(viewModel: LudoViewModel) {
    val tab by viewModel.leaderboardTab.collectAsState()
    val entries by viewModel.globalLeaderboard.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("LEADERBOARD", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Global", "Country", "Friends").forEach { t ->
                Button(
                    onClick = { viewModel.leaderboardTab.value = t },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (tab == t) PremiumTheme.Blue else PremiumTheme.CardBg
                    ),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(t, fontSize = 12.sp)
                }
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(entries) { entry ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (entry.isCurrentUser) PremiumTheme.Blue.copy(0.2f) else PremiumTheme.CardBg,
                            RoundedCornerShape(12.dp)
                        )
                        .border(
                            1.dp,
                            if (entry.isCurrentUser) PremiumTheme.Blue else PremiumTheme.CardBorder,
                            RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "#${entry.rank}",
                                color = when (entry.rank) {
                                    1 -> PremiumTheme.Yellow
                                    2 -> Color.LightGray
                                    3 -> Color(0xFFCD7F32)
                                    else -> Color.White
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.width(36.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(PremiumTheme.Blue),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(entry.name.take(2).uppercase(), color = Color.White, fontSize = 11.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(entry.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(entry.country, color = PremiumTheme.TextSecondary, fontSize = 11.sp)
                            }
                        }
                        Text(
                            "${entry.score} pts",
                            color = PremiumTheme.Yellow,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 12. LUCKY SPIN & DAILY CHECK-IN
// ==========================================
@Composable
fun LuckySpinScreen(viewModel: LudoViewModel) {
    val spinning by viewModel.isSpinning.collectAsState()
    val reward by viewModel.lastSpinReward.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text("Lucky Spin Wheel", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.width(48.dp))
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Rotating Spin Wheel Graphic
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .background(PremiumTheme.CardBg, CircleShape)
                    .border(4.dp, PremiumTheme.Blue, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Colored wedges representation
                Icon(
                    imageVector = Icons.Filled.Casino,
                    contentDescription = null,
                    tint = PremiumTheme.Yellow,
                    modifier = Modifier.size(96.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            if (reward != null) {
                Text("Congratulations!", color = PremiumTheme.Yellow, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("You won: $reward", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            } else {
                Text("Spin the wheel for high prizes!", color = PremiumTheme.TextSecondary, fontSize = 14.sp)
            }
        }

        Button(
            onClick = { viewModel.spinWheel() },
            colors = ButtonDefaults.buttonColors(containerColor = PremiumTheme.Blue),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            if (spinning) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("SPIN NOW", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun DailyCheckinDialog(viewModel: LudoViewModel, onDismiss: () -> Unit) {
    val claimed by viewModel.dailyRewardsClaimed.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("7-Day Daily Rewards", fontWeight = FontWeight.Bold, color = Color.White) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(200, 500, 1000, 1500, 2000, 3000, 5000).forEachIndexed { index, reward ->
                    val isClaimed = claimed.contains(index)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isClaimed) Color.Gray.copy(0.2f) else PremiumTheme.Blue.copy(0.1f),
                                RoundedCornerShape(8.dp)
                            )
                            .border(1.dp, if (isClaimed) Color.Transparent else PremiumTheme.Blue, RoundedCornerShape(8.dp))
                            .clickable {
                                if (!isClaimed) {
                                    viewModel.claimDailyReward(index)
                                }
                            }
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Day ${index + 1}", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("$reward Coins", color = PremiumTheme.Yellow, fontWeight = FontWeight.Bold)
                            if (isClaimed) {
                                Icon(Icons.Filled.Check, contentDescription = "Claimed", tint = PremiumTheme.Green)
                            } else {
                                Text("CLAIM", color = PremiumTheme.BlueGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = PremiumTheme.BlueGlow)
            }
        },
        containerColor = PremiumTheme.BgStart,
        textContentColor = Color.White
    )
}

// ==========================================
// 13. STORE SCREEN
// ==========================================
@Composable
fun StoreScreen(viewModel: LudoViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("THEME & SKIN STORE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(bottom = 12.dp))

        listOf(
            Triple("Classic Gold Dice", "dice", 2000),
            Triple("Cyberpunk Glow Board", "board", 5000),
            Triple("Neon Royal Glass", "dice", 1500),
            Triple("Vintage Wooden Board", "board", 4000)
        ).forEach { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .background(PremiumTheme.CardBg, RoundedCornerShape(12.dp))
                    .border(1.dp, PremiumTheme.CardBorder, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(item.first, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("Type: ${item.second.uppercase()}", color = PremiumTheme.TextSecondary, fontSize = 11.sp)
                    }
                    Button(
                        onClick = { viewModel.purchaseSkin(item.first, item.second, item.third, "COINS") },
                        colors = ButtonDefaults.buttonColors(containerColor = PremiumTheme.Yellow),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("${item.third} Coins", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ==========================================
// 14. WALLET & RECHARGE SCREEN
// ==========================================
@Composable
fun WalletScreen(viewModel: LudoViewModel) {
    val balance by viewModel.walletBalance.collectAsState()
    val gems by viewModel.walletGems.collectAsState()
    val txs by viewModel.transactionsList.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("WALLET & BALANCE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(bottom = 12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(listOf(PremiumTheme.Blue, Color(0xFF6366F1))),
                    RoundedCornerShape(16.dp)
                )
                .padding(20.dp)
        ) {
            Column {
                Text("Total Virtual Balance", color = Color.White.copy(0.8f), fontSize = 13.sp)
                Text("$balance Coins", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 28.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("$gems Gems", color = PremiumTheme.Yellow, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { viewModel.addFundsSimulate(5000) },
                colors = ButtonDefaults.buttonColors(containerColor = PremiumTheme.Green),
                modifier = Modifier.weight(1f)
            ) {
                Text("Deposit 5k")
            }

            Button(
                onClick = { viewModel.withdrawFundsSimulate(2000) },
                colors = ButtonDefaults.buttonColors(containerColor = PremiumTheme.Red),
                modifier = Modifier.weight(1f)
            ) {
                Text("Withdraw 2k")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("TRANSACTIONS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(txs) { tx ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PremiumTheme.CardBg, RoundedCornerShape(10.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(tx.type, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(tx.date, color = PremiumTheme.TextSecondary, fontSize = 11.sp)
                    }
                    Text(
                        tx.amount,
                        color = if (tx.amount.startsWith("+")) PremiumTheme.Green else PremiumTheme.Red,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

// ==========================================
// 15. SOCIAL FRIENDS & CHAT
// ==========================================
@Composable
fun FriendsScreen(viewModel: LudoViewModel) {
    val friends by viewModel.friendsList.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("MY FRIENDS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(bottom = 12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(friends) { friend ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PremiumTheme.CardBg, RoundedCornerShape(12.dp))
                        .border(1.dp, PremiumTheme.CardBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(PremiumTheme.Blue),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(friend.first.take(2).uppercase(), color = Color.White)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(friend.first, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text(friend.second, color = if (friend.second == "Online") PremiumTheme.Green else Color.Gray, fontSize = 12.sp)
                            }
                        }
                        IconButton(onClick = {
                            viewModel.activeChatFriend.value = friend.first
                            viewModel.navigateTo(Screen.Chat)
                        }) {
                            Icon(Icons.Filled.Chat, contentDescription = "Chat", tint = PremiumTheme.BlueGlow)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatScreen(viewModel: LudoViewModel) {
    val friend by viewModel.activeChatFriend.collectAsState()
    val chatLog by viewModel.chatHistory.collectAsState()
    var input by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PremiumTheme.BgStart)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(friend, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chatLog) { msg ->
                val alignment = if (msg.isMe) Alignment.CenterEnd else Alignment.CenterStart
                val bgColor = if (msg.isMe) PremiumTheme.Blue else PremiumTheme.CardBg
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = alignment
                ) {
                    Box(
                        modifier = Modifier
                            .background(bgColor, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                            .widthIn(max = 260.dp)
                    ) {
                        Column {
                            Text(msg.sender, color = PremiumTheme.Yellow, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text(msg.text, color = Color.White, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Write a message...") },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, focusedBorderColor = PremiumTheme.Blue),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = {
                if (input.isNotBlank()) {
                    viewModel.sendChatMessage(input)
                    input = ""
                }
            }) {
                Icon(Icons.Filled.Send, contentDescription = "Send", tint = PremiumTheme.Blue)
            }
        }
    }
}

// ==========================================
// 16. SETTINGS & SUPPORT SCREEN
// ==========================================
@Composable
fun SettingsScreen(viewModel: LudoViewModel) {
    val sound by viewModel.soundEnabled.collectAsState()
    val music by viewModel.musicEnabled.collectAsState()
    val notif by viewModel.notificationsEnabled.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text("Settings & Support", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Setting rows
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Sound Effects", color = Color.White)
                Switch(checked = sound, onCheckedChange = { viewModel.toggleSound() })
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Music Tracks", color = Color.White)
                Switch(checked = music, onCheckedChange = { viewModel.toggleMusic() })
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Push Notifications", color = Color.White)
                Switch(checked = notif, onCheckedChange = { viewModel.toggleNotifications() })
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Links
            OutlinedButton(
                onClick = { viewModel.navigateTo(Screen.HelpCenter) },
                border = BorderStroke(1.dp, PremiumTheme.CardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Help Center & Support", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { viewModel.navigateTo(Screen.ReportPlayer) },
                border = BorderStroke(1.dp, PremiumTheme.CardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Report Player", color = Color.White)
            }
        }

        Button(
            onClick = { viewModel.navigateTo(Screen.Auth) },
            colors = ButtonDefaults.buttonColors(containerColor = PremiumTheme.Red),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("LOG OUT")
        }
    }
}

@Composable
fun HelpCenterScreen(viewModel: LudoViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Settings) }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text("Help Center", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("FAQS", color = PremiumTheme.Yellow, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(12.dp))

        listOf(
            "How do I unlock new board skins?" to "Go to the Skins Store in the main dashboard menu and buy skins with Coins or Gems.",
            "Are games fully offline?" to "Deges Play features fully functional local offline mode and AI modes. Online modes require network.",
            "How do I claim weekly tournament prizes?" to "Brackets play out automatically and rewards are credited instantly to your Wallet."
        ).forEach { faq ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .background(PremiumTheme.CardBg, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(faq.first, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(faq.second, color = PremiumTheme.TextSecondary, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun ReportPlayerScreen(viewModel: LudoViewModel) {
    var name by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateTo(Screen.Settings) }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text("Report Cheater", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Cheater Name") },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, focusedBorderColor = PremiumTheme.Blue),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = reason,
                onValueChange = { reason = it },
                label = { Text("Reason (Cheating / Toxic behavior)") },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, focusedBorderColor = PremiumTheme.Blue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
        }

        Button(
            onClick = { viewModel.navigateTo(Screen.Dashboard) },
            colors = ButtonDefaults.buttonColors(containerColor = PremiumTheme.Blue),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("SUBMIT REPORT")
        }
    }
}

@Composable
fun PrivacyScreen(viewModel: LudoViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Auth) }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text("Privacy Policy", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Deges Play Privacy Policy",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            "We value your security. This application stores all match stats, user levels, coins, and themes safely on your device in local Room persistence databases. No personal data is shared without permission.",
            color = PremiumTheme.TextSecondary,
            fontSize = 14.sp
        )
    }
}

// Game Modes Selection View
@Composable
fun GameSetupScreen(viewModel: LudoViewModel) {
    // Unused but kept for completeness
}
