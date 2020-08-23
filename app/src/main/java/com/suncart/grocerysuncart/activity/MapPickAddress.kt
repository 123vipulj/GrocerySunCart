package com.suncart.grocerysuncart.activity

import android.Manifest
import android.animation.IntEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import com.dbflow5.config.FlowManager
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.material.textfield.TextInputEditText
import com.suncart.grocerysuncart.R
import com.suncart.grocerysuncart.constant.map.ConstantMap
import com.suncart.grocerysuncart.constant.map.locationListener
import com.suncart.grocerysuncart.database.AppDatabase
import com.suncart.grocerysuncart.util.DbUtils
import com.suncart.grocerysuncart.util.map.FetchAddressIntentService
import com.suncart.grocerysuncart.util.map.Location
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.map_layout_activity.*

class MapPickAddress : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val REQUEST_LOCATION_PERMISSION = 1
    var marker: Marker? = null
    var location: Location? = null
    private var mResultReceiver: AddressResultReceiver? = null
    lateinit var locationCallback: LocationCallback
    lateinit var groundOverlay : GroundOverlay
    var radius = 100000
    var valueAnimator : ValueAnimator? = null
    lateinit var exactLoc : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        FlowManager.init(this)

        Places.initialize(this, resources.getString(R.string.google_maps_key))
        val placesClient = Places.createClient(this)

        mResultReceiver = AddressResultReceiver(Handler())

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // toolbar
        val nav_icon  = supportActionBar?.customView?.findViewById<ImageView>(R.id.navigation_drawer)
        val cartImg = supportActionBar?.customView?.findViewById<ImageView>(R.id.cart_icons)
        val totalCart = supportActionBar?.customView?.findViewById<TextView>(R.id.total_cart)
        val titleBar = supportActionBar?.customView?.findViewById<TextView>(R.id.title_appbar)
        titleBar?.setText("My Pickup")
        titleBar?.setTextColor(Color.BLACK)

        nav_icon?.visibility = View.GONE
        cartImg?.visibility = View.GONE
        totalCart?.visibility = View.GONE

        toolbar.setBackgroundColor(Color.parseColor("#FF7B5FAE"))
        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

