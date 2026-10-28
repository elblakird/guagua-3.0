# Mi Launcher

Versión 0.4 · Diseño "OEM+" claro, pensado para un radio ligero (Android 11, ARMv7, 2 GB de RAM).

- Barra superior: nombre del coche, reloj grande y iconos de estado.
- Vista dividida: reproductor, tarjeta de Android Auto, vehículo (coche, velocidad, kilometraje) y mapa con clima.
- Dock inferior con 7 accesos: Radio, Música, Teléfono, Apps, Android Auto, Navegación y Ajustes.
- Reloj y fecha **reales** del sistema (se actualizan cada minuto con el aviso del propio Android).
- El dock abre apps reales (ver abajo). El clima es real (ver más abajo). Canción, velocidad y kilometraje siguen siendo **datos de prueba** de `data/Mock.kt`.
- Sin animaciones, sin imágenes: todo se dibuja con formas simples.

## 1. Obtener el APK (sin instalar nada)

1. Crea una cuenta en https://github.com y un repositorio nuevo (privado está bien).
2. En el repositorio, pulsa **Add file -> Upload files** y arrastra TODO el contenido de esta carpeta
   (incluida la carpeta oculta `.github`; si tu explorador no la muestra, activa "mostrar archivos ocultos").
3. Pulsa **Commit changes**.
4. Ve a la pestaña **Actions**. "Compilar APK" tarda unos 5-8 minutos.
   Si no arranca solo: entra en "Compilar APK" -> **Run workflow**.
5. Cuando salga el check verde, abre esa ejecución y descarga **mi-launcher-apk** (abajo, en *Artifacts*).
   El ZIP trae dos archivos:
   - `release/app-release.apk` -> **el recomendado para el radio** (optimizado, va más fluido).
   - `debug/app-debug.apk` -> versión de respaldo, más lenta, por si la release da problemas.

Si sale una X roja, copia el texto del error y envíamelo.

## 2. Instalar en el radio

Copia el APK al radio con una USB e instálalo desde el explorador de archivos, o por ADB:

    adb install -r app-release.apk

Para dejarlo como pantalla de inicio por defecto:

    adb shell cmd package set-home-activity com.mivehiculo.launcher/.MainActivity

Para volver al launcher original: desinstala la app, o

    adb shell pm clear-package-preferred-activities com.mivehiculo.launcher

## Qué abre cada botón

| Botón | Qué intenta, en orden |
|---|---|
| Ajustes | Ajustes del sistema -> apps de `Config.AJUSTES` |
| Teléfono | Marcador predeterminado -> apps de `Config.TELEFONO` |
| Música | App de música predeterminada -> apps de `Config.MUSICA`; si no hay, avisa con un mensaje |
| Navegación | Google Maps o Waze (`Config.NAVEGACION`) -> cualquier app de mapas |
| Apps | Lista de apps instaladas |
| Radio, Android Auto | Apps de `Config.RADIO` / `Config.ANDROID_AUTO` |

## Clima real (OpenWeatherMap)

1. Crea una cuenta gratis en https://openweathermap.org y copia tu API key (menú de tu cuenta -> "API keys").
   Una clave nueva puede tardar hasta un par de horas en activarse.
2. Pégala en `app/src/main/java/com/mivehiculo/launcher/Config.kt`, en `OPENWEATHER_API_KEY = ""`, entre las comillas.
3. **Importante:** si tu repositorio de GitHub es público, tu clave quedará visible. Usa un repositorio privado.

Sin clave, sin permiso de ubicación, sin GPS o sin internet, el clima muestra `--°C` y la app sigue funcionando.

## 3. Nombres de paquete (para que los botones abran TUS apps)

Cada app de Android tiene un "nombre de paquete" (ej. `com.waze`). Los botones los usan para saber qué abrir.
Los de `Config.kt` son suposiciones. Para ver los reales de tu radio, por ADB:

    adb shell pm list packages

Busca los de radio, bluetooth y mapas, y ponlos en `Config.kt`
(`app/src/main/java/com/mivehiculo/launcher/Config.kt`).
Si un botón dice "app no encontrada", es que su nombre de paquete no coincide.

## Dónde cambiar cada cosa

| Quiero cambiar... | Archivo |
|---|---|
| Nombre del coche, qué app abre cada botón | `Config.kt` |
| Textos de prueba (canción, km...) | `data/Mock.kt` |
| Clave del clima | `Config.kt` |
| Colores (naranja/cobre, fondo, textos) | `ui/Theme.kt` |
| Distribución, tamaños, tarjetas, dock | `ui/HomeScreen.kt` |
| Iconos dibujados, coche y mapa | `ui/Icons.kt` |
| Lista de apps | `ui/AppDrawer.kt` |
