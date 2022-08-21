package com.little_experimentator.arenaofdinamitty

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.preference.PreferenceManager
import androidx.annotation.RequiresApi
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
    var bindNoReady=true

    var WebServiceConnection = object:ServiceConnection{
        override fun onServiceConnected(name: ComponentName, service: IBinder){
            var myBinder:WebService.WebServiceBinder=service as WebService.WebServiceBinder
            webService=myBinder.getService()
            bindNoReady=false
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            TODO("Not yet implemented")
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun load(context: Context) {
        val log =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + "/sources/log.txt")
        log.createNewFile()
        log.writeText("ok/n")
        log.writeText(bindNoReady.toString())
        var job = GlobalScope.launch(Dispatchers.IO) {//later create activity scope?

            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            var enemy = pref.getString("enemy", "")

            //connect service
            var intent = Intent(context, WebService::class.java)
            context.bindService(intent, WebServiceConnection, Context.BIND_AUTO_CREATE)

            /*var socket = Socket(ip, 8081)//make variable for port
            var dout = DataOutputStream(socket.getOutputStream())
            var inputStream = socket.getInputStream()*/

            log.writeText(bindNoReady.toString())
            while(bindNoReady){}
            log.writeText(bindNoReady.toString())

            //need send id of fight
            val file =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + "/sources/images/minions/" + enemy)//?

            log.writeText(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + "/sources/images/minions/$enemy\n")
            if (file.exists()) {
                var send= JSONObject()
                send.put("c","ready")
                webService.sendMessage(send.toString())
                log.appendText("is exist\n");context.startActivity(
                    Intent(
                        context,
                        FightActivity::class.java
                    )
                )
            } else {
                log.appendText("need create\n");
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
                    //val log= File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"/sources/log.txt")
                    log.writeText("$name\n")
                    var downloading_file =
                        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + "/" + name)

                    if (name.contains(".")) {


                        if (downloading_file.exists()) break//maybe modificate
                        //else
                        downloading_file.createNewFile()

                        //in future make request

                        /*var digit = ByteArray(4)
                        inputStream.read(digit, 0, 4)
                        var l = BigInteger(digit).toInt()
                        val buf = ByteArray(1024)*/

                        var l = webService.getSize()

                        progressStringLive.value = name
                        progressMaxLive.value = l
                        progressLive.value = l

                        downloading_file.appendBytes(webService.downloadFile(l))

                        var num_read = 0
                        /*while (l > 0) {

                            num_read = inputStream.read(buf, 0, Math.min(buf.size, l))
                            if (num_read == -1) {  // end of stream
                                break;
                            }
                            downloading_file.appendBytes(buf)
                            l -= num_read
                        }*/
                    } else {
                        downloading_file.mkdir()
                    }

                    //get name for next downloading file
                    /*digit = ByteArray(4)
                    inputStream.read(digit, 0, 4)
                    l = BigInteger(digit).toInt()
                    var name_b = ByteArray(l)
                    inputStream.read(name_b,0,l)*/
                    name = webService.getMessage()//name_b.decodeToString()
                    log.writeText("$name\n")
                }

                message = JSONObject()
                message.put("c", "ready")
                val log =
                    File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + "/sources/log.txt")
                //log.appendText("sended\n")
                //dout.writeUTF(message.toString())
                //dout.flush()
                webService.sendMessage(message.toString())
                //log.appendText(""+scale)
                log.appendText("sended\n")
                context.startActivity(Intent(context, FightActivity::class.java))
            }
        }
    }

    /*
    var WebServiceConnection=ServiceConnection(){
        override fun onServiceConnected(name:ComponentName,service:IBinder){
            var myBinder:WebService.WebServiceBinder=service
            webService=myBinder.getService()
        }
    }
     */

    
}



