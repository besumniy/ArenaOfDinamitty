package com.little_experimentator.arenaofdinamitty

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.little_experimentator.arenaofdinamitty.usecases.Permissions
import com.little_experimentator.arenaofdinamitty.usecases.Screen
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.File
import java.lang.Math.min
import java.math.BigInteger
import java.net.Socket

class LoadActivity : AppCompatActivity() {
    lateinit var load_vm: LoadViewModel

    lateinit var load_info: TextView
    lateinit var progress_bar: ProgressBar

    //usecases
    val permissions=Permissions()
    val screen= Screen()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)

        //Toast.makeText(this, "u open", Toast.LENGTH_SHORT).show()
        //fix it
        permissions.getPermissions(this)

        //Toast.makeText(this, "u get permissions;)", Toast.LENGTH_SHORT).show()

        load_info = findViewById(R.id.load_info)
        progress_bar = findViewById(R.id.progressBar)

        load_vm = ViewModelProvider(this,LoadViewModelFactory(this)).get(LoadViewModel::class.java)
        load_vm.progressStringLive.observe(this, Observer {
            load_info.text = it
        })
        load_vm.progressMaxLive.observe(this, Observer {
            progress_bar.max=it
        })
        load_vm.progressLive.observe(this, Observer {
            progress_bar.progress=it
        })

        load_vm.loading()

    }

    override fun onResume(){
        super.onResume()
        screen.makeFullScreenMode(this)
    }
}
