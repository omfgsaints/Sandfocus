package com.example.ui.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext
import kotlin.math.sin
import kotlin.random.Random

enum class AmbientSoundType(
    val displayName: String,
    val description: String,
    val iconName: String
) {
    SILENT(
        "Silent Stillness",
        "Pure tranquility and silence (low battery & CPU usage)",
        "volume_off"
    ),
    KINETIC_SAND_ROLL(
        "Whispering Sand Sphere",
        "Hypnotic, gentle whisper of steel rolling smoothly through fine sand",
        "grain"
    ),
    TIBETAN_SINGING_BOWL(
        "Tibetan Singing Bowls",
        "Deep resonant bronze singing bowls easing your mind into alpha waves",
        "spa"
    ),
    CELESTIAL_432HZ(
        "432Hz Zen Ambience",
        "Warm oceanic harmonic drone for profound mental stillness",
        "self_improvement"
    ),
    BAMBOO_WATER_FOUNTAIN(
        "Bamboo Garden Stream",
        "Peaceful trickling water fountain in a serene Japanese Zen garden",
        "water_drop"
    ),
    GENTLE_CONSERVATORY_RAIN(
        "Soft Whispering Rain",
        "Gentle rain falling peacefully outside on veranda leaves",
        "cloud"
    )
}

/**
 * Ultra-Low-Overhead, Emulator-Safe Generative Zen Audio Engine.
 * Strictly throttled with coroutine delay to guarantee zero CPU starvation in virtual emulators.
 */
class AmbientSoundEngine {
    private var soundJob: Job? = null
    @Volatile
    private var isPlaying = false
    private val scope = CoroutineScope(Dispatchers.Default)

    fun startSound(type: AmbientSoundType, volume: Float = 0.5f) {
        stopSound()
        if (type == AmbientSoundType.SILENT) return

        isPlaying = true
        soundJob = scope.launch {
            try {
                when (type) {
                    AmbientSoundType.KINETIC_SAND_ROLL -> playSandRollingLoop(volume)
                    AmbientSoundType.TIBETAN_SINGING_BOWL -> playSingingBowlsLoop(volume)
                    AmbientSoundType.CELESTIAL_432HZ -> playCelestialHarmonyLoop(volume)
                    AmbientSoundType.BAMBOO_WATER_FOUNTAIN -> playBambooWaterLoop(volume)
                    AmbientSoundType.GENTLE_CONSERVATORY_RAIN -> playRainLoop(volume)
                    AmbientSoundType.SILENT -> Unit
                }
            } catch (_: Exception) {
            }
        }
    }

    fun stopSound() {
        isPlaying = false
        soundJob?.cancel()
        soundJob = null
    }

    fun playCompletionChime() {
        scope.launch {
            try {
                val sampleRate = 22050
                val numSamples = (sampleRate * 2.5).toInt()
                val samples = ShortArray(numSamples)
                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    val decay = Math.exp(-t * 1.6)
                    val fundamental = sin(2.0 * Math.PI * 369.99 * t)
                    val secondHarmonic = sin(2.0 * Math.PI * 739.99 * t) * 0.4 * Math.exp(-t * 2.2)
                    val sample = ((fundamental + secondHarmonic) * decay * 0.65 * Short.MAX_VALUE).toInt()
                    samples[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }
                playPcmBufferOnce(samples, sampleRate)
            } catch (_: Exception) {}
        }
    }

