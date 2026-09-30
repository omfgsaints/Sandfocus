package com.example.data.repository

import com.example.data.db.FocusDao
import com.example.data.db.FocusSessionEntity
import kotlinx.coroutines.flow.Flow

class FocusRepository(private val dao: FocusDao) {
    val allSessions: Flow<List<FocusSessionEntity>> = dao.getAllSessions()
    val successfulSessions: Flow<List<FocusSessionEntity>> = dao.getSuccessfulSessions()
    val completedReelsCount: Flow<Int> = dao.getCompletedReelsCount()
    val totalFocusSeconds: Flow<Long?> = dao.getTotalFocusSeconds()

    suspend fun recordSession(session: FocusSessionEntity): Long {
        return dao.insertSession(session)
    }

    suspend fun deleteSession(id: Long) {
        dao.deleteSessionById(id)
    }
}
