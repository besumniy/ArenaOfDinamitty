package com.little_experimentator.arenaofdinamitty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.little_experimentator.arenaofdinamitty.usecases.Screen

class MenuActivity : AppCompatActivity() {

    lateinit var button_fight: Button
    lateinit var button_warriors: Button
    lateinit var button_shop: Button

    lateinit var menu_vm: MenuViewModel

    val screen = Screen()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        button_fight=findViewById(R.id.button_fight)
        button_warriors=findViewById(R.id.button_warriors)
        button_shop=findViewById(R.id.button_shop)

        menu_vm = ViewModelProvider(this).get(MenuViewModel::class.java)

        button_fight.setOnClickListener {
            startActivity(Intent(this,FightSettingActivity::class.java))
        }
        button_warriors.setOnClickListener {
            startActivity(Intent(this,WarriorsActivity::class.java))
        }
        button_shop.setOnClickListener {
            startActivity(Intent(this,ShopActivity::class.java))
        }



    }

    override fun onResume(){
        super.onResume()
        screen.makeFullScreenMode(this)
    }

}