    private suspend fun playSandRollingLoop(volume: Float) {
        val sampleRate = 16000
        val bufferSize = sampleRate / 2 // 500ms chunk
        val buffer = ShortArray(bufferSize)
        val audioTrack = createAudioTrack(sampleRate) ?: return

        try {
            audioTrack.play()
            val random = Random(42)
            var lastSample = 0.0
            var phase = 0.0

            while (isPlaying && coroutineContext.isActive) {
                val startTime = System.currentTimeMillis()
                for (i in 0 until bufferSize) {
                    val t = phase / sampleRate
                    val rollSwell = (sin(2.0 * Math.PI * 0.35 * t) + 1.2) * 0.5
                    val white = random.nextDouble() * 2.0 - 1.0
                    lastSample = (lastSample + (0.035 * white)) / 1.035
                    val sandFriction = lastSample * rollSwell * 1.5
                    val magnetHum = sin(2.0 * Math.PI * 55.0 * t) * 0.04

                    val sample = ((sandFriction + magnetHum) * volume * 1.3 * Short.MAX_VALUE).toInt()
                    buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                    phase += 1.0
                }
                audioTrack.write(buffer, 0, buffer.size)

                // Enforce real-time rate limit to prevent CPU spin in emulator
                val elapsed = System.currentTimeMillis() - startTime
                val sleepTarget = 480L - elapsed
                if (sleepTarget > 10L) {
                    delay(sleepTarget)
                } else {
                    delay(50L)
                }
            }
        } catch (_: Exception) {
        } finally {
            cleanupAudioTrack(audioTrack)
        }
    }

    private suspend fun playSingingBowlsLoop(volume: Float) {
        val sampleRate = 16000
        val bowlDuration = 4.0 // 4 seconds per strike cycle
        val cycleSamples = (sampleRate * bowlDuration).toInt()
        val buffer = ShortArray(cycleSamples)
        val audioTrack = createAudioTrack(sampleRate) ?: return

        val bowlFrequencies = doubleArrayOf(216.0, 272.0, 324.0, 432.0)
        var bowlIdx = 0

        try {
            audioTrack.play()
            while (isPlaying && coroutineContext.isActive) {
                val startTime = System.currentTimeMillis()
                val freq = bowlFrequencies[bowlIdx % bowlFrequencies.size]
                bowlIdx++

                for (i in 0 until cycleSamples) {
                    val t = i.toDouble() / sampleRate
                    val decay = Math.exp(-t * 0.9)
                    val beat = sin(2.0 * Math.PI * 2.5 * t) * 0.15
                    val fundamental = sin(2.0 * Math.PI * freq * t)
                    val octave = sin(2.0 * Math.PI * (freq * 2.0) * t) * 0.35

                    val mixed = (fundamental + octave + beat) * decay * 0.6
                    val sample = (mixed * volume * Short.MAX_VALUE).toInt()
                    buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }
                audioTrack.write(buffer, 0, buffer.size)

                val elapsed = System.currentTimeMillis() - startTime
                val sleepTarget = 3900L - elapsed
                if (sleepTarget > 50L) {
                    delay(sleepTarget)
                } else {
                    delay(100L)
                }
            }
        } catch (_: Exception) {
        } finally {
            cleanupAudioTrack(audioTrack)
        }
    }

    private suspend fun playCelestialHarmonyLoop(volume: Float) {
        val sampleRate = 16000
        val audioTrack = createAudioTrack(sampleRate) ?: return
        val bufferSize = sampleRate / 2
        val buffer = ShortArray(bufferSize)

        try {
            audioTrack.play()
            var sampleOffset = 0L
            val baseFreq = 108.0

            while (isPlaying && coroutineContext.isActive) {
                val startTime = System.currentTimeMillis()
                for (i in 0 until bufferSize) {
                    val t = (sampleOffset + i).toDouble() / sampleRate
                    val breath = (sin(2.0 * Math.PI * 0.08 * t) + 1.0) * 0.5
                    val root = sin(2.0 * Math.PI * baseFreq * t) * 0.4
                    val fifth = sin(2.0 * Math.PI * (baseFreq * 1.5) * t) * 0.25
                    val octave = sin(2.0 * Math.PI * (baseFreq * 2.0) * t) * 0.18
                    val shimmeringTenth = sin(2.0 * Math.PI * (baseFreq * 2.5) * t + 0.5) * (0.12 * breath)

                    val sampleVal = ((root + fifth + octave + shimmeringTenth) * volume * 0.75 * Short.MAX_VALUE).toInt()
                    buffer[i] = sampleVal.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }
                sampleOffset += bufferSize
                audioTrack.write(buffer, 0, buffer.size)

                val elapsed = System.currentTimeMillis() - startTime
                val sleepTarget = 480L - elapsed
                if (sleepTarget > 10L) {
                    delay(sleepTarget)
                } else {
                    delay(50L)
                }
            }
        } catch (_: Exception) {
        } finally {
            cleanupAudioTrack(audioTrack)
        }
    }

