package com.example.mapboxapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.example.mapboxapp.dataclass.issues
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location

class MapActivity : AppCompatActivity() {

    private val resArray = arrayListOf<Int>(R.drawable.red_marker,R.drawable.blackmarker,R.drawable.yellowpin,R.drawable.bluemarker, R.drawable.greenmarker,R.drawable.purplemarker,R.drawable.lightbluemarker)
    private val typeArray = arrayListOf<String>("Urban Flood","Rural Flood","Drought","Drainage Issues","Oil Spills","Tsunami","Polluted River")
    val problemTypes = arrayOf(
        "none",
        "Urban Flooding",
        "Rural Flooding",
        "Oil Spill",
        "Tsunami",
        "Polluted River",
        "Drought",
        "Drainage problems"
    )

    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())
    }

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
        mapView.gestures.focalPoint = mapView.getMapboxMap().pixelForCoordinate(it)
    }
    private lateinit var locationList:ArrayList<LocationData>
    private lateinit var resList:ArrayList<Int>
    var PERMISSION_ALL = 1
    var PERMISSIONS = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    private lateinit var location:LocationData
    private lateinit var dbref : DatabaseReference

    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
            onCameraTrackingDismissed()
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {}
    }
    private lateinit var mapView: MapView
    private lateinit var legend:ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        mapView = findViewById(R.id.mapView)
        dbref = FirebaseDatabase.getInstance().getReference("problems")
        //location= LocationData((intent.getDoubleExtra("latitude",0.0)),(intent.getDoubleExtra("longitude",0.0)),intent.getStringExtra("address")!!)
        /*Log.i("recievedValueLat",location.latitude.toString())
        Log.i("recievedValueLng",location.longitude.toString())
        Log.i("recievedValueadd",location.address)*/
        mapView.setOnClickListener {
            legend.visibility = if (legend.visibility==View.VISIBLE) View.GONE else View.VISIBLE
        }
        onMapReady()
        fetchRecyclerView()
        //addAnnotationToMap(location)
    }
    private fun fetchRecyclerView() {
        locationList= arrayListOf()
        resList= arrayListOf()
        for (i in 1..7) {
            dbref.child(problemTypes[i]).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (dataSnap in snapshot.children) {
                            val data = dataSnap?.getValue(issues::class.java)
                            if (data != null) {
                                locationList.add(LocationData(data.locationLat, data.locationLong,data.city))
                                val type = data.type
                                for( (index,t) in problemTypes.withIndex())
                                    if(type.contentEquals(t))
                                    {
                                        resList.add(resArray[index])
                                    }
                            }

                        }


                        /*val itemAdapter = issuesAdapter(issueList)
                        itemRecyclerView.adapter = itemAdapter*/

//                        itemAdapter.setOnItemClickListener(object: issuesAdapter.onItemClickListener{
//                            override fun onItemClick(position: Int) {
//                                //onClick
//
//                            }
//
//                        })

                    }

                    addAnnotationToMap()

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MapActivity, error.toString(), Toast.LENGTH_LONG).show()
                }
            })
        }
    }
    private fun onMapReady() {
        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(14.0)
                .build()
        )
        mapView.getMapboxMap().loadStyleUri(
            Style.MAPBOX_STREETS
        ) {
            initLocationComponent()
            setupGesturesListener()
        }
    }

    private fun setupGesturesListener() {
        mapView.gestures.addOnMoveListener(onMoveListener)
    }
    private fun addAnnotationToMap() {
// Create an instance of the Annotation API and get the PointAnnotationManager.
        for( (index,location) in locationList.withIndex()) {

            bitmapFromDrawableRes(
                this@MapActivity,
                resList[index]
            )?.let {
                val annotationApi = mapView.annotations
                val pointAnnotationManager = annotationApi.createPointAnnotationManager()
// Set options for the resulting symbol layer.
                val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                var lat = 0.0
                var long = 0.0;
                val locationListener = LocationListener { location ->
                    lat = location.latitude
                    long = location.longitude

                }
                val pointAnnotationOptions: PointAnnotationOptions =
                    PointAnnotationOptions().withIconImage(it)
                        .withPoint(Point.fromLngLat(location.longitude, location.latitude))
                pointAnnotationManager.create(pointAnnotationOptions)
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {


                } else {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        0f,
                        locationListener
                    )
                    Log.i("getLoc", "location retrieved $lat + $long")
                }
            }
        }
    }
    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
// copying drawable object to not manipulate on the same reference
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                20, 20,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }

    private fun initLocationComponent() {
        val locationComponentPlugin = mapView.location
        locationComponentPlugin.updateSettings {
            this.enabled = true
            this.locationPuck = LocationPuck2D(
                bearingImage = AppCompatResources.getDrawable(
                    this@MapActivity,
                    R.drawable.circleicon,
                ),
                shadowImage = AppCompatResources.getDrawable(
                    this@MapActivity,
                    R.drawable.circleicon,
                ),
                scaleExpression = interpolate {
                    linear()
                    zoom()
                    stop {
                        literal(0.0)
                        literal(0.6)
                    }
                    stop {
                        literal(20.0)
                        literal(1.0)
                    }
                }.toJson()
            )
        }
        locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
    }

    private fun onCameraTrackingDismissed() {
        Toast.makeText(this, "onCameraTrackingDismissed", Toast.LENGTH_SHORT).show()
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }
}