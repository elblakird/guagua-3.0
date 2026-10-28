package com.mivehiculo.launcher.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.Dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

enum class Glyph { Brightness, Volume, Wifi, Bluetooth, Play, Prev, Next, Apps }

/** Iconos simples dibujados con formas (sin imágenes ni librerías de iconos). */
@Composable
fun GlyphIcon(glyph: Glyph, color: Color, dp: Dp) {
    Canvas(Modifier.size(dp)) {
        val w = size.width
        val h = size.height
        when (glyph) {
            Glyph.Brightness -> {
                val c = Offset(w / 2f, h / 2f)
                drawCircle(color, radius = w * 0.18f, center = c)
                for (i in 0 until 8) {
                    val a = i * PI.toFloat() / 4f
                    drawLine(
                        color,
                        Offset(c.x + w * 0.30f * cos(a), c.y + w * 0.30f * sin(a)),
                        Offset(c.x + w * 0.45f * cos(a), c.y + w * 0.45f * sin(a)),
                        strokeWidth = w * 0.08f,
                        cap = StrokeCap.Round,
                    )
                }
            }
            Glyph.Volume -> {
                val body = Path().apply {
                    moveTo(w * 0.10f, h * 0.38f); lineTo(w * 0.28f, h * 0.38f)
                    lineTo(w * 0.50f, h * 0.18f); lineTo(w * 0.50f, h * 0.82f)
                    lineTo(w * 0.28f, h * 0.62f); lineTo(w * 0.10f, h * 0.62f)
                    close()
                }
                drawPath(body, color)
                val st = Stroke(width = w * 0.08f, cap = StrokeCap.Round)
                drawArc(color, startAngle = -45f, sweepAngle = 90f, useCenter = false,
                    topLeft = Offset(w * 0.35f, h * 0.30f), size = Size(w * 0.34f, h * 0.40f), style = st)
                drawArc(color, startAngle = -45f, sweepAngle = 90f, useCenter = false,
                    topLeft = Offset(w * 0.24f, h * 0.16f), size = Size(w * 0.56f, h * 0.68f), style = st)
            }
            Glyph.Wifi -> {
                val st = Stroke(width = w * 0.09f, cap = StrokeCap.Round)
                val cx = w / 2f
                val cy = h * 0.82f
                for (r in listOf(0.25f, 0.47f, 0.69f)) {
                    val rad = w * r
                    drawArc(color, startAngle = 225f, sweepAngle = 90f, useCenter = false,
                        topLeft = Offset(cx - rad, cy - rad), size = Size(rad * 2f, rad * 2f), style = st)
                }
                drawCircle(color, radius = w * 0.07f, center = Offset(cx, cy))
            }
            Glyph.Bluetooth -> {
                val p = Path().apply {
                    moveTo(w * 0.28f, h * 0.30f); lineTo(w * 0.72f, h * 0.70f)
                    lineTo(w * 0.50f, h * 0.90f); lineTo(w * 0.50f, h * 0.10f)
                    lineTo(w * 0.72f, h * 0.30f); lineTo(w * 0.28f, h * 0.70f)
                }
                drawPath(p, color, style = Stroke(width = w * 0.09f, cap = StrokeCap.Round, join = StrokeJoin.Round))
            }
            Glyph.Play -> {
                val p = Path().apply {
                    moveTo(w * 0.30f, h * 0.18f); lineTo(w * 0.82f, h * 0.50f); lineTo(w * 0.30f, h * 0.82f); close()
                }
                drawPath(p, color)
            }
            Glyph.Prev -> {
                drawRect(color, topLeft = Offset(w * 0.18f, h * 0.22f), size = Size(w * 0.10f, h * 0.56f))
                val p = Path().apply {
                    moveTo(w * 0.84f, h * 0.22f); lineTo(w * 0.34f, h * 0.50f); lineTo(w * 0.84f, h * 0.78f); close()
                }
                drawPath(p, color)
            }
            Glyph.Next -> {
                drawRect(color, topLeft = Offset(w * 0.72f, h * 0.22f), size = Size(w * 0.10f, h * 0.56f))
                val p = Path().apply {
                    moveTo(w * 0.16f, h * 0.22f); lineTo(w * 0.66f, h * 0.50f); lineTo(w * 0.16f, h * 0.78f); close()
                }
                drawPath(p, color)
            }
            Glyph.Apps -> {
                for (i in 0..2) for (j in 0..2) {
                    drawCircle(color, radius = w * 0.075f, center = Offset(w * (0.22f + 0.28f * i), h * (0.22f + 0.28f * j)))
                }
            }
        }
    }
}

