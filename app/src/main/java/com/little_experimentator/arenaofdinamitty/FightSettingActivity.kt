package com.little_experimentator.arenaofdinamitty

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.little_experimentator.arenaofdinamitty.adapters.WarriorIconAdapter
import com.little_experimentator.arenaofdinamitty.usecases.GenerateListOfWarriors
import com.little_experimentator.arenaofdinamitty.usecases.Screen
import java.io.File

class FightSettingActivity : AppCompatActivity() {
    var clickable=true

    lateinit var fs_vm: FightSettingViewModel

    lateinit var button_fight: Button
    //lateinit var scroll_layout: LinearLayout
    lateinit var recycler_warriors: RecyclerView
    lateinit var choosen_warrior_img: ImageView
    lateinit var server_ip: EditText

    lateinit var layoutManager:RecyclerView.LayoutManager
    //lateinit var adapter:RecyclerView.Adapter<WarriorIconAdapter.ViewHolder>

    //var choosen_warrior=""

    //usecases
    val screen= Screen()
    val listOfWarriors=GenerateListOfWarriors()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight_setting)

        button_fight=findViewById(R.id.button_fight)
        //scroll_layout=findViewById(R.id.scroll_layout)
        recycler_warriors=findViewById(R.id.warriors)
        choosen_warrior_img=findViewById(R.id.choosenWarrior)
        server_ip=findViewById(R.id.serverIp)

        layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recycler_warriors.layoutManager=layoutManager

        fs_vm = ViewModelProvider(this).get(FightSettingViewModel::class.java)
        fs_vm.clickableLive.observe(this,{
            clickable=it
            button_fight.isEnabled = it
        })
        fs_vm.buttonTextLive.observe(this,{
            button_fight.text=it
        })
        fs_vm.choosenWarriorLive.observe(this, Observer {
            choosen_warrior_img.setImageBitmap(BitmapFactory.decodeFile(it))
        })
        fs_vm.adapterLive.observe(this,{
            recycler_warriors.adapter=it
        })

        fs_vm.init(this)
        //listOfWarriors.generate(this,scroll_layout,::onClick)//can i move it to viewmodel??? must to check it later

        button_fight.setOnClickListener {
            fs_vm.findFight(this,server_ip.text.toString())//check how clear is it
            //context.startActivity(Intent(context,FightLoadActivity::class.java))
            //startActivity(Intent(this,FightLoadActivity::class.java))
        }


    }

    override fun onResume(){
        super.onResume()
        screen.makeFullScreenMode(this)
    }
    /*fun onClick(name:String):Unit{
        fs_vm.changeChoosenWarrior(name)
    }*/
}