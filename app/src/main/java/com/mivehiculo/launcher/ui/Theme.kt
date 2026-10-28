package com.mivehiculo.launcher.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/** Paleta clara. Cambia los colores aquí para cambiar toda la apariencia. */
object Palette {
    val BgTop = Color(0xFFF8F6F3)
    val BgBottom = Color(0xFFE8E4DE)
    val Card = Color(0xFFFFFFFF)
    val Text = Color(0xFF1B1D21)
    val Muted = Color(0xFF6A6F78)
    val Track = Color(0xFFE9E5DF)
    val Chip = Color(0xFFF1EEE9)
    val Accent = Color(0xFFD9772B)      // cobre / naranja cálido
    val AccentDark = Color(0xFFB85F1C)
    val AccentSoft = Color(0xFFFCE9D9)
    val Shadow = Color(0x26000000)
}

/** Fondo off-white con degradado vertical y un brillo suave arriba a la izquierda (se dibuja una sola vez). */
fun Modifier.screenBackground(): Modifier = this.drawBehind {
    drawRect(Brush.verticalGradient(listOf(Palette.BgTop, Palette.BgBottom)))
    drawRect(
        Brush.radialGradient(
            colors = listOf(Color(0x99FFFFFF), Color.Transparent),
            center = Offset(size.width * 0.2f, 0f),
            radius = size.width * 0.7f,
        )
    )
}
