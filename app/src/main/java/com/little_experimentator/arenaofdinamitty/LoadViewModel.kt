package com.little_experimentator.arenaofdinamitty

import android.os.Environment
import android.preference.PreferenceManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.little_experimentator.arenaofdinamitty.usecases.Load
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.File
import java.math.BigInteger
import java.net.Socket

class LoadViewModel(load:Load): ViewModel() {

    val adress = "192.168.1.109"//this is ip of computer in wifi
    val port = 8081

    var isFirstTime = true

    val progressLive=MutableLiveData<Int>()
    var progressMaxLive=MutableLiveData<Int>()
    val progressStringLive=MutableLiveData<String>()

    //usecases
    val load=load

    fun loading(){


        isFirstTime=load.getIsFistTime()

        if (isFirstTime) {
            GlobalScope.launch(Dispatchers.IO) {
                download_sources(adress, port)
            }
        } else {
            //open avtorisation (in full)
            //open list of warriors
            load.isFinished()
        }
    }

    suspend fun download_sources(adress: String, port: Int) {//later go out
        val socket = Socket(adress, port)
        val dout = DataOutputStream(socket.getOutputStream())
        val inputStream = socket.getInputStream()

        //request about all directories and resources
        var message = JSONObject()
        message.put("c", "directories.txt")
        dout.writeUTF(message.toString())//? // maybe data about user
        dout.flush()
        val digit = ByteArray(4)
        inputStream.read(digit, 0, 4)
        var l = BigInteger(digit).toInt() + 2

        progressStringLive.value="Get list of files..."
        progressMaxLive.value=l
        progressLive.value=l
        //generate text for progressStringLive

        //download resources
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "directories.txt"
        )
        file.createNewFile()
        val buf = ByteArray(1024)
        while (l > 0) {
            val num_read = inputStream.read(buf, 0, Math.min(buf.size, l))
            if (num_read == -1) {  // end of stream
                break;
            }
            file.appendBytes(buf)
            l -= num_read
            progressLive.value=l
        }

        progressStringLive.value="Download files..."
        progressMaxLive.value=file.readLines().size//later check it
        progressLive.value=file.readLines().size

        for (line in file.readLines()) {
            var downloading_file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                line
            )
            if (downloading_file.exists()) {progressLive.value=progressLive.value!!-1;break}//later check full downloading
            //else

            progressStringLive.value="Download $line"
            downloading_file.createNewFile()
            dout.writeUTF(line)
            dout.flush()

            val digit = ByteArray(4)
            inputStream.read(digit, 0, 4)
            var l = BigInteger(digit).toInt() + 2
            val buf = ByteArray(1024)

            while (l > 0) {
                val num_read = inputStream.read(buf, 0, Math.min(buf.size, l))
                if (num_read == -1) {  // end of stream
                    break;
                }
                downloading_file.appendBytes(buf)
                l -= num_read
            }
            progressLive.value=progressLive.value!!-1
        }

            //closing
            dout.writeUTF("finished")//send server we funished downloading
            dout.flush()
            dout.close()//
            socket.close()//

           load.setIsFirstTime()


            //open avtorisation (in full)
            //open list of warriors

            load.isFinished()

        }


    override fun onCleared() {
        super.onCleared()
    }

}