package com.example.adl_13

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.maplibre.android.MapLibre
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import android.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var mapLibreMap: MapLibreMap
    private val markers = mutableListOf<org.maplibre.android.annotations.Marker>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    lateinit var btnAgregar: Button
    lateinit var btnEliminar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Init MapLibre
        MapLibre.getInstance(this)

        // Init layout view
        val inflater = LayoutInflater.from(this)
        val rootView = inflater.inflate(R.layout.activity_main, null)
        setContentView(rootView)

        // Init the MapView
        mapView = rootView.findViewById(R.id.mapView)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapView.getMapAsync { map ->
            map.setStyle("https://demotiles.maplibre.org/style.json")
            map.cameraPosition =
                CameraPosition.Builder().target(LatLng(22.77636, -102.59731)).zoom(2.0).build()
            this.mapLibreMap = map
            marcadorUPIIZ()
            enableLocation()
        }

        btnAgregar = findViewById(R.id.btnAgregar)
        btnEliminar = findViewById(R.id.btnEliminar)

        btnAgregar.setOnClickListener {
            marcadoresAleatorios()
        }

        btnEliminar.setOnClickListener {
            borrarMarcadores()
        }
    }

    private fun enableLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val userLocation = LatLng(it.latitude, it.longitude)
                mapLibreMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 2.0))

                val userMarker = mapLibreMap.addMarker(
                    MarkerOptions()
                        .position(userLocation)
                        .title("Tu ubicación")
                        .snippet("Usted se encuentra aquí")
                )

                markers.add(userMarker)
            }
        }
    }

    fun marcadoresAleatorios() {
        val lat: Int = (-90..90).random()
        val lon: Int = (-180..180).random()
        val marker = mapLibreMap.addMarker(
            MarkerOptions().position(
                LatLng(
                    lat.toDouble(),
                    lon.toDouble()
                )
            )
                .title(
                    "Marcador aleatorio"
                )
                .snippet("Lat: $lat, Lon: $lon")
        )

        markers.add(marker)
    }

    fun marcadorUPIIZ() {
        val markerOptions = MarkerOptions()
            .position(LatLng(22.77636, -102.59731))
            .title("UPIIZ")
            .snippet("Huélum!")

        mapLibreMap.addMarker(markerOptions)
    }

    fun borrarMarcadores() {
        for (marker in markers) {
            mapLibreMap.removeMarker(marker)
        }
        markers.clear()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}