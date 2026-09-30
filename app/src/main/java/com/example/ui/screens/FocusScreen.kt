package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.audio.AmbientSoundType
import com.example.ui.sand.FullScreenSandTable
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TableBorderLight
import com.example.ui.theme.TableBorderMedium
import com.example.ui.theme.TableOffWhite
import com.example.ui.theme.TableSurface
import com.example.ui.theme.ZenGoldAccent
import com.example.ui.theme.ZenSageAccent
import com.example.ui.theme.ZenSlateAccent
import com.example.ui.theme.ZenTerracottaAccent
import com.example.ui.theme.ZenTextMuted
import com.example.ui.theme.ZenTextPrimary
import com.example.ui.theme.ZenTextSecondary
import com.example.ui.viewmodel.FocusState
import com.example.ui.viewmodel.FocusUiState

@Composable
fun FocusScreen(
    uiState: FocusUiState,
    onPauseToggle: () -> Unit,
    onShowGiveUpWarning: () -> Unit,
    onDismissGiveUpWarning: () -> Unit,
    onStartEmergencyHold: () -> Unit,
    onCancelEmergencyHold: () -> Unit,
    onAbortSession: () -> Unit,
    onSoundTypeChange: (AmbientSoundType) -> Unit,
    modifier: Modifier = Modifier
) {
    // Intercept hardware/gesture Back only if session is active
    BackHandler(enabled = uiState.focusState == FocusState.RUNNING || uiState.focusState == FocusState.PAUSED) {
        if (uiState.strictLockEnabled) {
            onShowGiveUpWarning()
        } else {
            onAbortSession()
        }
    }

    var showSoundDialog by remember { mutableStateOf(false) }
    var isZenMinimalMode by remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(TableOffWhite)
    ) {
        val isLandscapeOrTablet = maxWidth >= 600.dp || maxWidth > maxHeight

        // 1. FULL-SCREEN WHITE KINETIC SAND ART CANVAS (Runs smoothly with hardware VSYNC)
        FullScreenSandTable(
            theme = uiState.currentTheme,
            progress = uiState.progress,
            isRunning = uiState.focusState == FocusState.RUNNING,
            isFullScreen = true,
            lightingMode = uiState.lightingMode,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Soft Ambient Vignette (Keeps floating text crisp while sand shines through)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (isZenMinimalMode) 0.05f else 0.45f),
                            Color.Transparent,
                            Color.Transparent,
                            Color.White.copy(alpha = if (isZenMinimalMode) 0.1f else 0.5f)
                        )
                    )
                )
        )

        // 3. Floating Scandinavian Frosted White HUD
        AnimatedVisibility(
            visible = !isZenMinimalMode,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            if (isLandscapeOrTablet) {
                LandscapeWhiteSandHUD(
                    uiState = uiState,
                    onPauseToggle = onPauseToggle,
                    onExitSession = {
                        if (uiState.strictLockEnabled) onShowGiveUpWarning() else onAbortSession()
                    },
                    onStartEmergencyHold = onStartEmergencyHold,
                    onCancelEmergencyHold = onCancelEmergencyHold,
                    onOpenSoundDialog = { showSoundDialog = true },
                    onToggleMinimal = { isZenMinimalMode = true }
                )
            } else {
                PortraitWhiteSandHUD(
                    uiState = uiState,
                    onPauseToggle = onPauseToggle,
                    onExitSession = {
                        if (uiState.strictLockEnabled) onShowGiveUpWarning() else onAbortSession()
                    },
                    onStartEmergencyHold = onStartEmergencyHold,
                    onCancelEmergencyHold = onCancelEmergencyHold,
                    onOpenSoundDialog = { showSoundDialog = true },
                    onToggleMinimal = { isZenMinimalMode = true }
                )
            }
        }

        // Minimalist Clock Indicator when in Zen Minimal Mode
        if (isZenMinimalMode) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 28.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shadow(4.dp, RoundedCornerShape(20.dp))
                    .background(PureWhite.copy(alpha = 0.95f))
                    .border(1.dp, TableBorderLight, RoundedCornerShape(20.dp))
                    .clickable { isZenMinimalMode = false }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = uiState.formattedRemainingTime,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = ZenTextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Show Controls",
                        tint = ZenTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "SHOW CONTROLS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ZenTextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }

        // Give Up / Reset Sand Bed Warning Dialog
        if (uiState.showGiveUpDialog) {
            AlertDialog(
                onDismissRequest = onDismissGiveUpWarning,
                containerColor = PureWhite,
                shape = RoundedCornerShape(16.dp),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Warning",
                            tint = ZenTerracottaAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "EXIT SESSION?",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = ZenTextPrimary,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Your kinetic sphere is currently carving \"${uiState.currentTheme.title}\" into white silica sand.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = ZenTextPrimary)
                        )
                        Text(
                            text = "Progress: ${(uiState.progress * 100).toInt()}% completed. Would you like to keep focusing or return to setup?",
                            style = MaterialTheme.typography.bodySmall.copy(color = ZenTextSecondary)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = onDismissGiveUpWarning,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ZenSlateAccent,
                            contentColor = PureWhite
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("RESUME FOCUS", fontWeight = FontWeight.SemiBold)
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = {
                            onDismissGiveUpWarning()
                            onAbortSession()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ZenTerracottaAccent),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TableBorderLight),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("EXIT TO SETUP")
                    }
                }
            )
        }

        // Relaxing Sound Selector Dialog
        if (showSoundDialog) {
            AlertDialog(
                onDismissRequest = { showSoundDialog = false },
                containerColor = PureWhite,
                shape = RoundedCornerShape(16.dp),
                title = {
                    Text(
                        text = "RELAXING BACKGROUND AMBIENCE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ZenTextPrimary,
                            letterSpacing = 0.5.sp
                        )
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        AmbientSoundType.entries.forEach { type ->
                            val isSelected = uiState.soundType == type
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) TableSurface else PureWhite)
                                    .border(
                                        1.dp,
                                        if (isSelected) ZenSlateAccent else TableBorderLight,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable {
                                        onSoundTypeChange(type)
                                        showSoundDialog = false
                                    }
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = type.displayName,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (isSelected) ZenTextPrimary else ZenTextSecondary,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = type.description,
                                            color = ZenTextMuted,
                                            fontSize = 11.sp
                                        )
                                    }
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.VolumeUp,
                                            contentDescription = "Active",
                                            tint = ZenSlateAccent,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showSoundDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ZenSlateAccent,
                            contentColor = PureWhite
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("CLOSE")
                    }
                }
            )
        }
    }
}

