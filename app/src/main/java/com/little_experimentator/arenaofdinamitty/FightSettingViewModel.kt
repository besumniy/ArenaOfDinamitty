package com.little_experimentator.arenaofdinamitty

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Environment
import android.preference.PreferenceManager
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.little_experimentator.arenaofdinamitty.adapters.WarriorIconAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.File
import java.math.BigInteger
import java.net.Socket

class FightSettingViewModel:ViewModel() {
    val choosenWarriorLive= MutableLiveData<String>()
    val adapterLive=MutableLiveData<RecyclerView.Adapter<WarriorIconAdapter.ViewHolder>>()
    val buttonTextLive=MutableLiveData<String>()
    val clickableLive=MutableLiveData<Boolean>()

    var name=""
    lateinit var warriors:Array<File>

    fun init(context: Context){
        clickableLive.value=true
        buttonTextLive.value="FIND VICTIM"
        warriors=File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"//sources//images//minions").listFiles()
        choosenWarriorLive.value=warriors.get(0).path+"/head.png"
        initiateAdapter(context)
    }

    fun initiateAdapter(context: Context){
        adapterLive.value=WarriorIconAdapter(context,
            warriors,
            ::onClick
        )

    }
    fun onClick(name:String,path:String):Unit{
        if(clickableLive.value!!)changeChoosenWarrior(name,path)
    }

    fun changeChoosenWarrior(name:String,path:String){
        this.name=name
        choosenWarriorLive.value=path+"/head.png"
        //later need update this logic

    }

    fun findFight(context:Context,ip:String){
        buttonTextLive.value="searching"
        clickableLive.value=false
        var job= GlobalScope.launch(Dispatchers.IO) {//later create activity scope?
            var socket = Socket(ip, 8081)//make variable for port
            var dout = DataOutputStream(socket.getOutputStream())
            var inputStream = socket.getInputStream()

            var height= Resources.getSystem().displayMetrics.heightPixels.toInt()//??? maiby after onCreate
            var width= Resources.getSystem().displayMetrics.widthPixels.toInt()

            var message= JSONObject()
            message.put("c","fight")
            dout.writeUTF(message.toString())
            dout.flush()

            //get 'wait' from serfer for checking connectin
            val check=ByteArray(4)
            inputStream.read(check,0,4)
            //Toast.makeText(this,check.decodeToString() ,Toast.LENGTH_SHORT).show()//WITH UI

            //send info about ur warrior
            var send= JSONObject()
            send.put("n",name)
            send.put("h",height)
            send.put("w",width)
            dout.writeUTF(send.toString())
            dout.flush()


            //get info about enemies warrior
            //later update
            //need to add timer and if-else
            //add getting id of fight
            val digit=ByteArray(4)
            inputStream.read(digit, 0,4)
            var l= BigInteger(digit).toInt()//+2
            val en=ByteArray(l)//4?
            inputStream.read(en, 0,l)
            var enemy=en.decodeToString()

            //i need check need i do in this way or i can use data from this viewmodel in other activities
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = pref.edit()
            editor.putString("ip", ip)
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
        }
    }
}