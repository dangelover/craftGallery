package com.incanoit.craftgalleryapp.utils

import android.util.Log
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import java.net.URISyntaxException

object SocketHandler {
    lateinit var mSocket: Socket
    @Synchronized
    fun setSocket(){
        try {
            mSocket=IO.socket("http://192.168.87.2:3000/orders/delivery")
            Log.d("Error","${mSocket}")


        }catch (e:URISyntaxException){
            Log.d("Error","No se pudo conectar el socket ${e.message}")
        }
    }
    @Synchronized
    fun getSocket(): Socket{
        return mSocket
    }
    @Synchronized
    fun establishConnection() {
        mSocket.connect()
    }
    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }
}