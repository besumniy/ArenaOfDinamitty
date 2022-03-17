package com.little_experimentator.arenaofdinamitty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.little_experimentator.arenaofdinamitty.usecases.GenerateListOfWarriors
import com.little_experimentator.arenaofdinamitty.usecases.Screen

class FightSettingActivity : AppCompatActivity() {
    lateinit var fs_vm: FightSettingViewModel

    lateinit var button_fight: Button
    lateinit var scroll_layout: LinearLayout

    var choosen_warrior=""

    //usecases
    val screen= Screen()
    val listOfWarriors=GenerateListOfWarriors()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight_setting)

        button_fight=findViewById(R.id.button_fight)
        scroll_layout=findViewById(R.id.scroll_layout)

        fs_vm = ViewModelProvider(this).get(FightSettingViewModel::class.java)
        fs_vm.choosenWarriorLive.observe(this, Observer {
            choosen_warrior = it
        })
        listOfWarriors.generate(this,scroll_layout,::onClick)//can i move it to viewmodel??? must to check it later

        button_fight.setOnClickListener {
            startActivity(Intent(this,FightLoadActivity::class.java))
        }


    }

    override fun onResume(){
        super.onResume()
        screen.makeFullScreenMode(this)
    }
    fun onClick(name:String):Unit{
        choosen_warrior=name
        //maybe do function in vm?
    }
}