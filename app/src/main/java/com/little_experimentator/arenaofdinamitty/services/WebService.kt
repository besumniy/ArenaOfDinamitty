package com.little_experimentator.arenaofdinamitty.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.preference.PreferenceManager
import com.little_experimentator.arenaofdinamitty.R
import org.json.JSONArray
import java.io.DataOutputStream
import java.io.InputStream
import java.math.BigInteger
import java.net.Socket

class WebService : Service() {
    var binder= WebServiceBinder()

    lateinit var socket: Socket
    lateinit var dout : DataOutputStream
    lateinit var inputStream : InputStream

    override fun onBind(intent: Intent): IBinder {
        return this.binder
    }


    override fun onCreate(){
        //make connection
        val pref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        var ip=pref.getString("ip", applicationContext.getString(R.string.ip))
        socket = Socket(ip, 8081)
        var dout = DataOutputStream(socket.getOutputStream())
        var inputStream = socket.getInputStream()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        //close socket
    }

    fun makeRequest(request:String):JSONArray{
        return JSONArray()
    }

    fun makeRequestShort(request:String):String{
        dout.writeUTF(request.toString())
        dout.flush()
        var digit = ByteArray(4)
        inputStream.read(digit, 0, 4)
        var l = BigInteger(digit).toInt()
        var answer_b = ByteArray(l);
        inputStream.read(answer_b, 0, l)
        return answer_b.decodeToString()
    }

    fun sendMessage(request:String){
        dout.writeUTF(request.toString())
        dout.flush()
    }

        class WebServiceBinder(): Binder() {
            fun getService():WebService{
                return WebService.this
            }
        }

}