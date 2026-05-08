Nombre de la app:
Charging Station Finder

Equipo:
Equipo Charging Station Finder

Descripción:
Aplicación Android para buscar y visualizar estaciones de recarga para vehículos eléctricos e híbridos. La app permite registrar un usuario, consultar estaciones de carga, buscar por texto, filtrar resultados, guardar favoritos y agregar reseñas locales.

Tecnologias usadas:
- Activities
- Fragments
- ViewBinding
- RecyclerView
- Room
- LiveData
- MVVM
- Coroutines
- Datos locales desde CSV
- OpenStreetMap/osmdroid
- SharedPreferences
- Testing with JUnit/Espresso

Versión mínima de Android:
minSdk 24

Instrucciones para ejecutar:
1. Abrir el proyecto en Android Studio.
2. Sincronizar Gradle.
3. Ejecutar la app en un emulador o dispositivo Android.
4. Registrarse con cualquier nombre y correo de prueba.
5. Usar la lista o mapa para buscar estaciones.
6. Abrir una estación para ver detalles, guardar favoritos o agregar reseñas.

Credenciales de prueba:
No se requieren credenciales reales.

Configuración especial:
La información de estaciones se carga desde el archivo local app/src/main/assets/centros_de_carga.csv. No se requieren llaves de API. La pantalla de mapa usa OpenStreetMap/osmdroid y necesita conexión a internet para descargar los mosaicos del mapa.

Mapa:
La pantalla de mapa usa osmdroid con OpenStreetMap. Muestra marcadores para las estaciones con coordenadas válidas. Al tocar un marcador se muestra información de la estación; al tocarlo de nuevo se abre la pantalla de detalles.

Pruebas:
- Unit tests: app/src/test/java/victor/berlanga/chargingstationfinder/ChargingStationLogicTest.kt
- Instrumented tests: app/src/androidTest/java/victor/berlanga/chargingstationfinder/ChargingStationUiTest.kt