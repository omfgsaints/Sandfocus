package com.example

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.CompletedScreen
import com.example.ui.screens.FocusScreen
import com.example.ui.screens.GalleryScreen
import com.example.ui.screens.SetupScreen
import com.example.ui.screens.StatsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TableBorderLight
import com.example.ui.theme.TableOffWhite
import com.example.ui.theme.TableSurface
import com.example.ui.theme.ZenSlateAccent
import com.example.ui.theme.ZenTextMuted
import com.example.ui.theme.ZenTextPrimary
import com.example.ui.theme.ZenTextSecondary
import com.example.ui.viewmodel.FocusState
import com.example.ui.viewmodel.FocusViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val focusViewModel: FocusViewModel = viewModel()
                val uiState by focusViewModel.uiState.collectAsStateWithLifecycle()
                val allSessions by focusViewModel.allSessions.collectAsStateWithLifecycle()
                val totalSeconds by focusViewModel.totalFocusSeconds.collectAsStateWithLifecycle()
                val completedReelsCount by focusViewModel.completedReelsCount.collectAsStateWithLifecycle()

                // Keep screen awake during Sand Table focus session if configured
                DisposableEffect(uiState.focusState, uiState.keepScreenOn) {
                    if (uiState.focusState == FocusState.RUNNING && uiState.keepScreenOn) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    } else {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    }
                    onDispose {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    }
                }

                var selectedTab by remember { mutableIntStateOf(0) }

                when (uiState.focusState) {
                    FocusState.RUNNING, FocusState.PAUSED -> {
                        // Fullscreen immersive Kinetic Sand Art session (adaptive for portrait and desktop/landscape)
                        FocusScreen(
                            uiState = uiState,
                            onPauseToggle = {
                                if (uiState.focusState == FocusState.RUNNING) {
                                    focusViewModel.pauseSession()
                                } else {
                                    focusViewModel.resumeSession()
                                }
                            },
                            onShowGiveUpWarning = { focusViewModel.showGiveUpWarning() },
                            onDismissGiveUpWarning = { focusViewModel.dismissGiveUpWarning() },
                            onStartEmergencyHold = { focusViewModel.startEmergencyReleaseHold() },
                            onCancelEmergencyHold = { focusViewModel.cancelEmergencyReleaseHold() },
                            onAbortSession = { focusViewModel.abortSession() },
                            onSoundTypeChange = { focusViewModel.setSoundType(it) },
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("focus_screen")
                        )
                    }

                    FocusState.COMPLETED -> {
                        // Completed sculpture celebration screen
                        CompletedScreen(
                            uiState = uiState,
                            onNoteChange = { focusViewModel.updateCompletionNote(it) },
                            onViewGallery = {
                                focusViewModel.resetToSetup()
                                selectedTab = 1
                            },
                            onNewSession = { focusViewModel.resetToSetup() },
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("completed_screen")
                        )
                    }

                    FocusState.SETUP -> {
                        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                            val isDesktopOrWide = maxWidth >= 720.dp

                            if (isDesktopOrWide) {
                                // Desktop / Windows PC Layout with Navigation Rail
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(TableOffWhite)
                                ) {
                                    NavigationRail(
                                        containerColor = PureWhite,
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .border(1.dp, TableBorderLight)
                                            .testTag("desktop_nav_rail"),
                                        header = {
                                            Spacer(modifier = Modifier.height(18.dp))
                                            Icon(
                                                imageVector = Icons.Default.Spa,
                                                contentDescription = "Logo",
                                                tint = ZenSlateAccent
                                            )
                                            Spacer(modifier = Modifier.height(18.dp))
                                        }
                                    ) {
                                        NavigationRailItem(
                                            selected = selectedTab == 0,
                                            onClick = { selectedTab = 0 },
                                            icon = {
                                                Icon(
                                                    imageVector = Icons.Default.Spa,
                                                    contentDescription = "Sand Table"
                                                )
                                            },
                                            label = { Text("Table", fontSize = 11.sp, fontWeight = if (selectedTab == 0) FontWeight.SemiBold else FontWeight.Normal) },
                                            colors = NavigationRailItemDefaults.colors(
                                                selectedIconColor = ZenTextPrimary,
                                                selectedTextColor = ZenTextPrimary,
                                                unselectedIconColor = ZenTextMuted,
                                                unselectedTextColor = ZenTextMuted,
                                                indicatorColor = TableSurface
                                            )
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        NavigationRailItem(
                                            selected = selectedTab == 1,
                                            onClick = { selectedTab = 1 },
                                            icon = {
                                                Icon(
                                                    imageVector = Icons.Default.Collections,
                                                    contentDescription = "Sand Vault"
                                                )
                                            },
                                            label = { Text("Vault", fontSize = 11.sp, fontWeight = if (selectedTab == 1) FontWeight.SemiBold else FontWeight.Normal) },
                                            colors = NavigationRailItemDefaults.colors(
                                                selectedIconColor = ZenTextPrimary,
                                                selectedTextColor = ZenTextPrimary,
                                                unselectedIconColor = ZenTextMuted,
                                                unselectedTextColor = ZenTextMuted,
                                                indicatorColor = TableSurface
                                            )
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        NavigationRailItem(
                                            selected = selectedTab == 2,
                                            onClick = { selectedTab = 2 },
                                            icon = {
                                                Icon(
                                                    imageVector = Icons.Default.QueryStats,
                                                    contentDescription = "Ledger"
                                                )
                                            },
                                            label = { Text("Ledger", fontSize = 11.sp, fontWeight = if (selectedTab == 2) FontWeight.SemiBold else FontWeight.Normal) },
                                            colors = NavigationRailItemDefaults.colors(
                                                selectedIconColor = ZenTextPrimary,
                                                selectedTextColor = ZenTextPrimary,
                                                unselectedIconColor = ZenTextMuted,
                                                unselectedTextColor = ZenTextMuted,
                                                indicatorColor = TableSurface
                                            )
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                    ) {
                                        when (selectedTab) {
                                            0 -> SetupScreen(
                                                uiState = uiState,
                                                onTaskNameChange = { focusViewModel.setTaskName(it) },
                                                onMinutesChange = { focusViewModel.setTargetMinutes(it) },
                                                onThemeChange = { focusViewModel.setTheme(it) },
                                                onRandomThemeToggle = { focusViewModel.setRandomTheme(it) },
                                                onStrictLockToggle = { focusViewModel.setStrictLock(it) },
                                                onKeepScreenOnToggle = { focusViewModel.setKeepScreenOn(it) },
                                                onSoundTypeChange = { focusViewModel.setSoundType(it) },
                                                onSoundVolumeChange = { focusViewModel.setSoundVolume(it) },
                                                onLightingModeChange = { focusViewModel.setLightingMode(it) },
                                                onStartFocus = { focusViewModel.startFocusSession() },
                                                modifier = Modifier.fillMaxSize()
                                            )
                                            1 -> GalleryScreen(
                                                sessions = allSessions,
                                                onDeleteSession = { focusViewModel.deleteSession(it) },
                                                modifier = Modifier.fillMaxSize()
                                            )
                                            2 -> StatsScreen(
                                                sessions = allSessions,
                                                totalSeconds = totalSeconds ?: 0L,
                                                completedReelsCount = completedReelsCount,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        }
                                    }
                                }
                            } else {
                                // Phone / Mobile Layout with Bottom Navigation Bar
                                Scaffold(
                                    modifier = Modifier.fillMaxSize(),
                                    containerColor = TableOffWhite,
                                    bottomBar = {
                                        NavigationBar(
                                            containerColor = PureWhite,
                                            tonalElevation = 3.dp,
                                            modifier = Modifier
                                                .border(1.dp, TableBorderLight)
                                                .testTag("bottom_nav_bar")
                                        ) {
                                            NavigationBarItem(
                                                selected = selectedTab == 0,
                                                onClick = { selectedTab = 0 },
                                                icon = {
                                                    Icon(
                                                        imageVector = Icons.Default.Spa,
                                                        contentDescription = "Sand Table"
                                                    )
                                                },
                                                label = { Text("Sand Table", fontSize = 11.sp, fontWeight = if (selectedTab == 0) FontWeight.SemiBold else FontWeight.Normal) },
                                                colors = NavigationBarItemDefaults.colors(
                                                    selectedIconColor = ZenTextPrimary,
                                                    selectedTextColor = ZenTextPrimary,
                                                    unselectedIconColor = ZenTextMuted,
                                                    unselectedTextColor = ZenTextMuted,
                                                    indicatorColor = TableSurface
                                                )
                                            )

                                            NavigationBarItem(
                                                selected = selectedTab == 1,
                                                onClick = { selectedTab = 1 },
                                                icon = {
                                                    Icon(
                                                        imageVector = Icons.Default.Collections,
                                                        contentDescription = "Sand Vault"
                                                    )
                                                },
                                                label = { Text("Sand Vault", fontSize = 11.sp, fontWeight = if (selectedTab == 1) FontWeight.SemiBold else FontWeight.Normal) },
                                                colors = NavigationBarItemDefaults.colors(
                                                    selectedIconColor = ZenTextPrimary,
                                                    selectedTextColor = ZenTextPrimary,
                                                    unselectedIconColor = ZenTextMuted,
                                                    unselectedTextColor = ZenTextMuted,
                                                    indicatorColor = TableSurface
                                                )
                                            )

                                            NavigationBarItem(
                                                selected = selectedTab == 2,
                                                onClick = { selectedTab = 2 },
                                                icon = {
                                                    Icon(
                                                        imageVector = Icons.Default.QueryStats,
                                                        contentDescription = "Ledger"
                                                    )
                                                },
                                                label = { Text("Ledger", fontSize = 11.sp, fontWeight = if (selectedTab == 2) FontWeight.SemiBold else FontWeight.Normal) },
                                                colors = NavigationBarItemDefaults.colors(
                                                    selectedIconColor = ZenTextPrimary,
                                                    selectedTextColor = ZenTextPrimary,
                                                    unselectedIconColor = ZenTextMuted,
                                                    unselectedTextColor = ZenTextMuted,
                                                    indicatorColor = TableSurface
                                                )
                                            )
                                        }
                                    }
                                ) { paddingValues ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(paddingValues)
                                    ) {
                                        when (selectedTab) {
                                            0 -> SetupScreen(
                                                uiState = uiState,
                                                onTaskNameChange = { focusViewModel.setTaskName(it) },
                                                onMinutesChange = { focusViewModel.setTargetMinutes(it) },
                                                onThemeChange = { focusViewModel.setTheme(it) },
                                                onRandomThemeToggle = { focusViewModel.setRandomTheme(it) },
                                                onStrictLockToggle = { focusViewModel.setStrictLock(it) },
                                                onKeepScreenOnToggle = { focusViewModel.setKeepScreenOn(it) },
                                                onSoundTypeChange = { focusViewModel.setSoundType(it) },
                                                onSoundVolumeChange = { focusViewModel.setSoundVolume(it) },
                                                onLightingModeChange = { focusViewModel.setLightingMode(it) },
                                                onStartFocus = { focusViewModel.startFocusSession() },
                                                modifier = Modifier.fillMaxSize()
                                            )
                                            1 -> GalleryScreen(
                                                sessions = allSessions,
                                                onDeleteSession = { focusViewModel.deleteSession(it) },
                                                modifier = Modifier.fillMaxSize()
                                            )
                                            2 -> StatsScreen(
                                                sessions = allSessions,
                                                totalSeconds = totalSeconds ?: 0L,
                                                completedReelsCount = completedReelsCount,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
