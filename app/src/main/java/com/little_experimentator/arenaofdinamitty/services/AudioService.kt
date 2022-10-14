package com.little_experimentator.arenaofdinamitty.services

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.SoundPool
import android.os.Binder
import android.os.IBinder

class AudioService: Service() {
    var binder= AudioServiceBinder()

    lateinit var soundpool: SoundPool

    override fun onCreate() {
        super.onCreate()
        soundpool= SoundPool(3, AudioManager.STREAM_MUSIC,0)
    }

    fun loadSound(path:String):Int{
        //in future add check is load
        return soundpool.load(path,1)
    }
    fun playSound(soundId:Int){
        soundpool.play(soundId,1f,1f,1,0,1f)
    }

    inner class AudioServiceBinder(): Binder() {
        fun getService():AudioService=this@AudioService
    }

    override fun onBind(intent: Intent): IBinder {
        return this.binder
    }
}