package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.FocusSessionEntity
import com.example.data.model.ReelTheme
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GalleryScreen(
    sessions: List<FocusSessionEntity>,
    onDeleteSession: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSession by remember { mutableStateOf<FocusSessionEntity?>(null) }
    val dateFormat = remember { SimpleDateFormat("MMM d, yyyy · HH:mm", Locale.getDefault()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TableOffWhite)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Vault Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "SAND SCULPTURE VAULT",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = ZenTextPrimary,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "${sessions.size} PRESERVED KINETIC SCULPTURES",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ZenTextSecondary,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (sessions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TableBorderLight),
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Spa,
                            contentDescription = "Empty",
                            tint = TableBorderMedium,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No Sand Sculptures Yet",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = ZenTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Begin a focus session to sculpt your first white kinetic sand artwork!",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = ZenTextSecondary
                            ),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.testTag("gallery_grid")
            ) {
                items(sessions, key = { it.id }) { session ->
                    val theme = ReelTheme.getById(session.reelThemeId)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { selectedSession = session },
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (session.isSuccessful) TableBorderLight else ZenTerracottaAccent.copy(alpha = 0.4f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            // Mini White Kinetic Sand Table preview
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                FullScreenSandTable(
                                    theme = theme,
                                    progress = session.drawingProgress,
                                    isRunning = false,
                                    isFullScreen = false,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = session.reelTitle,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = ZenTextPrimary,
                                    fontSize = 13.sp
                                ),
                                maxLines = 1
                            )

                            Text(
                                text = session.taskName,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = ZenTextSecondary,
                                    fontSize = 11.sp
                                ),
                                maxLines = 1
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${session.targetMinutes}m",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = ZenSlateAccent,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = if (session.isSuccessful) "100% CARVED" else "${(session.drawingProgress * 100).toInt()}% PARTIAL",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (session.isSuccessful) ZenSageAccent else ZenTerracottaAccent,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Full Screen Sand Sculpture Inspector Dialog
        selectedSession?.let { session ->
            val theme = ReelTheme.getById(session.reelThemeId)
            AlertDialog(
                onDismissRequest = { selectedSession = null },
                containerColor = PureWhite,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth(0.95f),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = session.reelTitle,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ZenTextPrimary,
                                    fontFamily = FontFamily.Serif
                                )
                            )
                            Text(
                                text = dateFormat.format(Date(session.completedTimestamp)),
                                style = MaterialTheme.typography.labelSmall.copy(color = ZenTextSecondary)
                            )
                        }
                        IconButton(onClick = {
                            onDeleteSession(session.id)
                            selectedSession = null
                        }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = ZenTerracottaAccent)
                        }
                    }
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            FullScreenSandTable(
                                theme = theme,
                                progress = session.drawingProgress,
                                isRunning = true,
                                isFullScreen = false,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Card(
                            colors = CardDefaults.cardColors(containerColor = TableSurface),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "PATTERN ARCHIVE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = ZenSlateAccent,
                                        letterSpacing = 1.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = theme.description,
                                    style = MaterialTheme.typography.bodySmall.copy(color = ZenTextPrimary)
                                )

                                if (session.notes.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "REFLECTION: \"${session.notes}\"",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = ZenTextSecondary,
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                        )
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { selectedSession = null },
                        colors = ButtonDefaults.buttonColors(containerColor = ZenSlateAccent, contentColor = PureWhite),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("CLOSE")
                    }
                }
            )
        }
    }
}
