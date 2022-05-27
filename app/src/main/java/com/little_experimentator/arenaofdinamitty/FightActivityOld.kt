package com.little_experimentator.arenaofdinamitty

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.Proxy.getPort
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.little_experimentator.arenaofdinamitty.usecases.Screen
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.File
import java.io.InputStream
import java.lang.Integer.min
import java.math.BigInteger
import java.net.*

class FightActivityOld : AppCompatActivity() {
    var adress ="192.168.1.109"
    val port = 8081
    //val scale= Resources.getSystem().displayMetrics.heightPixels/1000.0
    //val icon_size= Resources.getSystem().displayMetrics.heightPixels/8.0

    lateinit var pref: SharedPreferences
    var warrior = ""
    var enemy = ""

    lateinit var game_screen:fightView

    var touch_down: JSONArray = JSONArray()
    var touch_up: JSONArray = JSONArray()
    var touch_move: JSONArray = JSONArray()
    var touch: JSONArray = JSONArray()
    var touches= JSONObject()

    lateinit var socket:Socket//Socket(adress, port)
    //val socketU= DatagramSocket()
    lateinit var dout:DataOutputStream// = DataOutputStream(socket.getOutputStream())
    lateinit var sendU: DatagramPacket
    lateinit var inputStream: InputStream//? = null//socket.getInputStream()

    var height=0//Resources.getSystem().displayMetrics.heightPixels.toInt()//??? maiby after onCreate
    var width=0//Resources.getSystem().displayMetrics.widthPixels.toInt()//
    //var game_area_width=0//(height*1.5).toInt()
    //var side_width=0//((width-game_area_width)/3.0).toInt()

