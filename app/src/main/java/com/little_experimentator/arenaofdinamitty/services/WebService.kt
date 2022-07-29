package com.little_experimentator.arenaofdinamitty.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class WebService : Service() {


    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate(){
        //make connection
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        //close socket
    }
}