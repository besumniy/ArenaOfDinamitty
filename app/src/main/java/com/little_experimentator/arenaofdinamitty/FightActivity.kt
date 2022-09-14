package com.little_experimentator.arenaofdinamitty

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MotionEvent
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.little_experimentator.arenaofdinamitty.usecases.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.DataOutputStream
import java.net.Socket

class FightActivity: AppCompatActivity() {
    lateinit var fight_vm: FightViewModel

    lateinit var game_screen:fightView

    val screen = Screen()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight)

        game_screen=findViewById<fightView>(R.id.game_screen)
        game_screen.activity=this

        fight_vm = ViewModelProvider(this).get(FightViewModel::class.java)
        fight_vm.getLive.observe(this, Observer {
            game_screen.get= it
        })
        fight_vm.worldLive.observe(this, Observer {
            game_screen.world= it
        })
        fight_vm.faceLive.observe(this, Observer {
            game_screen.face= it
        })
        fight_vm.xLive.observe(this, Observer {
            game_screen.X= it
        })
        fight_vm.yLive.observe(this, Observer {
            game_screen.Y= it
        })
        fight_vm.isInitilizedLive.observe(this, Observer {
            game_screen.isInitilized= it
        })
        screen.makeFullScreenMode(this)
        fight_vm.connectToService(this)
        //fight_vm.sendSize(game_screen.width,game_screen.height)
        while(!game_screen.sizeFormed){}
        fight_vm.fight(this,game_screen.width,game_screen.height)
        //Toast.makeText(this,game_screen.height.toString(), Toast.LENGTH_SHORT).show()
        //Toast.makeText(this,game_screen.width.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onResume(){
        super.onResume()
        screen.makeFullScreenMode(this)
    }

@RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onTouchEvent(event: MotionEvent?): Boolean{
        super.onTouchEvent(event)
        //Toast.makeText(this,game_screen.side_width.toString(), Toast.LENGTH_SHORT).show()
        fight_vm.onClick(event)
        //Toast.makeText(this,fight_vm.touches.toString(), Toast.LENGTH_SHORT).show()
        //Toast.makeText(this,game_screen.height.toString(), Toast.LENGTH_SHORT).show()
        //Toast.makeText(this,game_screen.width.toString(), Toast.LENGTH_SHORT).show()
        return true
    }
}

