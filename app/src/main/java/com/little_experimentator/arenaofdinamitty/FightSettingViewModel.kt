package com.little_experimentator.arenaofdinamitty

import android.content.Context
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.little_experimentator.arenaofdinamitty.adapters.WarriorIconAdapter
import java.io.File

class FightSettingViewModel:ViewModel() {
    val choosenWarriorLive= MutableLiveData<String>()
    val adapterLive=MutableLiveData<RecyclerView.Adapter<WarriorIconAdapter.ViewHolder>>()

    fun initiateAdapter(context: Context){
        adapterLive.value=WarriorIconAdapter(context,
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"//sources//images//minions").listFiles(),
            ::onClick
        )
    }
    fun onClick(name:String):Unit{
        changeChoosenWarrior(name)
    }

    fun changeChoosenWarrior(name:String){
        choosenWarriorLive.value=name+"/head.png"
        //later need update this logic
    }
}