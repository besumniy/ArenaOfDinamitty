package com.little_experimentator.arenaofdinamitty

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Resources
import android.os.Build
import android.os.IBinder
import android.preference.PreferenceManager
import android.view.MotionEvent
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.little_experimentator.arenaofdinamitty.services.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.DataOutputStream
import java.math.BigInteger
import java.net.Socket

class FightViewModel: ViewModel() {
    //val sideWidthLive= MutableLiveData<Int>()
    //val iconSizeLive= MutableLiveData<Int>()
    val getLive= MutableLiveData<JSONObject>()
    val worldLive= MutableLiveData<JSONArray>()
    val faceLive= MutableLiveData<Boolean>()
    val xLive= MutableLiveData<Int>()
    val yLive= MutableLiveData<Int>()
    val isInitilizedLive= MutableLiveData<Boolean>()

    var touch_down: JSONArray = JSONArray()
    var touch_up: JSONArray = JSONArray()
    var touch_move: JSONArray = JSONArray()
    var touch: JSONArray = JSONArray()
    var touches= JSONObject()

    lateinit var webService:WebService
    //lateinit var get:JSONObject
    var serviseIsInitialized=false
    var fight=false
    var fightIsFinished=false

    var WebServiceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder){
            var myBinder:WebService.WebServiceBinder=service as WebService.WebServiceBinder
            webService=myBinder.getService()
            serviseIsInitialized=true

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            TODO("Not yet implemented")
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun fight(context: Context){
        GlobalScope.launch(Dispatchers.IO) {//later create activity scope?
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            var enemy=pref.getString("enemy", "")

            var height= Resources.getSystem().displayMetrics.heightPixels.toInt()//??? maiby after onCreate
            var width= Resources.getSystem().displayMetrics.widthPixels.toInt()//
            //width=Resources.getSystem().displayMetrics.xdpi.toInt()
            //game_area_width=(height*1.5).toInt()
            //side_width=((width-game_area_width)/3.0).toInt()

            //connect servise
            var intent = Intent(context, WebService::class.java)
            context.bindService(intent, WebServiceConnection, Context.BIND_AUTO_CREATE)

            fight=true
            while(fight) {
                //send touches
                //maybe other launch
                touches.put("d",touch_down)
                touches.put("u",touch_up)
                touches.put("t",touch)

                //dout.writeUTF(activity.touches.toString())//? realy to string?
                //dout.flush()
                //sendU= DatagramPacket(touches.toString().toByteArray(),touches.toString().toByteArray().size, InetAddress.getByName(adress),new_port)

                GlobalScope.launch(Dispatchers.Main){Toast.makeText(context, "ok: "+serviseIsInitialized.toString(), Toast.LENGTH_SHORT).show()}


                if(serviseIsInitialized) {
                    val getJob = async {webService.makeRequest(touches.toString()) }
                    val get = getJob.await()//JSONObject(get_js.decodeToString())

                    touch_down = JSONArray()
                    touch_up = JSONArray()

                    GlobalScope.launch(Dispatchers.Main){

                                getLive.value = get
                                var world = getLive.value!!.getJSONArray("w")
                                worldLive.value = world
                                var face = getLive.value!!.getBoolean("f")
                                faceLive.value = face
                                //game_screen.isInitilized=true
                                var x = 0
                                var y = 0
                                if (face) {
                                    x = getLive.value!!.getInt("x")//*scale-width/20.0
                                    y = getLive.value!!.getInt("y")//*scale-height/20.0
                                } else {
                                    x = getLive.value!!.getInt("x")//*scale+width/20.0*19
                                    y = getLive.value!!.getInt("y")//*scale-height/20.0
                                }
                                xLive.value = x
                                yLive.value = y

                                //maibe it next try or launch
                                //getLive.value = get
                                worldLive.value = world
                                faceLive.value = face
                                isInitilizedLive.value = true}
                        }

            }
            context.startActivity(Intent(context, WarriorListActivity::class.java))
            //
        }
        Toast.makeText(context, "acunamatata", Toast.LENGTH_SHORT).show()

        Toast.makeText(context, serviseIsInitialized.toString(), Toast.LENGTH_SHORT).show()

    }
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun onClick(event: MotionEvent?){
        var x= event?.getX(event.actionIndex)
        var y= Resources.getSystem().displayMetrics.heightPixels-event?.getY(event.actionIndex)!!
        var id=event.getPointerId(event.actionIndex)
        //var id=event?.getPointerId()
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                var action=JSONObject()
                action.put("x",x)
                action.put("y",y)
                action.put("id",id)
                touch_down.put(action)//
                touch.put(action)
            }
            MotionEvent.ACTION_UP->{
                var action=JSONObject()
                action.put("x",x)
                action.put("y",y)
                action.put("id",id)
                touch_up.put(action)//
                for(i in 0..touch.length()-1)if(touch.getJSONObject(i).get("id")==id ){touch.remove(i);    break}
            }
            MotionEvent.ACTION_MOVE->{
                for(i in 0..touch.length()-1)
                    if(touch.getJSONObject(i).get("id")==id )
                    {
                        touch.getJSONObject(i).put("x",x)
                        touch.getJSONObject(i).put("y",y)
                        break}
            }
        }
    }


}