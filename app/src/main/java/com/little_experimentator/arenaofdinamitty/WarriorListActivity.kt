package com.little_experimentator.arenaofdinamitty

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.little_experimentator.arenaofdinamitty.usecases.Screen
import java.io.File

class WarriorListActivity : AppCompatActivity() {
    lateinit var main_layout: LinearLayout
    lateinit var adr: EditText
    lateinit var pref: SharedPreferences

    val widthPixels= Resources.getSystem().displayMetrics.widthPixels

    //usecases
    val screen = Screen()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_warrior_list)
        screen.makeFullScreenMode(this)
        Toast.makeText(this, "warrior list", Toast.LENGTH_SHORT).show()
        main_layout=findViewById(R.id.scroll_layout)
        adr=findViewById(R.id.adr)
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())

        //use last adres of server
        //it uses only in test version while we havnt own server:))
        if (pref.contains("adres")) {
            adr.setText( pref.getString("adres", ""))
        }
        //create icons of warriors
        var warriors= File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"//sources//images//minions").listFiles()
        var i=0
        for(warrior in warriors){//
            if(warrior.path.indexOf(".")==-1){//
                add_icon(warrior.path,i)
                i++}
        }
    }

    fun add_icon(name:String,i:Int){
        var new_view= ImageView(this)
        new_view.setScaleType(ImageView.ScaleType.FIT_XY)
        new_view.setImageBitmap(BitmapFactory.decodeFile(name+"/head.png"))//icon
        new_view.setLayoutParams(ViewGroup.LayoutParams(widthPixels/7,widthPixels/7))
        new_view.layout((i%7)*new_view.layoutParams.width,(i/7)*new_view.layoutParams.height,(i%7+1)*new_view.layoutParams.width,(i/7+1)*new_view.layoutParams.height)
        new_view.setScaleType(ImageView.ScaleType.FIT_XY)

        new_view.setOnClickListener {
            val editor = pref.edit()
            editor.putString("warrior", name)
            editor.putString("adres",adr.text.toString())
            editor.apply()
            startActivity(Intent(this, FightActivityOld::class.java))
        }

        main_layout.addView(new_view)
    }

    override fun onResume(){
        super.onResume()
        screen.makeFullScreenMode(this)
    }


}