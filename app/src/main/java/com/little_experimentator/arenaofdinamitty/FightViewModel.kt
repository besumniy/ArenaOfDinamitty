package com.little_experimentator.arenaofdinamitty

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.preference.PreferenceManager
import android.view.MotionEvent
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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

    @RequiresApi(Build.VERSION_CODES.N)
    fun fight(context: Context){
        var job= GlobalScope.launch(Dispatchers.IO) {//later create activity scope?
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            var ip=pref.getString("ip", "")
            var enemy=pref.getString("ip", "")

            var socket = Socket(ip, 8080)//make variable for port
            var dout = DataOutputStream(socket.getOutputStream())
            var inputStream = socket.getInputStream()

            var height= Resources.getSystem().displayMetrics.heightPixels.toInt()//??? maiby after onCreate
            var width= Resources.getSystem().displayMetrics.widthPixels.toInt()//
            //width=Resources.getSystem().displayMetrics.xdpi.toInt()
            //game_area_width=(height*1.5).toInt()
            //side_width=((width-game_area_width)/3.0).toInt()

            var fight=true
            //socketU.broadcast=true//need we at if connect only to one client?
            while(fight) {
                //get word
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
                //try work with word
                //try{
                var get = JSONObject(get_js.decodeToString())
                getLive.value=get
                var world = get.getJSONArray("w")
                worldLive.value=world
                var face=get.getBoolean("f")
                faceLive.value=face
                //game_screen.isInitilized=true
                var x=0
                var y=0
                if(face){
                    x=get.getInt("x")//*scale-width/20.0
                    y=get.getInt("y")//*scale-height/20.0
                }else{
                    x=get.getInt("x")//*scale+width/20.0*19
                    y=get.getInt("y")//*scale-height/20.0
                }
                xLive.value=x
                yLive.value=y

                //maibe it next try or launch
                getLive.value=get
                worldLive.value=world
                faceLive.value=face
                isInitilizedLive.value=true
                //try{game_screen.invalidate()}catch(e:Exception){}

                // }
                //catch (e:Exception){log.appendText("failed\n")}

                //send touches
                //maybe other launch
                touches.put("d",touch_down)
                touches.put("u",touch_up)
                touches.put("t",touch)

                //dout.writeUTF(activity.touches.toString())//? realy to string?
                //dout.flush()
                //sendU= DatagramPacket(touches.toString().toByteArray(),touches.toString().toByteArray().size, InetAddress.getByName(adress),new_port)
                dout.writeUTF(touches.toString())//
                dout.flush()
                touch_down= JSONArray()
                touch_up= JSONArray()
            }
            context.startActivity(Intent(context, WarriorListActivity::class.java))
            //
        }
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