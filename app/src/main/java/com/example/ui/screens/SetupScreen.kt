package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ReelTheme
import com.example.ui.audio.AmbientSoundType
import com.example.ui.sand.SandLightingMode
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TableBorderLight
import com.example.ui.theme.TableBorderMedium
import com.example.ui.theme.TableOffWhite
import com.example.ui.theme.TableSurface
import com.example.ui.theme.ZenGoldAccent
import com.example.ui.theme.ZenSageAccent
import com.example.ui.theme.ZenSlateAccent
import com.example.ui.theme.ZenTextMuted
import com.example.ui.theme.ZenTextPrimary
import com.example.ui.theme.ZenTextSecondary
import com.example.ui.viewmodel.FocusUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SetupScreen(
    uiState: FocusUiState,
    onTaskNameChange: (String) -> Unit,
    onMinutesChange: (Int) -> Unit,
    onThemeChange: (ReelTheme) -> Unit,
    onRandomThemeToggle: (Boolean) -> Unit,
    onStrictLockToggle: (Boolean) -> Unit,
    onKeepScreenOnToggle: (Boolean) -> Unit,
    onSoundTypeChange: (AmbientSoundType) -> Unit,
    onSoundVolumeChange: (Float) -> Unit,
    onLightingModeChange: (SandLightingMode) -> Unit,
    onStartFocus: () -> Unit,
    modifier: Modifier = Modifier
) {
    val quickDurations = listOf(10, 15, 25, 45, 60, 90)
    val taskSuggestions = listOf("Deep Flow", "Mindful Reading", "Quiet Study", "Creative Writing", "Zen Coding", "Meditation")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TableOffWhite)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(14.dp))
            // Pristine White Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, TableBorderLight),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Spa,
                            contentDescription = "Zen",
                            tint = ZenSageAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SANDFOCUS",
                            style = MaterialTheme.typography.labelMedium.copy(
                                letterSpacing = 3.sp,
                                color = ZenSlateAccent,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Kinetic Sand Art",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = ZenTextPrimary,
                            fontFamily = FontFamily.Serif,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Text(
                        text = "Fine white silica sand sculpted by a magnetic sphere as you focus",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = ZenTextSecondary,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }

        // Section 1: Focus Endeavor
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, TableBorderLight),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "Task",
                            tint = ZenSlateAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Focus Endeavor",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = ZenTextPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = uiState.taskName,
                        onValueChange = onTaskNameChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("task_name_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ZenSlateAccent,
                            unfocusedBorderColor = TableBorderLight,
                            focusedTextColor = ZenTextPrimary,
                            unfocusedTextColor = ZenTextPrimary,
                            focusedContainerColor = TableSurface,
                            unfocusedContainerColor = TableSurface
                        ),
                        placeholder = { Text("What will you dedicate this session to?", color = ZenTextMuted) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        taskSuggestions.forEach { task ->
                            FilterChip(
                                selected = uiState.taskName == task,
                                onClick = { onTaskNameChange(task) },
                                label = { Text(task, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ZenSlateAccent,
                                    selectedLabelColor = PureWhite,
                                    containerColor = TableSurface,
                                    labelColor = ZenTextSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = uiState.taskName == task,
                                    borderColor = if (uiState.taskName == task) ZenSlateAccent else TableBorderLight
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }
                }
            }
        }

        // Section 2: Session Duration
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, TableBorderLight),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = "Duration",
                                tint = ZenSlateAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Session Duration",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = ZenTextPrimary
                                )
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(TableSurface)
                                .border(1.dp, TableBorderLight, RoundedCornerShape(12.dp))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${uiState.targetMinutes} MIN",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ZenTextPrimary,
                                    letterSpacing = 1.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Slider(
                        value = uiState.targetMinutes.toFloat(),
                        onValueChange = { onMinutesChange(it.toInt()) },
                        valueRange = 5f..120f,
                        steps = 22,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("duration_slider"),
                        colors = SliderDefaults.colors(
                            thumbColor = ZenSlateAccent,
                            activeTrackColor = ZenSlateAccent,
                            inactiveTrackColor = TableBorderLight
                        )
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        quickDurations.forEach { mins ->
                            FilterChip(
                                selected = uiState.targetMinutes == mins,
                                onClick = { onMinutesChange(mins) },
                                label = { Text("${mins}m", fontSize = 12.sp) },
                                modifier = Modifier.weight(1f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ZenSlateAccent,
                                    selectedLabelColor = PureWhite,
                                    containerColor = TableSurface,
                                    labelColor = ZenTextSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = uiState.targetMinutes == mins,
                                    borderColor = if (uiState.targetMinutes == mins) ZenSlateAccent else TableBorderLight
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }
                }
            }
        }

        // Section 3: Kinetic Sand Pattern Selection
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, TableBorderLight),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Pattern",
                                tint = ZenSlateAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Sand Pattern",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = ZenTextPrimary
                                )
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (uiState.isRandomTheme) "Mystery" else "Selected",
                                style = MaterialTheme.typography.labelSmall.copy(color = ZenTextSecondary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Switch(
                                checked = uiState.isRandomTheme,
                                onCheckedChange = onRandomThemeToggle,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = PureWhite,
                                    checkedTrackColor = ZenSlateAccent,
                                    uncheckedThumbColor = TableBorderMedium,
                                    uncheckedTrackColor = TableBorderLight
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (uiState.isRandomTheme) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(TableSurface)
                                .border(1.dp, TableBorderLight, RoundedCornerShape(12.dp))
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.Casino,
                                    contentDescription = "Random",
                                    tint = ZenSageAccent,
                                    modifier = Modifier.size(26.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Mystery Sand Sculpture",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = ZenTextPrimary
                                        )
                                    )
                                    Text(
                                        text = "A tranquil sacred geometry or dune ripple pattern will be chosen at random!",
                                        style = MaterialTheme.typography.bodySmall.copy(color = ZenTextSecondary)
                                    )
                                }
                            }
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            ReelTheme.ALL_THEMES.forEach { theme ->
                                val isSelected = uiState.currentTheme.id == theme.id
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) TableSurface else PureWhite)
                                        .border(
                                            1.dp,
                                            if (isSelected) ZenSlateAccent else TableBorderLight,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable { onThemeChange(theme) }
                                        .padding(14.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = theme.title,
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = ZenTextPrimary
                                                )
                                            )
                                            Text(
                                                text = theme.subtitle,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = ZenTextSecondary,
                                                    fontSize = 11.sp
                                                )
                                            )
                                        }
                                        if (isSelected) {
                                            Box(
                                                modifier = Modifier
                                                    .size(10.dp)
                                                    .background(ZenSlateAccent, CircleShape)
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

        // Section 4: Relaxing Sound & Ambience
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, TableBorderLight),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = "Music",
                            tint = ZenSlateAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Relaxing Background Ambience",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = ZenTextPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    AmbientSoundType.entries.forEach { soundType ->
                        val isSelected = uiState.soundType == soundType
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) TableSurface else PureWhite)
                                .border(
                                    1.dp,
                                    if (isSelected) ZenSlateAccent else TableBorderLight,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onSoundTypeChange(soundType) }
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = soundType.displayName,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = ZenTextPrimary
                                        )
                                    )
                                    Text(
                                        text = soundType.description,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = ZenTextSecondary,
                                            fontSize = 11.sp
                                        )
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

                    if (uiState.soundType != AmbientSoundType.SILENT) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Volume", style = MaterialTheme.typography.labelMedium.copy(color = ZenTextSecondary))
                            Spacer(modifier = Modifier.width(12.dp))
                            Slider(
                                value = uiState.soundVolume,
                                onValueChange = onSoundVolumeChange,
                                modifier = Modifier.weight(1f),
                                colors = SliderDefaults.colors(
                                    thumbColor = ZenSlateAccent,
                                    activeTrackColor = ZenSlateAccent,
                                    inactiveTrackColor = TableBorderLight
                                )
                            )
                        }
                    }
                }
            }
        }

        // Section 5: Ambient LED Side Lighting
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, TableBorderLight),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = "Light",
                            tint = ZenGoldAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sand Table Lighting",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = ZenTextPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SandLightingMode.entries.forEach { mode ->
                            val isSelected = uiState.lightingMode == mode
                            FilterChip(
                                selected = isSelected,
                                onClick = { onLightingModeChange(mode) },
                                label = { Text(mode.displayName.split(" ")[0], fontSize = 12.sp) },
                                modifier = Modifier.weight(1f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ZenSlateAccent,
                                    selectedLabelColor = PureWhite,
                                    containerColor = TableSurface,
                                    labelColor = ZenTextSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = if (isSelected) ZenSlateAccent else TableBorderLight
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }
                }
            }
        }

        // Section 6: Strict Lock & Desk Stand Awake
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, TableBorderLight),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Lock",
                                tint = ZenSlateAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Strict Phone Lock Mode",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = ZenTextPrimary
                                    )
                                )
                                Text(
                                    text = "Keeps phone locked to prevent distractions. 5-sec hold to emergency unlock.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = ZenTextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                        Switch(
                            checked = uiState.strictLockEnabled,
                            onCheckedChange = onStrictLockToggle,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = PureWhite,
                                checkedTrackColor = ZenSlateAccent,
                                uncheckedThumbColor = TableBorderMedium,
                                uncheckedTrackColor = TableBorderLight
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tv,
                                contentDescription = "Screen Awake",
                                tint = ZenSlateAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Keep Screen Awake (Sand Table Mode)",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = ZenTextPrimary
                                    )
                                )
                                Text(
                                    text = "Phone sits on your desk as an illuminated white kinetic sand table.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = ZenTextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                        Switch(
                            checked = uiState.keepScreenOn,
                            onCheckedChange = onKeepScreenOnToggle,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = PureWhite,
                                checkedTrackColor = ZenSlateAccent,
                                uncheckedThumbColor = TableBorderMedium,
                                uncheckedTrackColor = TableBorderLight
                            )
                        )
                    }
                }
            }
        }

        // Section 7: Action Button
        item {
            Button(
                onClick = onStartFocus,
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp)
                    .height(56.dp)
                    .testTag("start_focus_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ZenSlateAccent,
                    contentColor = PureWhite
                ),
                shape = RoundedCornerShape(14.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start",
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "BEGIN KINETIC SAND SCULPTURE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
