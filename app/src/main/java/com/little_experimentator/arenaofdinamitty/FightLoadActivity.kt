package com.little_experimentator.arenaofdinamitty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.little_experimentator.arenaofdinamitty.usecases.Permissions
import com.little_experimentator.arenaofdinamitty.usecases.Screen

class FightLoadActivity : AppCompatActivity() {
    lateinit var fight_load_vm: FightLoadViewModel

    lateinit var load_info: TextView
    lateinit var progress_bar: ProgressBar

    //usecases
    val screen= Screen()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight_load)

        load_info = findViewById(R.id.fightLoadInfo)
        progress_bar = findViewById(R.id.downloadEnemy)

        fight_load_vm = ViewModelProvider(this).get(FightLoadViewModel::class.java)
        fight_load_vm.progressStringLive.observe(this, Observer {
            load_info.text = it
        })
        fight_load_vm.progressMaxLive.observe(this, Observer {
            progress_bar.max=it
        })
        fight_load_vm.progressLive.observe(this, Observer {
            progress_bar.progress=it
        })

        fight_load_vm.load(this)
    }

    override fun onResume(){
        super.onResume()
        screen.makeFullScreenMode(this)
    }
}