    private suspend fun playBambooWaterLoop(volume: Float) {
        val sampleRate = 16000
        val bufferSize = sampleRate / 2
        val buffer = ShortArray(bufferSize)
        val audioTrack = createAudioTrack(sampleRate) ?: return

        try {
            audioTrack.play()
            val random = Random(888)
            var lastSample = 0.0
            var sampleCounter = 0L

            while (isPlaying && coroutineContext.isActive) {
                val startTime = System.currentTimeMillis()
                for (i in 0 until bufferSize) {
                    val t = sampleCounter.toDouble() / sampleRate
                    val white = random.nextDouble() * 2.0 - 1.0
                    lastSample = (lastSample + (0.04 * white)) / 1.04
                    var water = lastSample * 1.3
                    if (random.nextDouble() < 0.002) {
                        val dropFreq = 1200.0 + random.nextDouble() * 800.0
                        water += sin(2.0 * Math.PI * dropFreq * t) * 0.5
                    }
                    val sample = (water * volume * Short.MAX_VALUE).toInt()
                    buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                    sampleCounter++
                }
                audioTrack.write(buffer, 0, buffer.size)

                val elapsed = System.currentTimeMillis() - startTime
                val sleepTarget = 480L - elapsed
                if (sleepTarget > 10L) {
                    delay(sleepTarget)
                } else {
                    delay(50L)
                }
            }
        } catch (_: Exception) {
        } finally {
            cleanupAudioTrack(audioTrack)
        }
    }

    private suspend fun playRainLoop(volume: Float) {
        val sampleRate = 16000
        val bufferSize = sampleRate / 2
        val buffer = ShortArray(bufferSize)
        val audioTrack = createAudioTrack(sampleRate) ?: return

        try {
            audioTrack.play()
            val random = Random(42)
            var lastSample = 0.0

            while (isPlaying && coroutineContext.isActive) {
                val startTime = System.currentTimeMillis()
                for (i in 0 until bufferSize) {
                    val white = random.nextDouble() * 2.0 - 1.0
                    lastSample = (lastSample + (0.02 * white)) / 1.02
                    val rain = lastSample * 1.6
                    val sample = (rain * volume * Short.MAX_VALUE).toInt()
                    buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }
                audioTrack.write(buffer, 0, buffer.size)

                val elapsed = System.currentTimeMillis() - startTime
                val sleepTarget = 480L - elapsed
                if (sleepTarget > 10L) {
                    delay(sleepTarget)
                } else {
                    delay(50L)
                }
            }
        } catch (_: Exception) {
        } finally {
            cleanupAudioTrack(audioTrack)
        }
    }

    private fun createAudioTrack(sampleRate: Int): AudioTrack? {
        return try {
            AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(sampleRate)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()
        } catch (_: Exception) {
            null
        }
    }

    private fun cleanupAudioTrack(audioTrack: AudioTrack?) {
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (_: Exception) {}
    }

    private fun playPcmBufferOnce(samples: ShortArray, sampleRate: Int) {
        try {
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(samples.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(samples, 0, samples.size)
            audioTrack.play()
            scope.launch {
                delay(2800)
                try {
                    audioTrack.stop()
                    audioTrack.release()
                } catch (_: Exception) {}
            }
        } catch (_: Exception) {}
    }
}
