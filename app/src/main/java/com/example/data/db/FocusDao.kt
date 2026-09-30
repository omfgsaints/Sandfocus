package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusDao {
    @Query("SELECT * FROM focus_sessions ORDER BY completedTimestamp DESC")
    fun getAllSessions(): Flow<List<FocusSessionEntity>>

    @Query("SELECT * FROM focus_sessions WHERE isSuccessful = 1 ORDER BY completedTimestamp DESC")
    fun getSuccessfulSessions(): Flow<List<FocusSessionEntity>>

    @Query("SELECT * FROM focus_sessions WHERE reelThemeId = :themeId ORDER BY completedTimestamp DESC")
    fun getSessionsByTheme(themeId: String): Flow<List<FocusSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: FocusSessionEntity): Long

    @Query("DELETE FROM focus_sessions WHERE id = :id")
    suspend fun deleteSessionById(id: Long)

    @Query("SELECT COUNT(*) FROM focus_sessions WHERE isSuccessful = 1")
    fun getCompletedReelsCount(): Flow<Int>

    @Query("SELECT SUM(actualSeconds) FROM focus_sessions")
    fun getTotalFocusSeconds(): Flow<Long?>
}
