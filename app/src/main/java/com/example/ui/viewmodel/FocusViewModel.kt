package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.FocusSessionEntity
import com.example.data.db.KinetoDatabase
import com.example.data.model.ReelTheme
import com.example.data.repository.FocusRepository
import com.example.ui.audio.AmbientSoundEngine
import com.example.ui.audio.AmbientSoundType
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class FocusState {
    SETUP,
    RUNNING,
    PAUSED,
    COMPLETED
}

data class FocusUiState(
    val focusState: FocusState = FocusState.SETUP,
    val taskName: String = "Deep Focus",
    val targetMinutes: Int = 25,
    val remainingSeconds: Int = 25 * 60,
    val elapsedSeconds: Int = 0,
    val currentTheme: ReelTheme = ReelTheme.ALL_THEMES.first(),
    val isRandomTheme: Boolean = true,
    val strictLockEnabled: Boolean = false,
    val keepScreenOn: Boolean = false,
    val soundType: AmbientSoundType = AmbientSoundType.SILENT,
    val soundVolume: Float = 0.5f,
    val lightingMode: com.example.ui.sand.SandLightingMode = com.example.ui.sand.SandLightingMode.GALLERY_WHITE,
    val emergencyHoldProgress: Float = 0f,
    val showGiveUpDialog: Boolean = false,
    val completionNote: String = "",
    val distractionAttempts: Int = 0,
    val lastCompletedSession: FocusSessionEntity? = null
) {
    val totalSeconds: Int get() = targetMinutes * 60
    val progress: Float
        get() = if (totalSeconds > 0) {
            (elapsedSeconds.toFloat() / totalSeconds.toFloat()).coerceIn(0f, 1f)
        } else 0f

    val formattedRemainingTime: String
        get() {
            val minutes = remainingSeconds / 60
            val seconds = remainingSeconds % 60
            return "%02d:%02d".format(minutes, seconds)
        }

    val formattedElapsedTime: String
        get() {
            val minutes = elapsedSeconds / 60
            val seconds = elapsedSeconds % 60
            return "%02d:%02d".format(minutes, seconds)
        }

    // Equivalent footage of 35mm celluloid film developed (standard 16 frames/ft)
    val filmFootageDeveloped: Int
        get() = (elapsedSeconds * 0.8f).toInt()

    val totalFilmFootage: Int
        get() = (totalSeconds * 0.8f).toInt()
}

class FocusViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FocusRepository
    private val soundEngine = AmbientSoundEngine()
    private var timerJob: Job? = null
    private var emergencyHoldJob: Job? = null

    private val _uiState = MutableStateFlow(FocusUiState())
    val uiState: StateFlow<FocusUiState> = _uiState.asStateFlow()

    init {
        val db = KinetoDatabase.getDatabase(application)
        repository = FocusRepository(db.focusDao())
    }

    val allSessions: StateFlow<List<FocusSessionEntity>> = repository.allSessions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val successfulSessions: StateFlow<List<FocusSessionEntity>> = repository.successfulSessions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedReelsCount: StateFlow<Int> = repository.completedReelsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalFocusSeconds: StateFlow<Long?> = repository.totalFocusSeconds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    fun setTaskName(name: String) {
        _uiState.update { it.copy(taskName = name) }
    }

    fun setTargetMinutes(minutes: Int) {
        val clamped = minutes.coerceIn(1, 180)
        _uiState.update {
            it.copy(
                targetMinutes = clamped,
                remainingSeconds = clamped * 60,
                elapsedSeconds = 0
            )
        }
    }

    fun setTheme(theme: ReelTheme) {
        _uiState.update { it.copy(currentTheme = theme, isRandomTheme = false) }
    }

    fun setRandomTheme(enable: Boolean) {
        _uiState.update { it.copy(isRandomTheme = enable) }
    }

    fun setStrictLock(enabled: Boolean) {
        _uiState.update { it.copy(strictLockEnabled = enabled) }
    }

    fun setKeepScreenOn(enabled: Boolean) {
        _uiState.update { it.copy(keepScreenOn = enabled) }
    }

    fun setSoundType(type: AmbientSoundType) {
        _uiState.update { it.copy(soundType = type) }
        if (_uiState.value.focusState == FocusState.RUNNING) {
            soundEngine.startSound(type, _uiState.value.soundVolume)
        }
    }

    fun setSoundVolume(volume: Float) {
        val clamped = volume.coerceIn(0f, 1f)
        _uiState.update { it.copy(soundVolume = clamped) }
        if (_uiState.value.focusState == FocusState.RUNNING) {
            soundEngine.startSound(_uiState.value.soundType, clamped)
        }
    }

    fun setLightingMode(mode: com.example.ui.sand.SandLightingMode) {
        _uiState.update { it.copy(lightingMode = mode) }
    }

    fun startFocusSession() {
        val selectedTheme = if (_uiState.value.isRandomTheme) {
            ReelTheme.getRandom()
        } else {
            _uiState.value.currentTheme
        }

        _uiState.update {
            it.copy(
                focusState = FocusState.RUNNING,
                currentTheme = selectedTheme,
                remainingSeconds = it.targetMinutes * 60,
                elapsedSeconds = 0,
                emergencyHoldProgress = 0f,
                showGiveUpDialog = false,
                distractionAttempts = 0
            )
        }

        soundEngine.startSound(_uiState.value.soundType, _uiState.value.soundVolume)
        startTimerLoop()
    }

    private fun startTimerLoop() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                val current = _uiState.value
                if (current.focusState != FocusState.RUNNING) break

                if (current.remainingSeconds <= 1) {
                    onSessionCompleted()
                    break
                } else {
                    _uiState.update {
                        it.copy(
                            remainingSeconds = it.remainingSeconds - 1,
                            elapsedSeconds = it.elapsedSeconds + 1
                        )
                    }
                }
            }
        }
    }

    private fun onSessionCompleted() {
        timerJob?.cancel()
        soundEngine.stopSound()
        soundEngine.playCompletionChime()
        triggerVibration(longPattern = true)

        val state = _uiState.value
        val completedSession = FocusSessionEntity(
            taskName = state.taskName,
            targetMinutes = state.targetMinutes,
            actualSeconds = state.totalSeconds,
            completedTimestamp = System.currentTimeMillis(),
            isSuccessful = true,
            reelThemeId = state.currentTheme.id,
            reelTitle = state.currentTheme.title,
            drawingProgress = 1.0f,
            notes = state.completionNote
        )

        viewModelScope.launch {
            repository.recordSession(completedSession)
        }

        _uiState.update {
            it.copy(
                focusState = FocusState.COMPLETED,
                remainingSeconds = 0,
                elapsedSeconds = state.totalSeconds,
                lastCompletedSession = completedSession
            )
        }
    }

    fun pauseSession() {
        if (_uiState.value.strictLockEnabled) {
            // Strict lock prevents simple pausing - requires emergency confirm
            _uiState.update { it.copy(showGiveUpDialog = true) }
            return
        }
        timerJob?.cancel()
        soundEngine.stopSound()
        _uiState.update { it.copy(focusState = FocusState.PAUSED) }
    }

    fun resumeSession() {
        _uiState.update { it.copy(focusState = FocusState.RUNNING) }
        soundEngine.startSound(_uiState.value.soundType, _uiState.value.soundVolume)
        startTimerLoop()
    }

    fun showGiveUpWarning() {
        _uiState.update {
            it.copy(
                showGiveUpDialog = true,
                distractionAttempts = it.distractionAttempts + 1
            )
        }
        triggerVibration(longPattern = false)
    }

    fun dismissGiveUpWarning() {
        _uiState.update { it.copy(showGiveUpDialog = false, emergencyHoldProgress = 0f) }
    }

    fun startEmergencyReleaseHold() {
        emergencyHoldJob?.cancel()
        emergencyHoldJob = viewModelScope.launch {
            val totalSteps = 50 // 5 seconds (100ms per step)
            for (step in 1..totalSteps) {
                delay(100)
                _uiState.update { it.copy(emergencyHoldProgress = step.toFloat() / totalSteps.toFloat()) }
            }
            // Completed 5 seconds hold: abort session and unlock
            abortSession()
        }
    }

    fun cancelEmergencyReleaseHold() {
        emergencyHoldJob?.cancel()
        emergencyHoldJob = null
        _uiState.update { it.copy(emergencyHoldProgress = 0f) }
    }

    fun abortSession() {
        timerJob?.cancel()
        soundEngine.stopSound()
        emergencyHoldJob?.cancel()

        val state = _uiState.value
        if (state.elapsedSeconds > 10) {
            // Record partial / aborted attempt
            val partialSession = FocusSessionEntity(
                taskName = state.taskName,
                targetMinutes = state.targetMinutes,
                actualSeconds = state.elapsedSeconds,
                completedTimestamp = System.currentTimeMillis(),
                isSuccessful = false,
                reelThemeId = state.currentTheme.id,
                reelTitle = state.currentTheme.title,
                drawingProgress = state.progress,
                notes = "Aborted after ${state.formattedElapsedTime}"
            )
            viewModelScope.launch {
                repository.recordSession(partialSession)
            }
        }

        _uiState.update {
            it.copy(
                focusState = FocusState.SETUP,
                remainingSeconds = it.targetMinutes * 60,
                elapsedSeconds = 0,
                emergencyHoldProgress = 0f,
                showGiveUpDialog = false
            )
        }
    }

    fun resetToSetup() {
        timerJob?.cancel()
        soundEngine.stopSound()
        _uiState.update {
            it.copy(
                focusState = FocusState.SETUP,
                remainingSeconds = it.targetMinutes * 60,
                elapsedSeconds = 0,
                emergencyHoldProgress = 0f,
                showGiveUpDialog = false,
                lastCompletedSession = null,
                completionNote = ""
            )
        }
    }

    fun updateCompletionNote(note: String) {
        _uiState.update { it.copy(completionNote = note) }
        val session = _uiState.value.lastCompletedSession
        if (session != null) {
            viewModelScope.launch {
                repository.recordSession(session.copy(notes = note))
            }
        }
    }

    fun deleteSession(id: Long) {
        viewModelScope.launch {
            repository.deleteSession(id)
        }
    }

    private fun triggerVibration(longPattern: Boolean) {
        try {
            val context = getApplication<Application>()
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                manager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = if (longPattern) {
                    longArrayOf(0, 300, 150, 400, 200, 600)
                } else {
                    longArrayOf(0, 120, 80, 120)
                }
                vibrator?.vibrate(VibrationEffect.createWaveform(timings, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(if (longPattern) 600 else 150)
            }
        } catch (_: Exception) {}
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        emergencyHoldJob?.cancel()
        soundEngine.stopSound()
    }
}
