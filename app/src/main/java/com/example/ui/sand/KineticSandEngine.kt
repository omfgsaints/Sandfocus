package com.example.ui.sand

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import com.example.data.model.ReelDrawingType
import com.example.data.model.ReelTheme
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

enum class SandLightingMode(val displayName: String) {
    GALLERY_WHITE("Studio White (4500K)"),
    WARM_CANDLE("Warm Sand (2900K)"),
    DAWN_BLUSH("Dawn Rose (3200K)")
}

data class SampledPoint(
    val x: Float,
    val y: Float,
    val angle: Float // Heading angle in radians for rolling sphere rotation
)

/**
 * Hyper-Realistic Kinetic Sand Engine for White Silica Sand Tables.
 * Accurately models physical white quartz sand dunes, multi-tier concave trenches,
 * directional side-illumination shadows, and mirror-finish chrome ball physics.
 */
class KineticSandEngine {

    // Cached trajectory data to prevent per-frame recomputation
    private var cachedThemeType: ReelDrawingType? = null
    private var cachedWidth: Float = 0f
    private var cachedHeight: Float = 0f
    private var cachedPoints: List<SampledPoint> = emptyList()
    private var cachedMasterPath: Path = Path()
    private var cachedPathMeasure: PathMeasure = PathMeasure()
    private var cachedTotalLength: Float = 0f

    // Micro-grains seed for consistent tactile quartz texture
    private val staticGrainOffsets: List<Offset> = run {
        val random = Random(4096)
        List(160) {
            Offset(random.nextFloat(), random.nextFloat())
        }
    }

    fun prepareTrajectory(
        type: ReelDrawingType,
        width: Float,
        height: Float
    ) {
        if (cachedThemeType == type && cachedWidth == width && cachedHeight == height) {
            return
        }

        cachedThemeType = type
        cachedWidth = width
        cachedHeight = height

        val minDim = min(width, height)
        val cx = width / 2f
        val cy = height / 2f
        val maxR = (minDim / 2f) * 0.88f

        val points = mutableListOf<SampledPoint>()
        val path = Path()
        val totalSteps = 1000

        when (type) {
            ReelDrawingType.MANDALA_LOTUS -> {
                val totalRot = 16.0 * PI
                var prevX = cx
                var prevY = cy
                for (i in 0..totalSteps) {
                    val frac = i.toDouble() / totalSteps
                    val t = frac * totalRot
                    val petal = 0.38 + 0.62 * kotlin.math.abs(cos(4.0 * t))
                    val r = maxR * (frac * 0.94 + 0.06) * petal
                    val x = (cx + r * cos(t)).toFloat()
                    val y = (cy + r * sin(t)).toFloat()
                    val angle = atan2(y - prevY, x - prevX)
                    points.add(SampledPoint(x, y, angle))
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    prevX = x
                    prevY = y
                }
            }

            ReelDrawingType.FIBONACCI_SPIRAL -> {
                val totalLoops = 14.0 * PI
                var prevX = cx
                var prevY = cy
                for (i in 0..totalSteps) {
                    val p = i.toDouble() / totalSteps
                    val theta = p * totalLoops
                    val r = maxR * (0.05 + 0.93 * Math.pow(p, 0.72))
                    val x = (cx + r * cos(theta)).toFloat()
                    val y = (cy + r * sin(theta)).toFloat()
                    val angle = atan2(y - prevY, x - prevX)
                    points.add(SampledPoint(x, y, angle))
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    prevX = x
                    prevY = y
                }
            }

            ReelDrawingType.OCEAN_DUNES -> {
                val passes = 18
                var prevX = cx
                var prevY = cy
                var idx = 0
                for (pass in 0 until passes) {
                    val yNorm = (pass.toDouble() / (passes - 1)) * 2.0 - 1.0
                    val py = cy + (yNorm * maxR * 0.86).toFloat()
                    val pointsInPass = totalSteps / passes
                    for (j in 0..pointsInPass) {
                        val xFrac = j.toDouble() / pointsInPass
                        val pxNorm = if (pass % 2 == 0) xFrac * 2.0 - 1.0 else (1.0 - xFrac) * 2.0 - 1.0
                        val px = cx + (pxNorm * maxR * 0.86).toFloat()
                        val waveOffset = (sin(pxNorm * 11.0 + pass * 0.85) * 9.5).toFloat()
                        val finalY = py + waveOffset
                        val angle = atan2(finalY - prevY, px - prevX)
                        points.add(SampledPoint(px, finalY, angle))
                        if (idx == 0) path.moveTo(px, finalY) else path.lineTo(px, finalY)
                        prevX = px
                        prevY = finalY
                        idx++
                    }
                }
            }

            ReelDrawingType.SACRED_TORUS -> {
                val totalT = 2.0 * PI * 7.0
                var prevX = cx
                var prevY = cy
                for (i in 0..totalSteps) {
                    val t = (i.toDouble() / totalSteps) * totalT
                    val r = maxR * (0.64 + 0.32 * cos(3.0 / 7.0 * t))
                    val x = (cx + r * cos(t)).toFloat()
                    val y = (cy + r * sin(t)).toFloat()
                    val angle = atan2(y - prevY, x - prevX)
                    points.add(SampledPoint(x, y, angle))
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    prevX = x
                    prevY = y
                }
            }

            ReelDrawingType.HARMONIC_LISSAJOUS -> {
                val totalT = 28.0 * PI
                var prevX = cx
                var prevY = cy
                for (i in 0..totalSteps) {
                    val p = i.toDouble() / totalSteps
                    val t = p * totalT
                    val decay = 1.0 - p * 0.12
                    val x = (cx + maxR * 0.88 * sin(3.0 * t + 0.35) * decay).toFloat()
                    val y = (cy + maxR * 0.88 * sin(4.0 * t) * decay).toFloat()
                    val angle = atan2(y - prevY, x - prevX)
                    points.add(SampledPoint(x, y, angle))
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    prevX = x
                    prevY = y
                }
            }

            ReelDrawingType.CELESTIAL_ORBIT -> {
                val totalT = 10.0 * PI
                val R = maxR * 0.65
                val r = maxR * 0.25
                val d = maxR * 0.3
                var prevX = cx
                var prevY = cy
                for (i in 0..totalSteps) {
                    val t = (i.toDouble() / totalSteps) * totalT
                    val x = (cx + (R - r) * cos(t) + d * cos((R - r) / r * t)).toFloat()
                    val y = (cy + (R - r) * sin(t) - d * sin((R - r) / r * t)).toFloat()
                    val angle = atan2(y - prevY, x - prevX)
                    points.add(SampledPoint(x, y, angle))
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    prevX = x
                    prevY = y
                }
            }
        }

        cachedPoints = points
        cachedMasterPath = path
        cachedPathMeasure = PathMeasure().apply { setPath(path, false) }
        cachedTotalLength = cachedPathMeasure.length
    }

