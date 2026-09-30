package com.example.data.model

enum class ReelDrawingType {
    MANDALA_LOTUS,
    FIBONACCI_SPIRAL,
    OCEAN_DUNES,
    SACRED_TORUS,
    HARMONIC_LISSAJOUS,
    CELESTIAL_ORBIT
}

data class ReelTheme(
    val id: String,
    val title: String,
    val subtitle: String,
    val historicalYear: String,
    val description: String,
    val type: ReelDrawingType,
    val primaryAccent: Long = 0xFFDDA667
) {
    companion object {
        val ALL_THEMES = listOf(
            ReelTheme(
                id = "mandala_lotus",
                title = "Zen Lotus Bloom",
                subtitle = "Sacred Radiating Mandala",
                historicalYear = "Symmetry",
                description = "An eight-fold concentric lotus flower carved with hypnotic precision as the steel sphere glides across sand.",
                type = ReelDrawingType.MANDALA_LOTUS,
                primaryAccent = 0xFFDDA667
            ),
            ReelTheme(
                id = "fibonacci_spiral",
                title = "Fibonacci Golden Spiral",
                subtitle = "Phyllotaxis Sand Ripples",
                historicalYear = "Golden Ratio",
                description = "Nature's logarithmic spiral carving endless golden ratio wave crests into fine quartz sand.",
                type = ReelDrawingType.FIBONACCI_SPIRAL,
                primaryAccent = 0xFF7C9D88
            ),
            ReelTheme(
                id = "ocean_dunes",
                title = "Desert Wind Dunes",
                subtitle = "Interference Wave Tracks",
                historicalYear = "Flow State",
                description = "Gentle sinusoidal wind ripples resembling Sahara dunes sculpted under soft ambient light.",
                type = ReelDrawingType.OCEAN_DUNES,
                primaryAccent = 0xFFC87856
            ),
            ReelTheme(
                id = "sacred_torus",
                title = "Sacred Torus Knot",
                subtitle = "Endless Geometric Loop",
                historicalYear = "Infinity",
                description = "A continuous interlocking magnetic track winding endlessly around an ethereal geometric torus.",
                type = ReelDrawingType.SACRED_TORUS,
                primaryAccent = 0xFFE0C496
            ),
            ReelTheme(
                id = "harmonic_lissajous",
                title = "Lissajous Harmonograph",
                subtitle = "Resonant Frequency Dance",
                historicalYear = "Harmonics",
                description = "Complex geometric pendulum harmonics creating mesmerizing acoustic standing waves in the sand.",
                type = ReelDrawingType.HARMONIC_LISSAJOUS,
                primaryAccent = 0xFF8BAEA2
            ),
            ReelTheme(
                id = "celestial_orbit",
                title = "Planetary Epicycloid",
                subtitle = "Cosmic Orbital Conjunction",
                historicalYear = "Cosmos",
                description = "The geometric dance of Earth and Venus orbiting the Sun, carved in velvety sand.",
                type = ReelDrawingType.CELESTIAL_ORBIT,
                primaryAccent = 0xFFD29864
            )
        )

        fun getById(id: String): ReelTheme {
            return ALL_THEMES.find { it.id == id } ?: ALL_THEMES.first()
        }

        fun getRandom(): ReelTheme {
            return ALL_THEMES.random()
        }
    }
}
