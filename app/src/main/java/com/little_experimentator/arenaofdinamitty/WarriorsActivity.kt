package com.little_experimentator.arenaofdinamitty

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.little_experimentator.arenaofdinamitty.usecases.Screen
import java.util.Observer

class WarriorsActivity : AppCompatActivity() {
    lateinit var w_vm: WarriorsViewModel

    lateinit var scroll_layout: LinearLayout
    lateinit var recycler_warriors: RecyclerView
    lateinit var choosen_warrior_img: ImageView

    lateinit var layoutManager: RecyclerView.LayoutManager

    //usecases
    val screen= Screen()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_warriors)

        scroll_layout=findViewById(R.id.scroll_layout)
        recycler_warriors=findViewById(R.id.warriors)
        choosen_warrior_img=findViewById(R.id.choosenWarrior)

        layoutManager= LinearLayoutManager(this)
        /*adapter=WarriorIconAdapter(this,
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"//sources//images//minions").listFiles(),
            ::onClick
        )*/

        w_vm = ViewModelProvider(this).get(WarriorsViewModel::class.java)
        w_vm.choosenWarriorLive.observe(this, {
            choosen_warrior_img.setImageBitmap(BitmapFactory.decodeFile(it))
        })
        w_vm.adapterLive.observe(this,{
            recycler_warriors.adapter=it
        })

        w_vm.initiateAdapter(this)
    }

    override fun onResume(){
        super.onResume()
        screen.makeFullScreenMode(this)
    }
}