    fun drawRealisticSandTable(
        drawScope: DrawScope,
        theme: ReelTheme,
        progress: Float,
        frameNanos: Long,
        isFullScreen: Boolean = true,
        lightingMode: SandLightingMode = SandLightingMode.GALLERY_WHITE
    ) {
        val size = drawScope.size
        val w = size.width
        val h = size.height
        val minDim = min(w, h)
        val cx = w / 2f
        val cy = h / 2f
        val tableRadius = if (isFullScreen) (minDim / 2f) * 0.96f else (minDim / 2f) * 0.86f
        val scale = tableRadius / 180f

        // Ensure trajectory is prepared and cached
        prepareTrajectory(theme.type, w, h)

        val clampedProgress = progress.coerceIn(0f, 1f)

        // 1. Render Fine White Quartz Sand Surface with Ambient Illumination
        drawScope.renderWhiteSandBed(cx, cy, tableRadius, isFullScreen, lightingMode)

        // 2. Render 3D Directional Concave Trenches & Raised Sand Dune Berms
        val activeBallPoint = drawScope.render3DWhiteSandGrooves(clampedProgress, scale)

        // 3. Render Mirror-Finish Chrome Rolling Sphere with Contact Shadows
        if (activeBallPoint != null) {
            drawScope.renderRollingChromeBall(activeBallPoint, scale, frameNanos, lightingMode)
        }

        // 4. Render Minimalist White Table Rim & Recessed Perimeter LED Strip
        drawScope.renderWhiteTableRimAndLED(cx, cy, tableRadius, isFullScreen, lightingMode)
    }