@Composable
private fun PortraitWhiteSandHUD(
    uiState: FocusUiState,
    onPauseToggle: () -> Unit,
    onExitSession: () -> Unit,
    onStartEmergencyHold: () -> Unit,
    onCancelEmergencyHold: () -> Unit,
    onOpenSoundDialog: () -> Unit,
    onToggleMinimal: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Floating Action Bar
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Exit Button
                IconButton(
                    onClick = onExitSession,
                    modifier = Modifier
                        .shadow(4.dp, CircleShape)
                        .background(PureWhite.copy(alpha = 0.95f), CircleShape)
                        .border(1.dp, TableBorderLight, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Exit to Menu",
                        tint = ZenTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Task Name Floating Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .shadow(4.dp, RoundedCornerShape(20.dp))
                        .background(PureWhite.copy(alpha = 0.95f))
                        .border(1.dp, TableBorderLight, RoundedCornerShape(20.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = uiState.taskName.uppercase(),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ZenTextPrimary,
                            letterSpacing = 1.sp
                        )
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Sound Button
                    IconButton(
                        onClick = onOpenSoundDialog,
                        modifier = Modifier
                            .shadow(4.dp, CircleShape)
                            .background(PureWhite.copy(alpha = 0.95f), CircleShape)
                            .border(1.dp, TableBorderLight, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = "Sound Settings",
                            tint = ZenSlateAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Zen Minimal View Button
                    IconButton(
                        onClick = onToggleMinimal,
                        modifier = Modifier
                            .shadow(4.dp, CircleShape)
                            .background(PureWhite.copy(alpha = 0.95f), CircleShape)
                            .border(1.dp, TableBorderLight, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VisibilityOff,
                            contentDescription = "Hide HUD",
                            tint = ZenTextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Time Display with High Legibility
            Text(
                text = uiState.formattedRemainingTime,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Light,
                    color = ZenTextPrimary,
                    letterSpacing = 3.sp,
                    fontFamily = FontFamily.Monospace
                ),
                modifier = Modifier.testTag("time_countdown")
            )

            Text(
                text = "CARVING: ${uiState.currentTheme.title.uppercase()}",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 1.5.sp,
                    color = ZenTextSecondary
                )
            )
        }

        // Center: Full-Screen White Sand Table remains in full glory
        Spacer(modifier = Modifier.weight(1f))

        // Bottom Floating Controls
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // White Sand progress pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(4.dp, RoundedCornerShape(16.dp))
                    .background(PureWhite.copy(alpha = 0.95f))
                    .border(1.dp, TableBorderLight, RoundedCornerShape(16.dp))
                    .padding(horizontal = 18.dp, vertical = 12.dp)
                    .fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "PATTERN PROGRESS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ZenTextPrimary,
                                letterSpacing = 1.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "${(uiState.progress * 100).toInt()}% CARVED",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ZenTextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { uiState.progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = ZenSlateAccent,
                        trackColor = TableBorderLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Emergency Hold or Controls
            if (uiState.strictLockEnabled) {
                WhiteEmergencyHoldButton(
                    holdProgress = uiState.emergencyHoldProgress,
                    onStartHold = onStartEmergencyHold,
                    onCancelHold = onCancelEmergencyHold,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onPauseToggle,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PureWhite,
                            contentColor = ZenTextPrimary
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TableBorderLight),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .shadow(2.dp, RoundedCornerShape(12.dp))
                    ) {
                        Icon(
                            imageVector = if (uiState.focusState == FocusState.RUNNING) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Pause"
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (uiState.focusState == FocusState.RUNNING) "PAUSE" else "RESUME")
                    }

                    OutlinedButton(
                        onClick = onExitSession,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ZenTextPrimary),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TableBorderLight),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("EXIT SESSION")
                    }
                }
            }
        }
    }
}

