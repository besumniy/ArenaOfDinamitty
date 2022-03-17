package com.little_experimentator.arenaofdinamitty.usecases

import android.app.Activity
import android.os.Environment
import android.widget.ImageView
import android.widget.LinearLayout
import com.little_experimentator.arenaofdinamitty.FightActivity
import java.io.File

class GenerateListOfWarriors {
   fun generate(activity: Activity, layout: LinearLayout, function:(name:String)->Unit){
    var warriors= File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"//sources//images//minions").listFiles()
    var i=0
    for (warrior in warriors){//
        if(warrior.path.indexOf(".")==-1){//
            add_icon(activity,layout,warrior.path,i,function)
            i++}
    }}


    private fun add_icon(activity: Activity,layout:LinearLayout,name:String,i:Int,function:(name:String)->Unit){
        var new_view= ImageView(activity)
        //new_view.setScaleType(ImageView.ScaleType.FIT_XY)
        //new_view.setImageBitmap(BitmapFactory.decodeFile(name+"/head.png"))//icon
        //new_view.setLayoutParams(ViewGroup.LayoutParams(widthPixels/7,widthPixels/7))
        //new_view.layout((i%7)*new_view.layoutParams.width,(i/7)*new_view.layoutParams.height,(i%7+1)*new_view.layoutParams.width,(i/7+1)*new_view.layoutParams.height)
        //new_view.setScaleType(ImageView.ScaleType.FIT_XY)

        new_view.setOnClickListener {
            function(name)
        }

        layout.addView(new_view)//just a add?
    }
}