    private fun DrawScope.renderWhiteSandBed(
        cx: Float,
        cy: Float,
        radius: Float,
        isFullScreen: Boolean,
        lightingMode: SandLightingMode
    ) {
        // Pristine White Silica Quartz color gradients based on ambient LED temperature
        val (highlightColor, baseSandColor, shadeSandColor) = when (lightingMode) {
            SandLightingMode.GALLERY_WHITE -> Triple(
                Color(0xFFFFFFFF),
                Color(0xFFF3F5F8),
                Color(0xFFE2E7ED)
            )
            SandLightingMode.WARM_CANDLE -> Triple(
                Color(0xFFFFFDF8),
                Color(0xFFF7F2E8),
                Color(0xFFE8DFD0)
            )
            SandLightingMode.DAWN_BLUSH -> Triple(
                Color(0xFFFFFBF9),
                Color(0xFFF7EFEA),
                Color(0xFFEADBCE)
            )
        }

        // Directional Light Source (Simulated top-left rim lighting)
        val lightOffset = Offset(cx - radius * 0.35f, cy - radius * 0.35f)

        if (isFullScreen) {
            val radialSand = Brush.radialGradient(
                colors = listOf(highlightColor, baseSandColor, shadeSandColor),
                center = lightOffset,
                radius = radius * 1.6f
            )
            drawRect(brush = radialSand)
        } else {
            val radialSand = Brush.radialGradient(
                colors = listOf(highlightColor, baseSandColor, shadeSandColor),
                center = lightOffset,
                radius = radius * 1.15f
            )
            drawCircle(brush = radialSand, radius = radius, center = Offset(cx, cy))
        }

        // Concentric comb rake ripples in fine white sand (traditional Zen garden karesansui pattern)
        val rakeColor = Color(0xFF8B95A2).copy(alpha = 0.09f)
        val rings = 14
        for (r in 1..rings) {
            drawCircle(
                color = rakeColor,
                radius = (radius / rings) * r,
                center = Offset(cx, cy),
                style = Stroke(width = 0.9f)
            )
        }

        // Tactile white silica micro-sparkles (silica crystals reflecting light)
        for (offset in staticGrainOffsets) {
            val gx = (offset.x - 0.5f) * radius * 1.75f + cx
            val gy = (offset.y - 0.5f) * radius * 1.75f + cy
            // Micro shadow
            drawCircle(
                color = Color(0x184A5568),
                radius = 1.0f,
                center = Offset(gx + 0.6f, gy + 0.8f)
            )
            // Micro crystalline reflection glint
            drawCircle(
                color = Color(0x66FFFFFF),
                radius = 0.8f,
                center = Offset(gx, gy)
            )
        }
    }

