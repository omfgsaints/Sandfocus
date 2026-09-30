package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val taskName: String,
    val targetMinutes: Int,
    val actualSeconds: Int,
    val completedTimestamp: Long = System.currentTimeMillis(),
    val isSuccessful: Boolean,
    val reelThemeId: String,
    val reelTitle: String,
    val drawingProgress: Float,
    val notes: String = ""
)