    //usecases
    val screen = Screen()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight)
        screen.makeFullScreenMode(this)

        game_screen=findViewById<fightView>(R.id.game_screen)
        //game_screen.activity=this
        game_screen.side_width=((game_screen.width-game_screen.height*1.5)/3.0).toInt()
        game_screen.icon_size=game_screen.side_width//check if icon_n*icon_size>height

        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())

        if (pref.contains("warrior")) {
            warrior = pref.getString("warrior", "")!!
        }
        if (pref.contains("adres")) {
            adress = pref.getString("adres", "")!!
        }

        var job= GlobalScope.launch(Dispatchers.IO) {//later create activity scope?
            socket = Socket(adress, port)
            dout = DataOutputStream(socket.getOutputStream())
            inputStream = socket.getInputStream()

            height=Resources.getSystem().displayMetrics.heightPixels.toInt()//??? maiby after onCreate
            width=Resources.getSystem().displayMetrics.widthPixels.toInt()//
            //width=Resources.getSystem().displayMetrics.xdpi.toInt()
            //game_area_width=(height*1.5).toInt()
            //side_width=((width-game_area_width)/3.0).toInt()

            speakingWithServer()
            //
        }
        /*GlobalScope.launch(Dispatchers.Main) {
            while(true){
                if(game_screen.isInitilized){
                    game_screen.invalidate()
                    game_screen.isInitilized=false
                }
            }
        }*/

    }


    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun speakingWithServer(){

        var message=JSONObject()
        message.put("c","fight")
        dout.writeUTF(message.toString())
        dout.flush()

        //get 'wait' from serfer for checking connectin
        val check=ByteArray(4)
        inputStream.read(check,0,4)
        //Toast.makeText(this,check.decodeToString() ,Toast.LENGTH_SHORT).show()//WITH UI

        //send info about ur warrior
        var send=JSONObject()
        send.put("n",warrior.split("/")[warrior.split("/").size-1])
        send.put("h",game_screen.height)
        send.put("w",game_screen.width)
        dout.writeUTF(send.toString())

        //get info about enemies warrior
        val digit=ByteArray(4)
        inputStream.read(digit, 0,4)
        var l= BigInteger(digit).toInt()//+2
        val en=ByteArray(l)//4?
        inputStream.read(en, 0,l)
        enemy=en.decodeToString()

        //check u have recources for this warrior
        val file= File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"/sources/images/minions/"+enemy)//?
        val log=File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"/sources/log.txt")
        log.createNewFile()
        log.writeText(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"/sources/images/minions/$enemy\n")
        if (file.exists()) {log.appendText("is exist\n");FIGHT()}
        else {log.appendText("need create\n");
            file.mkdir()
            //Toast.makeText(this, "so what the problem?", Toast.LENGTH_SHORT).show()

            //send name of warrior for download sources for it
            var message = JSONObject()
            message.put("c", enemy)
            dout.writeUTF(message.toString())
            dout.flush()

            //get sources
            var digit = ByteArray(4)
            inputStream.read(digit, 0, 4)
            var l = BigInteger(digit).toInt()
            var name = "";
            var name_b = ByteArray(l);
            inputStream.read(name_b, 0, l)
            name = name_b.decodeToString()
            //dout.writeUTF("ok")
            //dout.flush()
            while (name != "finshed") {
                val log=File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"/sources/log.txt")
                log.writeText("$name\n")
                var downloading_file =File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + "/" + name)

                if(name.contains(".")) {


                    if (downloading_file.exists()) break//maybe modificate
                    //else
                    downloading_file.createNewFile()

                    var digit = ByteArray(4)
                    inputStream.read(digit, 0, 4)
                    var l = BigInteger(digit).toInt()
                    val buf = ByteArray(1024)

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

            FIGHT()}//make more beautifull

    }
    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun FIGHT(){
        //send u ready to fight
        //Toast.makeText(this, "all ok!!", Toast.LENGTH_SHORT ).show()//with ui
        var message=JSONObject()
        message.put("c","ready")
        val log=File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"/sources/log.txt")
        //log.appendText("sended\n")
        dout.writeUTF(message.toString())
        dout.flush()
        //log.appendText(""+scale)
        log.appendText("sended\n")



        log.appendText("so it cool\n")

        var fight=true
        //socketU.broadcast=true//need we at if connect only to one client?
        while(fight) {
            //get word
            val digit=ByteArray(4)
            log.appendText("ok\n")
            inputStream.read(digit,0,4)
            log.appendText("very good\n")
            var l= BigInteger(digit).toInt()
            var buf = ByteArray(1024)//4?
            var num_read=0
            var get_js=ByteArray(0)
            log.appendText("$l\n")
            while (l > 0) {
                num_read = inputStream.read(buf, 0, min(buf.size, l))
                if (num_read == -1 ) {  // end of stream
                    break;
                }
                get_js = get_js + buf.copyOfRange(0, num_read)
                l -= num_read
            }
            log.appendText("get succesfull\n")
            log.appendText("$get_js\n")
            log.appendText(get_js.decodeToString()+"\n")
            log.appendText(""+get_js.size+"\n")
            //try work with word
            //try{
                var get = JSONObject(get_js.decodeToString())
                game_screen.get=get
                var world = get.getJSONArray("w")
                game_screen.world=world
                var face=get.getBoolean("f")
                game_screen.face=face
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
                game_screen.X=x
                game_screen.Y=y

                //maibe it next try or launch
                game_screen.get=get
                game_screen.world=world
                game_screen.face=face
                game_screen.isInitilized=true
                //try{game_screen.invalidate()}catch(e:Exception){}

                log.appendText("succesfull\n")
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
            log.appendText(touches.toString())
			log.appendText("normal send\n")
            touch_down= JSONArray()
            touch_up=JSONArray()
        }
        startActivity(Intent(this, WarriorListActivity::class.java))
    }

    fun downloadEnemie(file:File) {
        //Toast.makeText(this, "need create", Toast.LENGTH_SHORT).show()
        //file.createNewFile()
        file.mkdir()
        //Toast.makeText(this, "so what the problem?", Toast.LENGTH_SHORT).show()

        //send name of warrior for download sources for it
        var message = JSONObject()
        message.put("c", enemy)
        dout.writeUTF(message.toString())
        dout.flush()

        //get sources
        var digit = ByteArray(4)
        inputStream.read(digit, 0, 4)
        var l = BigInteger(digit).toInt()
        var name = "";
        var name_b = ByteArray(l);
        inputStream.read(name_b, 0, l)
        name = name_b.decodeToString()
        dout.writeUTF("ok")
        dout.flush()
        while (name != "finshed") {
            val log=File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"/sources/log.txt")
            log.createNewFile()
            log.writeText("$name\n")

            var downloading_file =File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + "/" + name)

            if(name.contains(".")) {


                if (downloading_file.exists()) break//maybe modificate
                //else
                downloading_file.createNewFile()

                var digit = ByteArray(4)
                inputStream.read(digit, 0, 4)
                var l = BigInteger(digit).toInt()
                val buf = ByteArray(1024)

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
            var name = "";
            var name_b = ByteArray(l)
            inputStream.read(name_b,0,l)
            name=name_b.decodeToString()
        }
        //signal that finish
        //dout.writeUTF("finished")
    }
    override fun onResume(){
        super.onResume()
        screen.makeFullScreenMode(this)
    }

    override fun onDestroy(){
        super.onDestroy()
        //save fight instance
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        var x= event?.getX(event.actionIndex)
        var y= Resources.getSystem().displayMetrics.heightPixels-event?.getY(event.actionIndex)!!
        var id=event.getPointerId(event.actionIndex)
        //var id=event?.getPointerId()
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                Toast.makeText(this,"action down", Toast.LENGTH_SHORT).show()
                var action=JSONObject()
                action.put("x",x)
                action.put("y",y)
                action.put("id",id)
                touch_down.put(action)//
                touch.put(action)
                Toast.makeText(this,touch.toString(), Toast.LENGTH_SHORT).show()
            }
            MotionEvent.ACTION_UP->{
                Toast.makeText(this,"action up", Toast.LENGTH_SHORT).show()
                var action=JSONObject()
                action.put("x",x)
                action.put("y",y)
                action.put("id",id)
                touch_up.put(action)//
                for(i in 0..touch.length()-1)if(touch.getJSONObject(i).get("id")==id ){touch.remove(i);Toast.makeText(this,"REMOVED",
                    Toast.LENGTH_SHORT).show();break}
                Toast.makeText(this,touch_up.toString(), Toast.LENGTH_SHORT).show()
            }
            MotionEvent.ACTION_MOVE->{
                for(i in 0..touch.length()-1)
                    if(touch.getJSONObject(i).get("id")==id )
                    {
                        touch.getJSONObject(i).put("x",x)
                        touch.getJSONObject(i).put("y",y)
                        break}
                Toast.makeText(this,"action move", Toast.LENGTH_SHORT).show()
                Toast.makeText(this,touch.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

}