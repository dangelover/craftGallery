package com.incanoit.craftgalleryapp.activities.client.orders.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.github.nkzawa.socketio.client.Socket
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.activities.delivery.home.DeliveryHomeActivity
import com.incanoit.craftgalleryapp.models.Order
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.SocketEmit
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.providers.OrdersProvider
import com.incanoit.craftgalleryapp.utils.SharedPref
import com.incanoit.craftgalleryapp.utils.SocketHandler
import com.maps.route.extensions.drawRouteOnMap
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class ClientOrdersMapActivity : AppCompatActivity(), OnMapReadyCallback {
    var googleMap: GoogleMap?=null
    var TAG = "DeliveryOrdersMap"
    val PERMISSION_ID = 42
    var fusedLocationClient: FusedLocationProviderClient?=null
    var city = ""
    var country = ""
    var address=""
    var addresLatLng: LatLng?=null
    var markerDelivery: Marker?=null
    var marketAddress:Marker?=null
    var myLocationLanLng: LatLng?=null
    var deliveryLatLng:LatLng?=null
    var order:Order?=null
    var gson= Gson()
    var textViewClient: TextView?=null
    var textViewAddress: TextView?=null
    var textViewNeighbordhood: TextView?=null
    var circleImageUser: CircleImageView?=null
    var imageViewPhone: ImageView?=null
    val REQUEST_PHONE_CALL = 30
    var ordersProvider: OrdersProvider?=null
    var user:User?=null
    var sharedPref:SharedPref?=null
    var distanceBetween = 0.0f
    var socket: Socket?=null

    private val locationCallback= object: LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation = locationResult.lastLocation
            //este metodo va a ir obteniendo constantemente y obteniendo la ubicacion
            myLocationLanLng = LatLng(lastLocation.latitude,lastLocation.longitude)

//            googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(
//                CameraPosition.builder().target(
//                    LatLng(myLocationLanLng?.latitude!!,myLocationLanLng?.longitude!!)
//                ).zoom(15f).build()
//            ))
            //primero eliminamos el market para luego dibujarlo
            Log.d(TAG,"Distancia: $distanceBetween")
//            removeDeliveryMarker()
            //cada vez que se obtneien la ubicacion entonces redibujamos el mapa del delivery
//            addDeliveryMarker()
            Log.d("LOCALIZACION","Callback: $lastLocation")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_orders_map)
        sharedPref= SharedPref(this)
        getUsersFromSession()
        order=gson.fromJson(intent.getStringExtra("order"),Order::class.java)
        if (order?.lat !=null && order?.lng!=null){
            deliveryLatLng= LatLng(order?.lat!!,order?.lng!!)
        }
        ordersProvider= OrdersProvider(user?.sessionToken!!)
        addresLatLng= LatLng(order?.address?.lat!!,order?.address?.lng!!)
        val mapFragment= supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        //inicializamos esta variable que va a inicializar el servicio para encontrar nuestra localizacion
        fusedLocationClient= LocationServices.getFusedLocationProviderClient(this)
        textViewClient= findViewById(R.id.textview_repartidor)
        textViewAddress= findViewById(R.id.textview_address)
        textViewNeighbordhood= findViewById(R.id.textview_neighborhood)
        circleImageUser=findViewById(R.id.circleimage_user)
        imageViewPhone=findViewById(R.id.imageview_phone)
        getLastLocation()
        connectSocket()
        textViewClient?.text="${order?.delivery?.name} ${order?.delivery?.lastname}"
        textViewAddress?.text=order?.address?.address
        textViewNeighbordhood?.text=order?.address?.neighborhood
        if (!order?.delivery?.image.isNullOrBlank()){
            Glide.with(this).load(order?.delivery?.image).into(circleImageUser!!)
        }
        imageViewPhone?.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE),REQUEST_PHONE_CALL)
            }else{
                call()
            }
        }
    }
    private fun connectSocket(){
        SocketHandler.setSocket()
        socket= SocketHandler.getSocket()
        socket?.connect()
        socket?.on("position/${order?.id}"){arg->
            if (arg[0]!=null){
                runOnUiThread{
                    val data =gson.fromJson(arg[0].toString(),SocketEmit::class.java)
                    removeDeliveryMarker()
                    addDeliveryMarker(data.lat,data.lng)
                }
            }
        }
    }
    private fun call(){
        val i = Intent(Intent.ACTION_CALL)
        i.data= Uri.parse("tel: ${order?.delivery?.phone}")
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permiso denegado para realizar la llamada", Toast.LENGTH_SHORT).show()
        }
        startActivity(i)
    }
    private fun drawRoute(){
        if (deliveryLatLng !=null){
            val addressLocation = LatLng(order?.address?.lat!!,order?.address?.lng!!)
//            val deliveryLocation = LatLng(order?.lat!!,order?.lng!!)
            Log.d(TAG,"Camino $addressLocation")
            googleMap?.drawRouteOnMap(
                getString(R.string.google_map_api_key),
                source = deliveryLatLng!!,
                destination = addressLocation,
                context = this ,
                color = Color.BLACK,
                polygonWidth = 12,
                boundMarkers = false,
                markers = false
            )
        }
    }
    private fun removeDeliveryMarker(){
        markerDelivery?.remove()
    }
    private fun addDeliveryMarker(lat:Double, lng:Double){
        val location = LatLng(lat,lng)
//        val deliveryLocation = LatLng(order?.lat!!,order?.lng!!)
        markerDelivery= googleMap?.addMarker(
            MarkerOptions().position(location).title("Posicion del repartidor").icon(BitmapDescriptorFactory.fromResource(R.drawable.deliver))
        )
    }
    private fun addAddressMarker(){
        val addressLocation = LatLng(order?.address?.lat!!,order?.address?.lng!!)
        Log.d(TAG,"Casita $addressLocation")
        marketAddress= googleMap?.addMarker(
            MarkerOptions().position(addressLocation).title("Entregar aqui").icon(BitmapDescriptorFactory.fromResource(R.drawable.poss))
        )
    }
    override fun onMapReady(map: GoogleMap) {
        googleMap=map
        googleMap?.uiSettings?.isZoomControlsEnabled=true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (locationCallback!=null && fusedLocationClient !=null){
            fusedLocationClient?.removeLocationUpdates(locationCallback)
        }
        socket?.disconnect()
    }
    private fun goToHome(){
        val i = Intent(this,DeliveryHomeActivity::class.java)
        i.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }
    private fun getLastLocation(){
        if (checkPermission()){
            if (isLocationEnable()){
                fusedLocationClient?.lastLocation?.addOnCompleteListener {task->
                    var location = task.result
                    if(location !=null){
                        //este metodo solo se ejecuta una vez, es decir obtiene la localizacion una sola vez
                        myLocationLanLng= LatLng(location.latitude,location.longitude)
                        removeDeliveryMarker()
                        addDeliveryMarker(deliveryLatLng?.latitude!!,deliveryLatLng?.longitude!!)
                        addAddressMarker()
                        drawRoute()
                        if (deliveryLatLng != null){
                            googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(
                                CameraPosition.builder().target(
                                    LatLng(deliveryLatLng?.latitude!!,deliveryLatLng?.longitude!!)
                                ).zoom(15f).build()
                            ))

                        }
                    }

                }
            }else{
                Toast.makeText(this, "Habilita la localizacion", Toast.LENGTH_LONG).show()
                val i = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(i)
            }

        }else{
            requestPermissions()
        }
    }
    private fun requestNewLocationData(){
        val locationRequest = LocationRequest.create().apply {
            interval = 100
            fastestInterval = 50
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationClient?.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper()) //inicializa la posicion en tiempo real
    }
    private fun isLocationEnable(): Boolean{
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermission(): Boolean{
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            return  true
        }
        return false
    }
    private fun requestPermissions(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),PERMISSION_ID)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //si los permisos fueron concedidos de manera exitosa
        if (requestCode==PERMISSION_ID){
            if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLastLocation()
            }
        }
        if (requestCode==REQUEST_PHONE_CALL){
            call()
        }
    }
    private fun getUsersFromSession(){

        val gson = Gson()
        //usamos el SharedPref y usando el metodo getData vamos a obtener informacion del usuario
        //le pasamos el key o el nombre con el que guardamos la informacion
        if (!sharedPref?.getData("user").isNullOrEmpty()){
            //SI EL USUARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
            Log.d("ClientUpdateActivity","${user}")

        }
    }
}