package com.little_experimentator.arenaofdinamitty.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.preference.PreferenceManager
import com.little_experimentator.arenaofdinamitty.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.DataOutputStream
import java.io.File
import java.io.InputStream
import java.math.BigInteger
import java.net.Socket

class WebService : Service() {
    var binder= WebServiceBinder()

    lateinit var socket: Socket
    lateinit var dout : DataOutputStream
    lateinit var inputStream : InputStream




    override fun onCreate(){
        //make connection
        val pref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        var ip=pref.getString("ip", applicationContext.getString(R.string.ip))
        GlobalScope.launch(Dispatchers.IO) {reconnect(ip!!)}
    }

    suspend fun reconnect(ip:String){
        //val pref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        //var ip=pref.getString("ip", applicationContext.getString(R.string.ip))
        socket = Socket(ip, 8081)
        dout = DataOutputStream(socket.getOutputStream())
        inputStream = socket.getInputStream()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        //close socket
    }

    suspend fun makeRequest(request:String):JSONArray{
        return JSONArray()
    }

    suspend fun makeRequestShort(request:String):String{
        dout.writeUTF(request.toString())
        dout.flush()
        var digit = ByteArray(4)
        inputStream.read(digit, 0, 4)
        var l = BigInteger(digit).toInt()
        var answer_b = ByteArray(l);
        inputStream.read(answer_b, 0, l)
        return answer_b.decodeToString()
    }

    suspend fun sendMessage(request:String){
        dout.writeUTF(request.toString())
        dout.flush()
    }

    suspend fun getMessage():String{
        var digit = ByteArray(4)
        inputStream.read(digit, 0, 4)
        var l = BigInteger(digit).toInt()
        var answer_b = ByteArray(l)
        inputStream.read(answer_b,0,l)
        return answer_b.decodeToString()
    }

    suspend fun downloadFile(size:Int):ByteArray{
        //var downloadingArray=ByteArray(size)
        var downloadingArray= File("")
        var l=size
        val buf = ByteArray(1024)

        var num_read = 0
        while (l > 0) {

            num_read = inputStream.read(buf, 0, Math.min(buf.size, l))
            if (num_read == -1) {  // end of stream
                break;
            }
            downloadingArray.appendBytes(buf)//now fix
            //downloadingArray.set(size-l,buf)
            //downloadingArray=buf.joinTo(downloadingArray,"","","",-1,Null,None)
            l -= num_read
        }
        return downloadingArray.readBytes()
    }

    suspend fun getSize():Int{
        var digit = ByteArray(4)
        inputStream.read(digit, 0, 4)
        return BigInteger(digit).toInt()
    }

        inner class WebServiceBinder(): Binder() {
            fun getService():WebService=this@WebService
            }

    override fun onBind(intent: Intent): IBinder {
        return this.binder
    }

}