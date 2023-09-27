package com.example.mapboxapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.button.MaterialButton
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.Layer
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.SymbolLayer
import com.mapbox.maps.extension.style.layers.getLayer
import com.mapbox.maps.extension.style.layers.properties.generated.Visibility
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.getSourceAs
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.search.ResponseInfo
import com.mapbox.search.ReverseGeoOptions
import com.mapbox.search.SearchCallback
import com.mapbox.search.SearchEngine
import com.mapbox.search.SearchEngineSettings
import com.mapbox.search.common.AsyncOperationTask
import com.mapbox.search.result.SearchResult

class LocationPickerActivity : AppCompatActivity() {
    private val DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID"
    private var mapView: MapView? = null
    private lateinit var okButton: MaterialButton
    private val mapboxMap: MapboxMap? = null
    private lateinit var selectLocationButton: Button
    private val permissionsManager: PermissionsManager? = null
    private var hoveringMarker: ImageView? = null
    private lateinit var fixedPoint: Point
    private var droppedMarkerLayer: Layer? = null
    var PERMISSION_ALL = 1
    var PERMISSIONS = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_picker)
        okButton=findViewById(R.id.LocationfetchButton)
        okButton.visibility=View.GONE
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        mapView = findViewById(R.id.MapView)
        //ResourceOptionsManager.getDefault(this,getString(R.string.mapbox_public_token))
        onMapReady(mapView!!.getMapboxMap())
    }
    private fun onMapReady(mapboxMap: MapboxMap) {
        mapView!!.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(14.0)
                .build()
        )
        mapboxMap.loadStyleUri(
            //+geoJsonSource("dropped-icon-image"){}
            Style.MAPBOX_STREETS, onStyleLoaded = Style.OnStyleLoaded { style ->
                enableLocationPlugin()

                val hoveringMarker = ImageView(this@LocationPickerActivity)
                hoveringMarker.setImageResource(R.drawable.redpin)
                val params = FrameLayout.LayoutParams(
                    100,
                    100,
                    Gravity.CENTER
                )
                hoveringMarker.layoutParams = params
                mapView?.addView(hoveringMarker)
                initDroppedMarker(style)

                selectLocationButton = findViewById(R.id.select_location_button)
                selectLocationButton.setOnClickListener {
                    if (hoveringMarker.visibility == View.VISIBLE) {
                        val mapTargetLatLng: Point = mapboxMap.cameraState.center
                        fixedPoint=mapTargetLatLng
                        hoveringMarker.visibility = View.INVISIBLE
                        selectLocationButton.setBackgroundColor(Color.CYAN)
                        selectLocationButton.setText(R.string.cancel)
                        okButton.visibility= View.VISIBLE
                        okButton.setOnClickListener {
                            reverseGeoCode(mapTargetLatLng)
                        }
                        if (style.getLayer(DROPPED_MARKER_LAYER_ID) != null) {
                            val source: GeoJsonSource? =
                                style.getSourceAs("dropped-marker-source-id")
                            source?.feature(Feature.fromGeometry(mapTargetLatLng))

                            droppedMarkerLayer = style.getLayer(DROPPED_MARKER_LAYER_ID);
                            droppedMarkerLayer?.visibility(Visibility.VISIBLE)
                        }

                    } else {

// Switch the button appearance back to select a location.
                        selectLocationButton.setBackgroundColor(Color.YELLOW);
                        selectLocationButton.text = getString(R.string.Set_Location);
                        okButton.visibility= View.GONE
// Show the red hovering ImageView marker
                        hoveringMarker.visibility = View.VISIBLE;

                        // Hide the selected location SymbolLayer
                        droppedMarkerLayer = style.getLayer(DROPPED_MARKER_LAYER_ID);
                        if (droppedMarkerLayer != null) {
                            droppedMarkerLayer?.visibility(Visibility.NONE);
                        }
                    }
                }
            }
        )
    }

    private lateinit var searchEngine: SearchEngine
    private lateinit var searchRequestTask: AsyncOperationTask

    private val searchCallback = object : SearchCallback {

        override fun onResults(
            results: List<SearchResult>,
            responseInfo: ResponseInfo
        ) {
            if (results.isEmpty()) {
                Log.i("SearchApiExample", "No reverse geocoding results")
            } else {
                Toast.makeText(this@LocationPickerActivity,results[0].fullAddress, Toast.LENGTH_SHORT).show()
                Log.i("SearchApiExample", "Reverse geocoding results: $results")
                val intent = Intent(this@LocationPickerActivity,MapActivity::class.java)
                intent.putExtra("latitude",fixedPoint.latitude())
                intent.putExtra("longitude",fixedPoint.longitude())
                intent.putExtra("address",results[0].fullAddress)
                Log.i("sentValueLat",fixedPoint.latitude().toString())
                Log.i("sentValueLng",fixedPoint.longitude().toString())
                Log.i("sentValueadd",results[0].fullAddress.toString())
                startActivity(intent)
            }
        }

        override fun onError(e: Exception) {
            Log.i("SearchApiExample", "Reverse geocoding error $e", e)
        }
    }

    private fun reverseGeoCode(point: Point) {
        searchEngine = SearchEngine.createSearchEngineWithBuiltInDataProviders(
            SearchEngineSettings(getString(R.string.mapbox_access_token))
        )
        val options = ReverseGeoOptions(
            center = point,
            limit = 1
        )
        searchRequestTask = searchEngine.search(options, searchCallback)

    }

    private fun initDroppedMarker(loadedMapStyle: Style) {
        val drawable = AppCompatResources.getDrawable(applicationContext,R.drawable.fixedpin)
        val bitmap = drawable!!.toBitmap(width = 100, height = 100, config = null)
        loadedMapStyle.addImage(
            "dropped-icon-image",
            bitmap
            //BitmapFactory.decodeResource(applicationContext.resources, R.drawable.bluemarker)
        )
        GeoJsonSource.Builder("dropped-marker-source-id").build().bindTo(loadedMapStyle)

        loadedMapStyle.addLayer(
            SymbolLayer(DROPPED_MARKER_LAYER_ID, "dropped-marker-source-id")
                .iconImage("dropped-icon-image")
                .visibility(Visibility.NONE)
                .iconAllowOverlap(true)
                .iconIgnorePlacement(true)
        )

    }

    private fun enableLocationPlugin() {
        val locationComponentPlugin = mapView?.location
        locationComponentPlugin?.updateSettings {
            this.enabled = true
            this.locationPuck= LocationPuck2D()
        }
        locationComponentPlugin?.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        locationComponentPlugin?.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView!!.gestures.addOnMoveListener(onMoveListener)
    }
    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        mapView?.getMapboxMap()?.setCamera(CameraOptions.Builder().bearing(it).build())
    }

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        mapView!!.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
        mapView!!.gestures.focalPoint = mapView!!.getMapboxMap().pixelForCoordinate(it)
    }
    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
            onCameraTrackingDismissed()
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {

            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {
        }
    }
    private fun onCameraTrackingDismissed() {
        //Toast.makeText(this, "onCameraTrackingDismissed", Toast.LENGTH_SHORT).show()
        mapView!!.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView!!.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView!!.gestures.removeOnMoveListener(onMoveListener)
    }
    fun hasPermissions(context: Context?, permissions: Array<String>): Boolean {
        if (context != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }
}