//        valueAnimator = ValueAnimator()
//        valueAnimator!!.repeatCount = ValueAnimator.INFINITE
//        valueAnimator!!.repeatMode = ValueAnimator.RESTART
//        valueAnimator!!.setIntValues(0, radius)
//        valueAnimator!!.duration = 3000
//        valueAnimator!!.setEvaluator(IntEvaluator())
//        valueAnimator!!.interpolator = AccelerateDecelerateInterpolator()

        // initialize custom location
        location = Location(
            this,
            object :
                locationListener {
                override fun locationResponse(locationResult: LocationResult) {
                    mMap.clear()
                    val currentLoc = LatLng(
                        locationResult.lastLocation.latitude,
                        locationResult.lastLocation.longitude
                    )

                    marker = mMap.addMarker(
                        MarkerOptions().icon(
                            BitmapDescriptorFactory.fromBitmap(bitmapScaledDescriptorFromVector())
                        ).position(currentLoc).title("hi")
                    )

                    exactLoc = currentLoc.toString()

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 14f))
                    startIntentService(currentLoc)
                    location?.stopUpdateLocation()


                }
            })

        val nameFieldTxt = nameField.editText
        val flatFieldTxt  = flatField.editText
        val addrFieldTxt = addField.editText

        setTextChangeListener(nameFieldTxt!!)
        setTextChangeListener(flatFieldTxt!!)
        setTextChangeListener(addrFieldTxt!!)

        save_address.setOnClickListener {
            var alertDialogConfirm = AlertDialog.Builder(this)
            alertDialogConfirm.setTitle("Confirmation")
            alertDialogConfirm.setMessage("Are You want to save your address ?")
            alertDialogConfirm.setPositiveButton("ok") { dialog, which ->
                DbUtils.insertRowInDbAddress(nameFieldTxt.text.toString(),
                    flatFieldTxt.text.toString() + "\n" + addrFieldTxt.text.toString(), exactLoc)

//                val intent = Intent(this, ProceedToPayment::class.java)
//                startActivity(intent)
                dialog.dismiss()
                finish()
            }
            alertDialogConfirm.show()

        }

    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        // check if location is enabled
        enableMyLocation()
        mMap.uiSettings.isMyLocationButtonEnabled = false

        mMap.setOnCameraMoveStartedListener {
            gps_pinpoint.visibility = View.VISIBLE
            mMap.clear() }

        mMap.setOnCameraIdleListener {
            // fake marker will gone and real marker put into center of map and fetch the longitude
            // and latitude of this place
            val midLatLng : LatLng = mMap.cameraPosition.target
            if (marker != null) {
                marker?.position = midLatLng
                mMap.addMarker(
                    MarkerOptions().icon(
                        BitmapDescriptorFactory
                            .fromBitmap(bitmapScaledDescriptorFromVector())
                    )
                        .position(marker!!.position).title("hi")
                )

                Toast.makeText(this, "" + midLatLng, Toast.LENGTH_SHORT).show()
//                val d = GradientDrawable()
//                d.setShape(GradientDrawable.OVAL)
//                d.setSize(500, 500)
//                d.setColor(0x5500ff00)
//                d.setStroke(0, Color.TRANSPARENT)
//
//                val bitmap = Bitmap.createBitmap(
//                    d.intrinsicWidth, d.intrinsicHeight, Bitmap.Config.ARGB_8888
//                )
//                groundOverlay = mMap.addGroundOverlay(
//                    GroundOverlayOptions().position(
//                        midLatLng,
//                        (2 * radius).toFloat()
//                    ).image(BitmapDescriptorFactory.fromBitmap(bitmap))
//                )
//
//                valueAnimator!!.addUpdateListener { valueAnimator ->
//                    val animatedFraction = valueAnimator.animatedFraction
//                    groundOverlay.setDimensions(animatedFraction * radius * 2)
//                }
//
//                valueAnimator!!.start()
                startIntentService(midLatLng)

            }
            gps_pinpoint.visibility = View.INVISIBLE
        }
    }

    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            mMap.isMyLocationEnabled = true
            location = Location(
                this,
                object :
                    locationListener {
                    override fun locationResponse(locationResult: LocationResult) {
                        mMap.clear()
                        val currentLoc = LatLng(
                            locationResult.lastLocation.latitude,
                            locationResult.lastLocation.longitude
                        )

                        marker = mMap.addMarker(
                            MarkerOptions().icon(
                                BitmapDescriptorFactory.fromBitmap(bitmapScaledDescriptorFromVector())
                            ).position(currentLoc).title("hi")
                        )

                        exactLoc = currentLoc.toString()

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 14f))
                        startIntentService(currentLoc)
                        location?.stopUpdateLocation()


                    }
                })

        }
        else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }

    private fun bitmapScaledDescriptorFromVector(): Bitmap? {
        val height = 140
        val width = 80
        val imageBitmap = BitmapFactory.decodeResource(
            resources, resources.getIdentifier(
                "custommarker",
                "drawable",
                packageName
            )
        )
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }

    // this method start address receiver intent, passing longitude and latitude object
    private fun startIntentService(vlatlng: LatLng) {
        val intent = Intent(this, FetchAddressIntentService::class.java).apply {
            putExtra(ConstantMap.RECEIVER, mResultReceiver)
            putExtra(ConstantMap.LOCATION_DATA_EXTRA, vlatlng)
        }

        startService(intent)
    }

    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    @SuppressLint("RestrictedApi")
    inner class AddressResultReceiver internal constructor(handler: Handler) : ResultReceiver(
        handler
    ) {
        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            address_places.text = resultData.getString(ConstantMap.RESULT_DATA_KEY)
            addField.editText!!.setText(resultData.getString(ConstantMap.RESULT_DATA_KEY))
        }
    }

    private fun setTextChangeListener(idTxt : EditText){
        idTxt.doOnTextChanged(){ a, b, c, d ->
            if (a.toString().isNotEmpty() && flatField.editText?.length()!! > 0 && addField.editText
                    ?.length()!! > 0
            ){
                save_address.setBackgroundColor(Color.parseColor("#CDDC39"))
                save_add_txt.setTextColor(Color.parseColor("#000000"))
            }else{
                save_address.setBackgroundColor(Color.parseColor("#7F807F"))
                save_add_txt.setTextColor(Color.parseColor("#000000"))
            }
        }

    }

}