package com.little_experimentator.arenaofdinamitty.adapters

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.SoundPool
import android.os.Environment
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.little_experimentator.arenaofdinamitty.R
import com.little_experimentator.arenaofdinamitty.services.AudioService
import java.io.File

class WarriorIconAdapter(val context: Context,/*val audioService: AudioService,*/val soundPool: SoundPool, val items:Array<File>,function:(name:String, path:String/*,soundId:Int*/)->Unit): RecyclerView.Adapter<WarriorIconAdapter.ViewHolder>() {
    //var warriors= File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"//sources//images//minions").listFiles()
    var function=function
    lateinit var soundpool: SoundPool
    //var soundIdList=mutableListOf(0)
    var init_sounds=0
    //val saving_items=items///this is boolshit i will fix it later

    //lateinit var AudioServiceConnection: ServiceConnection
    //lateinit var audioService: AudioService
    //var audioServiceIsInit=false




    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WarriorIconAdapter.ViewHolder {

        /*AudioServiceConnection = object: ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                val log =
                    File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + "/sources/log lifecicle.txt")
                log.appendText("Service try init")

                var myBinder: AudioService.AudioServiceBinder =
                    service as AudioService.AudioServiceBinder
                audioService = myBinder.getService()
                audioServiceIsInit=true

                log.appendText("Service")
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                TODO("Not yet implemented")
            }
        }
        var intent1 = Intent(context, AudioService::class.java)
        context.bindService(intent1,AudioServiceConnection, Context.BIND_AUTO_CREATE)*/

        soundpool= soundPool//SoundPool(3, AudioManager.STREAM_MUSIC,0)

        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_warrior_icon,parent,false)
        )
    }

    override fun onBindViewHolder(holder: WarriorIconAdapter.ViewHolder, position: Int) {
        var name=items.get(position).name
        var path=items.get(position).path
        //while(!audioServiceIsInit){}
        //var soundId=audioService.loadSound(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"//sources//sounds//minions//"+name+"//congratulations.mp3")
        //name=warriors.get(position).name

        //var soundId=(soundpool.load(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"//sources//sounds//minions//"+name+"//congratulations.mp3",1))
        //var soundId=soundIdList[soundIdList.size-1]
        init_sounds+=1
        var id=init_sounds
        //log.appendText("SoundId"+soundId.toString()+"and"+init_sounds.toString())

        holder.itemImage.setImageBitmap(BitmapFactory.decodeFile(path+"/head.png"))
        holder.itemName.text=name


        //var new_view= ImageView(activity)
        //new_view.setScaleType(ImageView.ScaleType.FIT_XY)
        //new_view.setImageBitmap(BitmapFactory.decodeFile(name+"/head.png"))//icon
        //new_view.setLayoutParams(ViewGroup.LayoutParams(widthPixels/7,widthPixels/7))
        //new_view.layout((i%7)*new_view.layoutParams.width,(i/7)*new_view.layoutParams.height,(i%7+1)*new_view.layoutParams.width,(i/7+1)*new_view.layoutParams.height)
        //new_view.setScaleType(ImageView.ScaleType.FIT_XY)

        holder.itemImage.setOnClickListener {
            function(name,path/*,soundId*/)
            //var soundId=soundpool.load(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"//sources//sounds//minions//"+name+"//congratulations.mp3",1)
            soundpool.play(id, 1f,1f,1,0,1f)
            //soundpool.unload(soundId)
        }
    }

    override fun getItemCount(): Int {
        return items.size//warriors.size
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var itemImage:ImageView
        var itemName: TextView

        init{
            //super(itemView)

            itemImage=itemView.findViewById(R.id.image)
            itemName=itemView.findViewById(R.id.name)

        }
    }

}