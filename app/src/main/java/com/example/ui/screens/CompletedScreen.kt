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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.sand.FullScreenSandTable
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TableBorderLight
import com.example.ui.theme.TableOffWhite
import com.example.ui.theme.TableSurface
import com.example.ui.theme.ZenGoldAccent
import com.example.ui.theme.ZenSageAccent
import com.example.ui.theme.ZenSlateAccent
import com.example.ui.theme.ZenTextMuted
import com.example.ui.theme.ZenTextPrimary
import com.example.ui.theme.ZenTextSecondary
import com.example.ui.viewmodel.FocusUiState

@Composable
fun CompletedScreen(
    uiState: FocusUiState,
    onNoteChange: (String) -> Unit,
    onViewGallery: () -> Unit,
    onNewSession: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TableOffWhite)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            // Zen Completion Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, TableBorderLight),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(ZenSageAccent.copy(alpha = 0.12f), CircleShape)
                            .border(1.5.dp, ZenSageAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completed",
                            tint = ZenSageAccent,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "— SCULPTURE COMPLETED —",
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 2.sp,
                            color = ZenSlateAccent,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        text = "Tranquil Flow Achieved",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = ZenTextPrimary,
                            fontFamily = FontFamily.Serif
                        )
                    )

                    Text(
                        text = "Phone is now unlocked. Your white kinetic sand sculpture is preserved in the vault.",
                        style = MaterialTheme.typography.bodySmall.copy(color = ZenTextSecondary),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // The Completed Kinetic Sand Table Masterpiece
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 520.dp)
                    .height(340.dp),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, TableBorderLight),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                FullScreenSandTable(
                    theme = uiState.currentTheme,
                    progress = 1.0f,
                    isRunning = true,
                    isFullScreen = false,
                    lightingMode = uiState.lightingMode,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Session Stats Ledger
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, TableBorderLight),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "ZEN SCULPTURE SUMMARY",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = ZenSlateAccent,
                            letterSpacing = 1.5.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Endeavor", style = MaterialTheme.typography.labelSmall.copy(color = ZenTextMuted))
                            Text(uiState.taskName, style = MaterialTheme.typography.titleSmall.copy(color = ZenTextPrimary, fontWeight = FontWeight.SemiBold))
                        }
                        Column {
                            Text("Focus Time", style = MaterialTheme.typography.labelSmall.copy(color = ZenTextMuted))
                            Text("${uiState.targetMinutes} Minutes", style = MaterialTheme.typography.titleSmall.copy(color = ZenTextPrimary, fontWeight = FontWeight.SemiBold))
                        }
                        Column {
                            Text("Pattern", style = MaterialTheme.typography.labelSmall.copy(color = ZenTextMuted))
                            Text(uiState.currentTheme.title, style = MaterialTheme.typography.titleSmall.copy(color = ZenSageAccent, fontWeight = FontWeight.SemiBold))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = uiState.completionNote,
                        onValueChange = onNoteChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reflection_note_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ZenSlateAccent,
                            unfocusedBorderColor = TableBorderLight,
                            focusedTextColor = ZenTextPrimary,
                            unfocusedTextColor = ZenTextPrimary,
                            focusedContainerColor = TableSurface,
                            unfocusedContainerColor = TableSurface
                        ),
                        placeholder = { Text("Log any insights, thoughts, or progress made...", color = ZenTextMuted, fontSize = 12.sp) },
                        maxLines = 3,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        // Actions
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onNewSession,
                    colors = ButtonDefaults.buttonColors(containerColor = ZenSlateAccent, contentColor = PureWhite),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("new_session_button"),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(imageVector = Icons.Default.Replay, contentDescription = "New Session")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("CARVE ANOTHER SAND SCULPTURE", fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = onViewGallery,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ZenTextPrimary),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TableBorderLight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("view_gallery_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Collections, contentDescription = "Vault")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("VIEW SAND SCULPTURE VAULT", fontWeight = FontWeight.Medium)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