/** Ilustración estática de un coche (vista lateral), centrada y con proporción fija. */
@Composable
fun CarIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val cw = minOf(size.width, size.height * 2.5f)
        val ch = cw / 2.5f
        translate(left = (size.width - cw) / 2f, top = (size.height - ch) / 2f) {
            // sombra en el suelo
            drawOval(Color(0x22000000), topLeft = Offset(cw * 0.06f, ch * 0.90f), size = Size(cw * 0.88f, ch * 0.09f))
            // cabina
            val cabin = Path().apply {
                moveTo(cw * 0.20f, ch * 0.52f); lineTo(cw * 0.30f, ch * 0.16f)
                lineTo(cw * 0.70f, ch * 0.16f); lineTo(cw * 0.84f, ch * 0.52f); close()
            }
            drawPath(cabin, Color(0xFF3A3F47))
            // ventanas
            val win1 = Path().apply {
                moveTo(cw * 0.24f, ch * 0.50f); lineTo(cw * 0.32f, ch * 0.22f)
                lineTo(cw * 0.49f, ch * 0.22f); lineTo(cw * 0.49f, ch * 0.50f); close()
            }
            val win2 = Path().apply {
                moveTo(cw * 0.52f, ch * 0.22f); lineTo(cw * 0.68f, ch * 0.22f)
                lineTo(cw * 0.79f, ch * 0.50f); lineTo(cw * 0.52f, ch * 0.50f); close()
            }
            drawPath(win1, Color(0xFFDDE3EA))
            drawPath(win2, Color(0xFFDDE3EA))
            // carrocería
            drawRoundRect(Color(0xFF2B2F36), topLeft = Offset(0f, ch * 0.46f),
                size = Size(cw, ch * 0.38f), cornerRadius = CornerRadius(ch * 0.14f))
            // franja cobre
            drawRoundRect(Palette.Accent, topLeft = Offset(cw * 0.04f, ch * 0.62f),
                size = Size(cw * 0.92f, ch * 0.035f), cornerRadius = CornerRadius(ch * 0.02f))
            // faros
            drawRoundRect(Color(0xFFFFE2B8), topLeft = Offset(cw * 0.93f, ch * 0.52f),
                size = Size(cw * 0.05f, ch * 0.07f), cornerRadius = CornerRadius(ch * 0.03f))
            drawRoundRect(Palette.AccentDark, topLeft = Offset(cw * 0.02f, ch * 0.52f),
                size = Size(cw * 0.04f, ch * 0.07f), cornerRadius = CornerRadius(ch * 0.03f))
            // ruedas
            for (x in listOf(0.24f, 0.76f)) {
                drawCircle(Color(0xFF15171A), radius = ch * 0.17f, center = Offset(cw * x, ch * 0.80f))
                drawCircle(Color(0xFFC9CED6), radius = ch * 0.08f, center = Offset(cw * x, ch * 0.80f))
            }
        }
    }
}

/** Mapa simulado: bloques de color, calles blancas y una ruta cobre. */
@Composable
fun MapIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        drawRect(Color(0xFFEFEDE7))
        drawRoundRect(Color(0xFFDDE8D3), topLeft = Offset(w * 0.06f, h * 0.58f),
            size = Size(w * 0.28f, h * 0.26f), cornerRadius = CornerRadius(w * 0.03f))
        drawRoundRect(Color(0xFFD6E6F0), topLeft = Offset(w * 0.62f, h * 0.08f),
            size = Size(w * 0.30f, h * 0.20f), cornerRadius = CornerRadius(w * 0.03f))

        val road = Color.White
        val sw = w * 0.045f
        drawLine(road, Offset(0f, h * 0.45f), Offset(w, h * 0.45f), strokeWidth = sw)
        drawLine(road, Offset(0f, h * 0.18f), Offset(w, h * 0.18f), strokeWidth = sw * 0.7f)
        drawLine(road, Offset(w * 0.40f, 0f), Offset(w * 0.40f, h), strokeWidth = sw)
        drawLine(road, Offset(w * 0.72f, 0f), Offset(w * 0.72f, h), strokeWidth = sw * 0.8f)
        drawLine(road, Offset(w * 0.15f, h), Offset(w * 0.15f, h * 0.45f), strokeWidth = sw * 0.6f)

        val route = Path().apply {
            moveTo(w * 0.40f, h * 0.96f); lineTo(w * 0.40f, h * 0.45f)
            lineTo(w * 0.72f, h * 0.45f); lineTo(w * 0.72f, h * 0.30f)
        }
        drawPath(route, Palette.Accent, style = Stroke(width = sw * 0.45f, cap = StrokeCap.Round, join = StrokeJoin.Round))

        drawCircle(Color.White, radius = sw * 0.85f, center = Offset(w * 0.40f, h * 0.70f))
        drawCircle(Palette.Accent, radius = sw * 0.55f, center = Offset(w * 0.40f, h * 0.70f))
    }
}
