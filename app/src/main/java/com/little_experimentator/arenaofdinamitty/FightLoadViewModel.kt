package com.little_experimentator.arenaofdinamitty

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Environment
import android.os.IBinder
import android.preference.PreferenceManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.little_experimentator.arenaofdinamitty.services.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.File
import java.math.BigInteger
import java.net.Socket

class FightLoadViewModel: ViewModel() {
    val progressLive= MutableLiveData<Int>()
    var progressMaxLive= MutableLiveData<Int>()
    val progressStringLive= MutableLiveData<String>()

    lateinit var webService:WebService

    fun load( context: Context){
        var job= GlobalScope.launch(Dispatchers.IO) {//later create activity scope?
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            var enemy=pref.getString("enemy", "")

            //start service
            var intent = Intent(context, WebService::class.java)
            //context.startService(intent)
            context.bindService(intent,WebServiceConnection, Context.BIND_AUTO_CREATE)

            /*var socket = Socket(ip, 8081)//make variable for port
            var dout = DataOutputStream(socket.getOutputStream())
            var inputStream = socket.getInputStream()*/

            //need send id of fight
            val file= File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"/sources/images/minions/"+enemy)//?
            val log=File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"/sources/log.txt")
            log.createNewFile()
            log.writeText(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"/sources/images/minions/$enemy\n")
            if (file.exists()) {log.appendText("is exist\n");context.startActivity(Intent(context,FightActivityOld::class.java))}
            else {log.appendText("need create\n");
                file.mkdir()
                //Toast.makeText(this, "so what the problem?", Toast.LENGTH_SHORT).show()

                //send name of warrior for download sources for it
                var message = JSONObject()
                message.put("c", enemy)
                var name = webService.makeRequestShort(message.toString())
                //dout.writeUTF(message.toString())
                //dout.flush()

                //get sources
                /*var digit = ByteArray(4)
                inputStream.read(digit, 0, 4)
                var l = BigInteger(digit).toInt()
                var name = "";
                var name_b = ByteArray(l);
                inputStream.read(name_b, 0, l)
                name = name_b.decodeToString()*/
                //dout.writeUTF("ok")
                //dout.flush()
                while (name != "finshed") {
                    val log= File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"/sources/log.txt")
                    log.writeText("$name\n")
                    var downloading_file =
                        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + "/" + name)

                    if(name.contains("."))


                        if (downloading_file.exists()) break//maybe modificate
                        //else
                        downloading_file.createNewFile()

                    //in future make request

                        var digit = ByteArray(4)
                        inputStream.read(digit, 0, 4)
                        var l = BigInteger(digit).toInt()
                        val buf = ByteArray(1024)

                        progressStringLive.value=name
                        progressMaxLive.value=l
                        progressLive.value=l

                        var num_read = 0
                        while (l > 0) {
                            num_read = inputStream.read(buf, 0, Math.min(buf.size, l))
                            if (num_read == -1) {  // end of stream
                                break;
                            }
                            downloading_file.appendBytes(buf)
                            l -= num_read
                        }
                    }
                    else{
                        downloading_file.mkdir()
                    }

                    //get name for next downloading file
                    digit = ByteArray(4)
                    inputStream.read(digit, 0, 4)
                    l = BigInteger(digit).toInt()
                    var name_b = ByteArray(l)
                    inputStream.read(name_b,0,l)
                    name=name_b.decodeToString()
                    log.writeText("$name\n")
                }

                message=JSONObject()
                message.put("c","ready")
                val log=File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"/sources/log.txt")
                //log.appendText("sended\n")
                //dout.writeUTF(message.toString())
                //dout.flush()
                webSocket.makeRequest(message)
                //log.appendText(""+scale)
                log.appendText("sended\n")
                context.startActivity(Intent(context,FightActivityOld::class.java))}
    }

    var WebServiceConnection=ServiceConnection(){
        override fun onServiceConnected(name:ComponentName,service:IBinder){
            var myBinder:WebService.WebServiceBinder=service
            webService=myBinder.getService()
        }
    }

}



