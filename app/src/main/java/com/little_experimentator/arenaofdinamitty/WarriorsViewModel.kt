package com.little_experimentator.arenaofdinamitty

import android.content.Context
import android.media.SoundPool
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.little_experimentator.arenaofdinamitty.adapters.WarriorIconAdapter
import com.little_experimentator.arenaofdinamitty.services.AudioService
import java.io.File

class WarriorsViewModel: ViewModel() {
    val choosenWarriorLive= MutableLiveData<String>()
    val adapterLive= MutableLiveData<RecyclerView.Adapter<WarriorIconAdapter.ViewHolder>>()

    var name=""

    fun initiateAdapter(context: Context){
        adapterLive.value=WarriorIconAdapter(context,
            //AudioService(),//after change this shit
            SoundPool(0,0,0),
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"//sources//images//minions").listFiles(),
            ::onClick
        )
    }//maibe it in usecase or fragment

    fun onClick(name:String,path:String/*,soundId:Int*/):Unit{
        changeChoosenWarrior(name,path)
        //after add audioservice and play sound
    }

    fun changeChoosenWarrior(name:String,path:String){
        this.name=name
        choosenWarriorLive.value=path+"/head.png"
        //later need update this logic

    }

}