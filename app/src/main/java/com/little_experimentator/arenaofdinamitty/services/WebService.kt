package com.little_experimentator.arenaofdinamitty.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.preference.PreferenceManager
import androidx.annotation.RequiresApi
import com.little_experimentator.arenaofdinamitty.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
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
        try{socket = Socket(ip, 8081)
        dout = DataOutputStream(socket.getOutputStream())
        inputStream = socket.getInputStream()}
        catch(e:Exception){}
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        //close socket
    }

    @RequiresApi(Build.VERSION_CODES.N)
    /*suspend*/ fun makeRequest(request:String):JSONObject{
        /*val log =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + "/sources/long_message_story.txt")
        if(!log.exists())log.createNewFile()
        log.appendText(request)*/
        dout.writeUTF(request)
        dout.flush()

        val digit=ByteArray(4)
        inputStream.read(digit,0,4)
        var l= BigInteger(digit).toInt()
        var buf = ByteArray(1024)//4?
        var num_read=0
        var get_js=ByteArray(0)
        while (l > 0) {
            num_read = inputStream.read(buf, 0, Integer.min(buf.size, l))
            if (num_read == -1 ) {  // end of stream
                break;
            }
            get_js = get_js + buf.copyOfRange(0, num_read)
            l -= num_read
        }
        return JSONObject(get_js.decodeToString())
    }

    suspend fun makeRequestShort(request:String):String{
        /*val log =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + "/sources/message_story.txt")
        if(!log.exists())log.createNewFile()
        log.appendText(request)*/
        dout.writeUTF(request)
        dout.flush()
        //log.appendText(request+" sended")
        var digit = ByteArray(4)
        inputStream.read(digit, 0, 4)
        //log.appendText(digit.toString()+" size")
        var l = BigInteger(digit).toInt()
        var answer_b = ByteArray(l);
        inputStream.read(answer_b, 0, l)
        //log.appendText(answer_b.decodeToString()+" get")
        return answer_b.decodeToString()
    }

    suspend fun sendMessage(request:String){
        dout.writeUTF(request)
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