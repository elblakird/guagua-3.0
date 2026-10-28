package com.mivehiculo.launcher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mivehiculo.launcher.Config
import com.mivehiculo.launcher.data.Mock

data class HomeActions(
    val openRadio: () -> Unit,
    val openMusic: () -> Unit,
    val openPhone: () -> Unit,
    val openApps: () -> Unit,
    val openAndroidAuto: () -> Unit,
    val openNavigation: () -> Unit,
    val openSettings: () -> Unit,
)

@Composable
fun HomeScreen(time: String, date: String, weather: String, feels: String, actions: HomeActions) {
    BoxWithConstraints(Modifier.fillMaxSize().screenBackground()) {
        // Escala: todo se dimensiona respecto a una pantalla de 600 dp de alto.
        val s = (maxHeight.value / 600f).coerceIn(0.6f, 2.5f)

        Column(Modifier.fillMaxSize()) {
            TopBar(s, time, date)

            Row(
                Modifier.weight(1f).fillMaxWidth().padding(horizontal = (24 * s).dp),
                horizontalArrangement = Arrangement.spacedBy((18 * s).dp),
            ) {
                Column(
                    Modifier.weight(1.15f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy((16 * s).dp),
                ) {
                    MusicCard(s, Modifier.weight(1f).fillMaxWidth())
                    AutoCard(s, actions.openAndroidAuto, Modifier.fillMaxWidth().height((96 * s).dp))
                }
                VehicleCard(s, Modifier.weight(1f).fillMaxHeight())
                MapCard(s, weather, feels, actions.openNavigation, Modifier.weight(1.15f).fillMaxHeight())
            }

            Spacer(Modifier.height((16 * s).dp))
            Dock(s, actions)
        }
    }
}

/* ---------- Barra superior ---------- */

@Composable
private fun TopBar(s: Float, time: String, date: String) {
    Row(
        Modifier.fillMaxWidth().height((80 * s).dp).padding(horizontal = (32 * s).dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                Config.VEHICULO,
                color = Palette.Text,
                fontSize = (24 * s).sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(date, color = Palette.Muted, fontSize = (15 * s).sp, maxLines = 1)
        }
        Text(time, color = Palette.Text, fontSize = (56 * s).sp, fontWeight = FontWeight.Light)
        Row(
            Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy((22 * s).dp, Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GlyphIcon(Glyph.Brightness, Palette.Muted, (30 * s).dp)
            GlyphIcon(Glyph.Volume, Palette.Muted, (30 * s).dp)
            GlyphIcon(Glyph.Wifi, Palette.Accent, (30 * s).dp)
            GlyphIcon(Glyph.Bluetooth, Palette.Accent, (30 * s).dp)
        }
    }
}

/* ---------- Tarjetas ---------- */

@Composable
private fun FloatingCard(
    s: Float,
    modifier: Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val shape = RoundedCornerShape((32 * s).dp)
    Box(
        modifier
            .shadow((10 * s).dp, shape, clip = false, ambientColor = Palette.Shadow, spotColor = Palette.Shadow)
            .clip(shape)
            .background(Palette.Card)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        content = content,
    )
}

@Composable
private fun RoundButton(glyph: Glyph, diameter: Dp, background: Color, foreground: Color) {
    Box(
        Modifier.size(diameter).clip(CircleShape).background(background),
        contentAlignment = Alignment.Center,
    ) {
        GlyphIcon(glyph, foreground, diameter * 0.5f)
    }
}

@Composable
private fun MusicCard(s: Float, modifier: Modifier) {
    FloatingCard(s, modifier) {
        Column(
            Modifier.fillMaxSize().padding((20 * s).dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size((84 * s).dp)
                        .clip(RoundedCornerShape((20 * s).dp))
                        .background(Brush.linearGradient(listOf(Palette.Accent, Color(0xFF8E3F10)))),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("🎵", fontSize = (38 * s).sp)
                }
                Spacer(Modifier.width((18 * s).dp))
                Column {
                    Text(
                        Mock.SONG,
                        color = Palette.Text,
                        fontSize = (28 * s).sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(Mock.ARTIST, color = Palette.Muted, fontSize = (20 * s).sp, maxLines = 1)
                }
            }

            Column {
                Box(
                    Modifier.fillMaxWidth().height((6 * s).dp).clip(CircleShape).background(Palette.Track),
                ) {
                    Box(Modifier.fillMaxWidth(Mock.PROGRESS).fillMaxHeight().background(Palette.Accent))
                }
                Spacer(Modifier.height((6 * s).dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(Mock.ELAPSED, color = Palette.Muted, fontSize = (15 * s).sp)
                    Text(Mock.DURATION, color = Palette.Muted, fontSize = (15 * s).sp)
                }
            }

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RoundButton(Glyph.Prev, (58 * s).dp, Palette.Chip, Palette.Text)
                RoundButton(Glyph.Play, (70 * s).dp, Palette.Accent, Color.White)
                RoundButton(Glyph.Next, (58 * s).dp, Palette.Chip, Palette.Text)
            }
        }
    }
}

@Composable
private fun AutoCard(s: Float, onClick: () -> Unit, modifier: Modifier) {
    FloatingCard(s, modifier, onClick) {
        Row(
            Modifier.fillMaxSize().padding(horizontal = (20 * s).dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier.size((58 * s).dp).clip(CircleShape).background(Palette.AccentSoft),
                contentAlignment = Alignment.Center,
            ) {
                Text("📱", fontSize = (28 * s).sp)
            }
            Spacer(Modifier.width((16 * s).dp))
            Column(Modifier.weight(1f)) {
                Text(
                    "Android Auto",
                    color = Palette.Text,
                    fontSize = (22 * s).sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                )
                Text("Conectar teléfono", color = Palette.Muted, fontSize = (16 * s).sp, maxLines = 1)
            }
            Box(
                Modifier
                    .clip(CircleShape)
                    .background(Palette.Accent)
                    .padding(horizontal = (20 * s).dp, vertical = (10 * s).dp),
            ) {
                Text("Conectar", color = Color.White, fontSize = (18 * s).sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun VehicleCard(s: Float, modifier: Modifier) {
    FloatingCard(s, modifier) {
        Column(
            Modifier.fillMaxSize().padding((22 * s).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CarIllustration(Modifier.fillMaxWidth().weight(1f))
            Spacer(Modifier.height((8 * s).dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(Mock.SPEED, color = Palette.Accent, fontSize = (72 * s).sp, fontWeight = FontWeight.Medium)
                        Text(
                            " km/h",
                            Modifier.padding(bottom = (14 * s).dp),
                            color = Palette.Muted,
                            fontSize = (20 * s).sp,
                        )
                    }
                }
                Box(Modifier.width(1.dp).height((56 * s).dp).background(Palette.Track))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(Mock.ODOMETER, color = Palette.Text, fontSize = (34 * s).sp, fontWeight = FontWeight.Medium)
                    Text("km recorridos", color = Palette.Muted, fontSize = (16 * s).sp)
                }
            }
        }
    }
}

@Composable
private fun MapCard(s: Float, weather: String, feels: String, onClick: () -> Unit, modifier: Modifier) {
    FloatingCard(s, modifier, onClick) {
        MapIllustration(Modifier.fillMaxSize())

        // Clima discreto, arriba a la derecha
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .padding((14 * s).dp)
                .clip(CircleShape)
                .background(Color(0xE6FFFFFF))
                .padding(horizontal = (14 * s).dp, vertical = (7 * s).dp),
        ) {
            Column {
                Text(weather, color = Palette.Muted, fontSize = (15 * s).sp, fontWeight = FontWeight.Medium)
                if (feels.isNotEmpty()) {
                    Text(feels, color = Palette.Muted, fontSize = (13 * s).sp)
                }
            }
        }

        // Etiqueta del mapa, abajo a la izquierda
        Row(
            Modifier
                .align(Alignment.BottomStart)
                .padding((14 * s).dp)
                .clip(CircleShape)
                .background(Color(0xF2FFFFFF))
                .padding(horizontal = (16 * s).dp, vertical = (9 * s).dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(Modifier.size((12 * s).dp).clip(CircleShape).background(Palette.Accent))
            Spacer(Modifier.width((10 * s).dp))
            Text("Google Maps", color = Palette.Text, fontSize = (20 * s).sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

/* ---------- Dock inferior ---------- */

@Composable
private fun Dock(s: Float, actions: HomeActions) {
    val shape = RoundedCornerShape(topStart = (32 * s).dp, topEnd = (32 * s).dp)
    Row(
        Modifier
            .fillMaxWidth()
            .shadow((14 * s).dp, shape, clip = false, ambientColor = Palette.Shadow, spotColor = Palette.Shadow)
            .background(Palette.Card, shape)
            .padding(horizontal = (16 * s).dp, vertical = (14 * s).dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top,
    ) {
        DockItem(s, "Radio", false, actions.openRadio) { Emoji("📻", s) }
        DockItem(s, "Música", true, actions.openMusic) { Emoji("🎵", s) }
        DockItem(s, "Teléfono", false, actions.openPhone) { Emoji("📞", s) }
        DockItem(s, "Apps", false, actions.openApps) { GlyphIcon(Glyph.Apps, Palette.Text, (30 * s).dp) }
        DockItem(s, "Android Auto", false, actions.openAndroidAuto) { Emoji("📱", s) }
        DockItem(s, "Navegación", false, actions.openNavigation) { Emoji("🧭", s) }
        DockItem(s, "Ajustes", false, actions.openSettings) { Emoji("⚙️", s) }
    }
}

@Composable
private fun Emoji(text: String, s: Float) {
    Text(text, fontSize = (30 * s).sp)
}

@Composable
private fun DockItem(
    s: Float,
    label: String,
    active: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
) {
    Column(
        Modifier
            .clip(RoundedCornerShape((20 * s).dp))
            .clickable(onClick = onClick)
            .padding(horizontal = (10 * s).dp, vertical = (4 * s).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier
                .size((64 * s).dp)
                .clip(CircleShape)
                .background(if (active) Palette.AccentSoft else Palette.Chip),
            contentAlignment = Alignment.Center,
        ) {
            icon()
        }
        Spacer(Modifier.height((6 * s).dp))
        Text(
            label,
            color = if (active) Palette.AccentDark else Palette.Text,
            fontSize = (17 * s).sp,
            fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
            maxLines = 1,
        )
    }
}
