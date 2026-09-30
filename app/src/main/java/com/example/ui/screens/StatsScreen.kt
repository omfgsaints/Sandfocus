package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.FocusSessionEntity
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TableBorderLight
import com.example.ui.theme.TableOffWhite
import com.example.ui.theme.TableSurface
import com.example.ui.theme.ZenGoldAccent
import com.example.ui.theme.ZenSageAccent
import com.example.ui.theme.ZenSlateAccent
import com.example.ui.theme.ZenTerracottaAccent
import com.example.ui.theme.ZenTextMuted
import com.example.ui.theme.ZenTextPrimary
import com.example.ui.theme.ZenTextSecondary

@Composable
fun StatsScreen(
    sessions: List<FocusSessionEntity>,
    totalSeconds: Long,
    completedReelsCount: Int,
    modifier: Modifier = Modifier
) {
    val totalHours = totalSeconds / 3600
    val remainingMins = (totalSeconds % 3600) / 60
    val totalPatternsCarved = completedReelsCount
    val totalMetersRolled = (totalSeconds * 0.12f).toInt() // distance sphere rolled in meters

    val taskGroups = sessions.groupBy { it.taskName }
        .mapValues { entry -> entry.value.sumOf { it.actualSeconds } / 60 }
        .toList()
        .sortedByDescending { it.second }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TableOffWhite)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "ZEN FOCUS LEDGER",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = ZenTextPrimary,
                    letterSpacing = 1.sp
                )
            )
            Text(
                text = "MINDFUL PROGRESS & SPHERE TELEMETRY",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = ZenTextSecondary,
                    letterSpacing = 0.5.sp
                )
            )
        }

        // 4 Clean White Metric Cards
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    WhiteMetricCard(
                        title = "TOTAL FOCUS TIME",
                        value = "${totalHours}h ${remainingMins}m",
                        subtitle = "${totalSeconds / 60} Mindful Minutes",
                        icon = Icons.Default.HourglassBottom,
                        accentColor = ZenSlateAccent,
                        modifier = Modifier.weight(1f)
                    )
                    WhiteMetricCard(
                        title = "SAND SCULPTURES",
                        value = "$totalPatternsCarved",
                        subtitle = "Completed in Vault",
                        icon = Icons.Default.Spa,
                        accentColor = ZenSageAccent,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    WhiteMetricCard(
                        title = "SPHERE DISTANCE",
                        value = "$totalMetersRolled M",
                        subtitle = "Rolled Through Sand",
                        icon = Icons.Default.Timeline,
                        accentColor = ZenSlateAccent,
                        modifier = Modifier.weight(1f)
                    )
                    WhiteMetricCard(
                        title = "SESSION RATIO",
                        value = if (sessions.isNotEmpty()) {
                            "${(completedReelsCount * 100 / sessions.size)}%"
                        } else "100%",
                        subtitle = "${sessions.size} Sculptures Initiated",
                        icon = Icons.Default.Star,
                        accentColor = ZenTerracottaAccent,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Endeavor Breakdown
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
                    Text(
                        text = "ENDEAVORS DEDICATION",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = ZenTextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    if (taskGroups.isEmpty()) {
                        Text(
                            text = "No focus endeavors recorded yet.",
                            style = MaterialTheme.typography.bodySmall.copy(color = ZenTextSecondary)
                        )
                    } else {
                        taskGroups.forEach { (task, mins) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = task,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = ZenTextPrimary,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                                Text(
                                    text = "$mins Minutes",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = ZenSlateAccent,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun WhiteMetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, TableBorderLight),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = ZenTextSecondary,
                        letterSpacing = 0.5.sp,
                        fontSize = 10.sp
                    )
                )
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = ZenTextPrimary,
                    fontFamily = FontFamily.Serif
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = ZenTextSecondary,
                    fontSize = 11.sp
                )
            )
        }
    }
}
