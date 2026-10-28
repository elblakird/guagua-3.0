package com.mivehiculo.launcher

/**
 * AQUÍ se personaliza lo propio de tu radio.
 *
 * Cada botón abre la PRIMERA app instalada de su lista. Si tu radio usa otra
 * app, añade su "nombre de paquete" al principio de la lista.
 * (Cómo averiguarlo: ver README.md, sección "Nombres de paquete".)
 */
object Config {
    /** Nombre que aparece arriba a la izquierda. */
    const val VEHICULO = "Hyundai Santa Fe"

    /**
     * Clave gratuita de OpenWeatherMap (https://openweathermap.org/api).
     * Pégala entre las comillas. Si se deja vacía, el clima muestra "--°C".
     */
    const val OPENWEATHER_API_KEY = ""

    // Suposiciones habituales: ajústalas a tu radio.
    val RADIO = listOf("com.jancar.radio", "com.syu.radio", "com.mediatek.fmradio", "com.android.fmradio")
    val MUSICA = listOf("com.spotify.music", "com.google.android.apps.youtube.music", "com.android.music")
    val TELEFONO = listOf("com.jancar.bluetooth", "com.syu.bt", "com.google.android.dialer", "com.android.dialer")
    val NAVEGACION = listOf("com.google.android.apps.maps", "com.waze", "net.osmand", "net.osmand.plus", "app.organicmaps")
    val ANDROID_AUTO = listOf("com.google.android.projection.gearhead")
    val AJUSTES = listOf("com.android.settings")
}
