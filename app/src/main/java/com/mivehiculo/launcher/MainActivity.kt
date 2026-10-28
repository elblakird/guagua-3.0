package com.mivehiculo.launcher

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mivehiculo.launcher.data.AppEntry
import com.mivehiculo.launcher.data.Apps
import com.mivehiculo.launcher.data.ClockFormat
import com.mivehiculo.launcher.data.WeatherData
import com.mivehiculo.launcher.data.WeatherManager
import com.mivehiculo.launcher.ui.AppDrawer
import com.mivehiculo.launcher.ui.HomeActions
import com.mivehiculo.launcher.ui.HomeScreen
import java.util.Date

class MainActivity : ComponentActivity() {

    /** null = pantalla de inicio; con valor = lista de apps abierta. */
    private var drawerApps by mutableStateOf<List<AppEntry>?>(null)
    private var loadingApps = false

    private var time by mutableStateOf("")
    private var date by mutableStateOf("")
    private var weather by mutableStateOf<WeatherData?>(null)

    private val weatherManager by lazy { WeatherManager(this) { weather = it } }

    /** Pide el permiso de ubicación con el diálogo estándar de Android; al responder, actualiza el clima. */
    private val locationPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            weatherManager.refresh()
        }

    /** El sistema avisa solo cada minuto (TIME_TICK): no hay temporizadores ni bucles. */
    private val timeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            refreshClock()
            weatherManager.refreshIfDue()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        refreshClock()
        weather = weatherManager.cached()

        if (weatherManager.shouldAskPermission()) {
            try {
                locationPermission.launch(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                )
            } catch (e: Exception) {
                // Algunos firmwares no traen el diálogo de permisos: la app sigue sin clima.
            }
        }

        val actions = HomeActions(
            openRadio = { open("Radio", Apps.launchAny(this, Config.RADIO)) },
            openMusic = {
                // App de música predeterminada del sistema; si no hay, la lista de Config.
                val ok = Apps.tryOpen(this, Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_MUSIC)) ||
                    Apps.launchAny(this, Config.MUSICA)
                if (!ok) toast("No hay app de música instalada")
            },
            openPhone = {
                // Marcador predeterminado; si no hay, la app de Bluetooth/teléfono del radio.
                open("Teléfono", Apps.tryOpen(this, Intent(Intent.ACTION_DIAL)) || Apps.launchAny(this, Config.TELEFONO))
            },
            openApps = { showApps() },
            openAndroidAuto = { open("Android Auto", Apps.launchAny(this, Config.ANDROID_AUTO)) },
            openNavigation = {
                // Google Maps o Waze (lo que esté instalado); si no, cualquier app de mapas del sistema.
                open(
                    "Navegación",
                    Apps.launchAny(this, Config.NAVEGACION) ||
                        Apps.tryOpen(this, Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_MAPS)),
                )
            },
            openSettings = {
                open("Ajustes", Apps.tryOpen(this, Intent(Settings.ACTION_SETTINGS)) || Apps.launchAny(this, Config.AJUSTES))
            },
        )

        setContent {
            val apps = drawerApps
            if (apps != null) {
                BackHandler { drawerApps = null }
                AppDrawer(
                    apps = apps,
                    onLaunch = {
                        if (!Apps.launch(this@MainActivity, it.packageName)) toast("No se pudo abrir ${it.label}")
                        drawerApps = null
                    },
                    onClose = { drawerApps = null },
                )
            } else {
                BackHandler { /* en la pantalla de inicio, "atrás" no hace nada */ }
                val w = weather
                HomeScreen(time, date, w?.headline ?: "--°C", w?.feelsLine ?: "", actions)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        refreshClock()
        registerReceiver(
            timeReceiver,
            IntentFilter().apply {
                addAction(Intent.ACTION_TIME_TICK)
                addAction(Intent.ACTION_TIME_CHANGED)
                addAction(Intent.ACTION_TIMEZONE_CHANGED)
            },
        )
        weatherManager.refreshIfDue()
    }

    /** Cuando el launcher no está a la vista, se deja de escuchar: consumo cero. */
    override fun onStop() {
        unregisterReceiver(timeReceiver)
        super.onStop()
    }

    /** Al pulsar el botón Home estando ya en inicio, se cierra la lista de apps. */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        drawerApps = null
    }

    private fun refreshClock() {
        val now = Date()
        time = ClockFormat.time(this, now)
        date = ClockFormat.date(now)
    }

    /** La lista de apps se arma en un hilo aparte para no congelar la pantalla. */
    private fun showApps() {
        if (loadingApps) return
        loadingApps = true
        Thread {
            val list = try {
                Apps.installed(this)
            } catch (e: Exception) {
                emptyList()
            }
            runOnUiThread {
                drawerApps = list
                loadingApps = false
            }
        }.start()
    }

    private fun open(nombre: String, opened: Boolean) {
        if (!opened) toast("$nombre: app no encontrada (ver Config.kt)")
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}