@Composable
private fun LandscapeWhiteSandHUD(
    uiState: FocusUiState,
    onPauseToggle: () -> Unit,
    onExitSession: () -> Unit,
    onStartEmergencyHold: () -> Unit,
    onCancelEmergencyHold: () -> Unit,
    onOpenSoundDialog: () -> Unit,
    onToggleMinimal: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Floating Left Card with Telemetry
        Card(
            modifier = Modifier
                .widthIn(max = 320.dp)
                .fillMaxHeight(),
            colors = CardDefaults.cardColors(containerColor = PureWhite.copy(alpha = 0.95f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, TableBorderLight),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onExitSession) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = ZenTextPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(TableSurface)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = uiState.taskName.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = ZenTextPrimary,
                                letterSpacing = 1.sp
                            )
                        )
                    }

                    Row {
                        IconButton(onClick = onOpenSoundDialog) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = "Sound",
                                tint = ZenSlateAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        IconButton(onClick = onToggleMinimal) {
                            Icon(
                                imageVector = Icons.Default.VisibilityOff,
                                contentDescription = "Hide HUD",
                                tint = ZenTextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Time Readout
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = uiState.formattedRemainingTime,
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Light,
                            color = ZenTextPrimary,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 2.sp
                        ),
                        modifier = Modifier.testTag("time_countdown_landscape")
                    )
                    Text(
                        text = uiState.currentTheme.title.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ZenTextSecondary,
                            letterSpacing = 1.5.sp
                        )
                    )
                }

                // Progress & Emergency button
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "CARVED",
                            style = MaterialTheme.typography.labelSmall.copy(color = ZenTextPrimary, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${(uiState.progress * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall.copy(color = ZenTextSecondary)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { uiState.progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = ZenSlateAccent,
                        trackColor = TableBorderLight
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (uiState.strictLockEnabled) {
                        WhiteEmergencyHoldButton(
                            holdProgress = uiState.emergencyHoldProgress,
                            onStartHold = onStartEmergencyHold,
                            onCancelHold = onCancelEmergencyHold,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = onPauseToggle,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = TableSurface,
                                    contentColor = ZenTextPrimary
                                ),
                                border = androidx.compose.foundation.BorderStroke(1.dp, TableBorderLight),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(if (uiState.focusState == FocusState.RUNNING) "PAUSE" else "RESUME")
                            }

                            OutlinedButton(
                                onClick = onExitSession,
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ZenTextPrimary),
                                border = androidx.compose.foundation.BorderStroke(1.dp, TableBorderLight),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("EXIT")
                            }
                        }
                    }
                }
            }
        }

        // Empty right area: Fullscreen white sand table takes pride of place
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun WhiteEmergencyHoldButton(
    holdProgress: Float,
    onStartHold: () -> Unit,
    onCancelHold: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isHolding = holdProgress > 0f

    Box(
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .background(PureWhite.copy(alpha = 0.95f))
            .border(
                1.dp,
                if (isHolding) ZenTerracottaAccent else TableBorderLight,
                RoundedCornerShape(12.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onStartHold()
                        tryAwaitRelease()
                        onCancelHold()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (holdProgress > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(holdProgress)
                    .align(Alignment.CenterStart)
                    .background(ZenTerracottaAccent.copy(alpha = 0.2f))
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Lock",
                tint = if (isHolding) ZenTerracottaAccent else ZenTextSecondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isHolding) {
                    "RELEASING... ${(holdProgress * 100).toInt()}%"
                } else {
                    "HOLD 5s FOR EMERGENCY UNLOCK"
                },
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = if (isHolding) ZenTerracottaAccent else ZenTextSecondary,
                    letterSpacing = 0.5.sp,
                    fontSize = 11.sp
                )
            )
        }
    }
}