    private fun DrawScope.render3DWhiteSandGrooves(
        progress: Float,
        scale: Float
    ): SampledPoint? {
        if (progress <= 0f || cachedPoints.isEmpty()) return null

        val totalLen = cachedTotalLength
        if (totalLen <= 0f) return null

        val stopDistance = totalLen * progress.coerceIn(0.001f, 1f)
        val segmentPath = Path()
        cachedPathMeasure.getSegment(0f, stopDistance, segmentPath, true)

        val trenchWidth = 5.2f * scale
        val bermWidth = 8.0f * scale

        // Layer 1: Directional Sand Drop Shadow (Trench cast shadow, offset +1.4f * scale down-right)
        // This gives high-contrast tactile depth against white sand!
        drawPath(
            path = segmentPath,
            color = Color(0x40374151),
            style = Stroke(
                width = trenchWidth * 1.15f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Layer 2: Raised Pure White Sand Berm Crest (Piled up white sand crests catching LED light)
        val whiteBermCrest = Color(0xCCFFFFFF)
        drawPath(
            path = segmentPath,
            color = whiteBermCrest,
            style = Stroke(
                width = bermWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Layer 3: Smooth Concave Trench Shadow (Soft gradient slope into groove)
        val trenchSlopeShadow = Color(0xFF7B8694)
        drawPath(
            path = segmentPath,
            color = trenchSlopeShadow,
            style = Stroke(
                width = trenchWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Layer 4: Deep Velvet Trough Core (Center where the ball rolled deepest)
        val deepCoreTrough = Color(0xFF434A54)
        drawPath(
            path = segmentPath,
            color = deepCoreTrough,
            style = Stroke(
                width = trenchWidth * 0.42f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Compute current ball point
        val targetIdx = ((cachedPoints.size - 1) * progress).toInt().coerceIn(0, cachedPoints.size - 1)
        val basePoint = cachedPoints[targetIdx]

        // Sub-pixel position for fluid continuous motion
        val exactPos = cachedPathMeasure.getPosition(stopDistance)
        return SampledPoint(exactPos.x, exactPos.y, basePoint.angle)
    }

    private fun DrawScope.renderRollingChromeBall(
        pt: SampledPoint,
        scale: Float,
        frameNanos: Long,
        lightingMode: SandLightingMode
    ) {
        val ballRadius = 6.8f * scale
        val pos = Offset(pt.x, pt.y)

        // 1. Soft Ambient Occlusion Shadow on white sand (shifted down-right away from light)
        drawCircle(
            color = Color(0x281F2937),
            radius = ballRadius * 1.8f,
            center = Offset(pos.x + 3.2f * scale, pos.y + 4.0f * scale)
        )

        // 2. Sharp Physical Contact Shadow under the ball on silica sand
        drawCircle(
            color = Color(0x66111827),
            radius = ballRadius * 1.15f,
            center = Offset(pos.x + 1.0f * scale, pos.y + 1.6f * scale)
        )

        // 3. Displaced Sand Micro-Wake trailing the sphere
        val headingAngle = pt.angle
        val wakeX = pos.x - (cos(headingAngle) * ballRadius * 1.25f).toFloat()
        val wakeY = pos.y - (sin(headingAngle) * ballRadius * 1.25f).toFloat()
        drawCircle(
            color = Color(0x409CA3AF),
            radius = ballRadius * 0.85f,
            center = Offset(wakeX, wakeY)
        )

        // 4. Mirror-Finish Chrome Spherical Shading
        val ballLightCenter = Offset(pos.x - ballRadius * 0.38f, pos.y - ballRadius * 0.38f)
        val (highlightTint, equatorTint, shadowTint) = when (lightingMode) {
            SandLightingMode.GALLERY_WHITE -> Triple(Color(0xFFFFFFFF), Color(0xFFE8ECEF), Color(0xFF374151))
            SandLightingMode.WARM_CANDLE -> Triple(Color(0xFFFFFDF8), Color(0xFFECE7DE), Color(0xFF4A423B))
            SandLightingMode.DAWN_BLUSH -> Triple(Color(0xFFFFF9F5), Color(0xFFECE4DE), Color(0xFF4A3C38))
        }

        val chromeGradient = Brush.radialGradient(
            colors = listOf(
                highlightTint,
                equatorTint,
                Color(0xFF9CA3AF),
                shadowTint
            ),
            center = ballLightCenter,
            radius = ballRadius * 1.15f
        )
        drawCircle(brush = chromeGradient, radius = ballRadius, center = pos)

        // 5. Rolling Directional Horizon Reflection (Rotates with ball trajectory for 3D rolling illusion)
        val rollCycle = (frameNanos / 1000000L * 0.008f) % (2.0 * PI).toFloat()
        rotate(Math.toDegrees(headingAngle.toDouble()).toFloat(), pivot = pos) {
            val bandOffset = sin(rollCycle) * (ballRadius * 0.35f)
            drawLine(
                color = Color(0x35FFFFFF),
                start = Offset(pos.x - ballRadius * 0.7f, pos.y + bandOffset),
                end = Offset(pos.x + ballRadius * 0.7f, pos.y + bandOffset),
                strokeWidth = 1.2f * scale
            )
        }

        // 6. Crisp Mirror Specular Reflection Glint
        drawCircle(
            color = Color(0xFFFFFFFF),
            radius = ballRadius * 0.3f,
            center = Offset(pos.x - ballRadius * 0.42f, pos.y - ballRadius * 0.42f)
        )
        drawCircle(
            color = Color(0x88FFFFFF),
            radius = ballRadius * 0.55f,
            center = Offset(pos.x - ballRadius * 0.42f, pos.y - ballRadius * 0.42f)
        )
    }

    private fun DrawScope.renderWhiteTableRimAndLED(
        cx: Float,
        cy: Float,
        radius: Float,
        isFullScreen: Boolean,
        lightingMode: SandLightingMode
    ) {
        val ambientLedColor = when (lightingMode) {
            SandLightingMode.GALLERY_WHITE -> Color(0xFFE8F1FC)
            SandLightingMode.WARM_CANDLE -> Color(0xFFFFF5E6)
            SandLightingMode.DAWN_BLUSH -> Color(0xFFFFECE5)
        }

        if (!isFullScreen) {
            // Scandinavian Minimalist White Matte Rim (Like white Sisyphus / Dune table)
            val rimThickness = 16f
            // Drop shadow under outer table
            drawCircle(
                color = Color(0x15000000),
                radius = radius + rimThickness + 3f,
                center = Offset(cx, cy + 2f),
                style = Stroke(width = 4f)
            )

            // Pristine Matte White Casing
            drawCircle(
                color = Color(0xFFFFFFFF),
                radius = radius + rimThickness / 2f,
                center = Offset(cx, cy),
                style = Stroke(width = rimThickness)
            )

            // Recessed Inner LED Strip casting soft ambient light inward
            drawCircle(
                color = ambientLedColor.copy(alpha = 0.8f),
                radius = radius - 1f,
                center = Offset(cx, cy),
                style = Stroke(width = 3.5f)
            )

            // Subtle aluminum accent ring
            drawCircle(
                color = Color(0xFFE2E8F0),
                radius = radius + rimThickness,
                center = Offset(cx, cy),
                style = Stroke(width = 1.2f)
            )
        } else {
            // Fullscreen ambient perimeter LED diffusion
            val edgeGlow = ambientLedColor.copy(alpha = 0.12f)
            drawRect(color = edgeGlow)
        }
    }
}
