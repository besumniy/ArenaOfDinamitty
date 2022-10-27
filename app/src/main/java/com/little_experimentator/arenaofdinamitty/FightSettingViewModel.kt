package com.little_experimentator.arenaofdinamitty

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Resources
import android.media.AudioManager
import android.media.SoundPool
import android.os.Environment
import android.os.IBinder
import android.preference.PreferenceManager
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.little_experimentator.arenaofdinamitty.adapters.WarriorIconAdapter
import com.little_experimentator.arenaofdinamitty.services.AudioService
import com.little_experimentator.arenaofdinamitty.services.WebService
//import com.little_experimentator.arenaofdinamitty.services.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File

class FightSettingViewModel:ViewModel() {
    val choosenWarriorLive= MutableLiveData<String>()
    val adapterLive=MutableLiveData<RecyclerView.Adapter<WarriorIconAdapter.ViewHolder>>()
    val serverIpLive=MutableLiveData<String>()
    val buttonTextLive=MutableLiveData<String>()
    val clickableLive=MutableLiveData<Boolean>()

    val SEARCHING_TEXT="searching... (click to cancel)"
    val SETTINGS_TEXT="FIND VICTIM"

    var name=""
    lateinit var warriors:Array<File>

    //services
    lateinit var WebServiceConnection:ServiceConnection
    lateinit var webService: WebService

    //lateinit var AudioServiceConnection:ServiceConnection
    lateinit var audioService: AudioService
    var audioServiceIsInit=false
    lateinit var soundpool: SoundPool

    /*var AudioServiceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val log =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + "/sources/log lifecicle.txt")
            log.appendText("Service try init")
            var myBinder: AudioService.AudioServiceBinder =
                service as AudioService.AudioServiceBinder
            audioService = myBinder.getService()
            audioServiceIsInit=true
            log.appendText("Service")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            TODO("Not yet implemented")
        }
    }*/

    fun init(context: Context){
        clickableLive.value=true
        buttonTextLive.value="FIND VICTIM"
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        serverIpLive.value=pref.getString("ip", context.getString(R.string.ip))
        soundpool= SoundPool(3, AudioManager.STREAM_MUSIC,0)
        warriors=File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"//sources//images//minions").listFiles()
        name=warriors.get(0).name
        choosenWarriorLive.value=warriors.get(0).path+"/head.png"
        //initiateAdapter(context)



        WebServiceConnection = object:ServiceConnection{
            override fun onServiceConnected(name: ComponentName, service: IBinder){
                var myBinder:WebService.WebServiceBinder=service as WebService.WebServiceBinder
                webService=myBinder.getService()
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                TODO("Not yet implemented")
            }
        }

        //start service
        /*var intent1 = Intent(context, AudioService::class.java)
        context.startService(intent1)
        context.bindService(intent1,AudioServiceConnection, Context.BIND_AUTO_CREATE)*/

        //while(!audioServiceIsInit){}
        initiateSound(warriors)
        initiateAdapter(context)

        var intent = Intent(context, WebService::class.java)
        context.startService(intent)
        context.bindService(intent,WebServiceConnection, Context.BIND_AUTO_CREATE)

    }

    fun initiateAdapter(context: Context){

        adapterLive.value=WarriorIconAdapter(context,
            //audioService,
            soundpool,
            warriors,
            ::onClick
        )

    }
    fun initiateSound(warriors:Array<File>){
        for(warrior in warriors){
            var soundId=(soundpool.load(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"//sources//sounds//minions//"+warrior.name+"//congratulations.mp3",1))
        }
    }
    fun onClick(name:String,path:String/*,soundId:Int*/):Unit{
        if(clickableLive.value!!){
            changeChoosenWarrior(name,path)
            //audioService.playSound(soundId)
        }
    }

    fun changeChoosenWarrior(name:String,path:String){
        this.name=name
        choosenWarriorLive.value=path+"/head.png"
        //later need update this logic

    }

    fun findFight(context:Context,ip:String){
        if(clickableLive.value==false){
            buttonTextLive.value=SETTINGS_TEXT
            clickableLive.value=true
            var job= GlobalScope.launch(Dispatchers.IO) {
                //send cancel
                var message= JSONObject()
                message.put("c","cancel")

                //webService.makeRequestShort(message.toString())
                webService.sendMessage(message.toString())
            }
        }
        else{
        buttonTextLive.value=SEARCHING_TEXT
        clickableLive.value=false
        //replace to service
        var job= GlobalScope.launch(Dispatchers.IO) {//later create activity scope?
            val pref = PreferenceManager.getDefaultSharedPreferences(context)

            val editor = pref.edit()

            if(serverIpLive.value!=ip){webService.reconnect(serverIpLive.value!!)

            //later make only if good connection

            editor.putString("ip", ip)
            editor.apply()}




            /*var socket = Socket(ip, 8081)//make variable for port
            var dout = DataOutputStream(socket.getOutputStream())
            var inputStream = socket.getInputStream()*/

            var height= Resources.getSystem().displayMetrics.heightPixels.toInt()//??? maiby after onCreate
            var width= Resources.getSystem().displayMetrics.widthPixels.toInt()

            var message= JSONObject()
            message.put("c","fight")

            async{webService.makeRequestShort(message.toString())}.await()


            //get 'wait' from serfer for checking connectin
            //val check=ByteArray(4)
            //inputStream.read(check,0,4)
            //Toast.makeText(this,check.decodeToString() ,Toast.LENGTH_SHORT).show()//WITH UI

            //send info about ur warrior
            var send= JSONObject()
            send.put("n",name)
            //send.put("h",height)
            //send.put("w",width)
            var getEnemy=async{webService.makeRequestShort(send.toString())}
            var enemy = getEnemy.await()//webService.makeRequestShort(send.toString())

            if(enemy=="cancel") return@launch
            //get info about enemies warrior
            //later update
            //need to add timer and if-else
            //add getting id of fight
            /*val digit=ByteArray(4)
            inputStream.read(digit, 0,4)
            var l= BigInteger(digit).toInt()//+2
            val en=ByteArray(l)//4?
            inputStream.read(en, 0,l)
            var enemy=en.decodeToString()*/

            //i need check need i do in this way or i can use data from this viewmodel in other activities
            editor.putString("enemy", enemy)//
            editor.putString("ownWarrior",name)//
            editor.apply()

            //if time out:
            /*
            clickableLive.value=True
            buttonTextLive.value="FIND VICTIMS"
            * */


            //oh now i understand i need do more hard thing
            //i need create manager for this shit .. ?
            //but now i continue write this shit

            context.startActivity(Intent(context,FightLoadActivity::class.java))
            //comment cuz i not shure that this clear
            //or no commemt
        }}
    }


}

