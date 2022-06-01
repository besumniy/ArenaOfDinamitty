package com.little_experimentator.arenaofdinamitty.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.little_experimentator.arenaofdinamitty.R
import java.io.File

class WarriorIconAdapter(val context: Context, val items:Array<File>,function:(name:String, path:String)->Unit): RecyclerView.Adapter<WarriorIconAdapter.ViewHolder>() {
    var warriors= File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"//sources//images//minions").listFiles()
    var function=function


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WarriorIconAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_warrior_icon,parent,false)
        )
    }

    override fun onBindViewHolder(holder: WarriorIconAdapter.ViewHolder, position: Int) {
        var name=items.get(position).name
        var path=items.get(position).path
        //name=warriors.get(position).name

        holder.itemImage.setImageBitmap(BitmapFactory.decodeFile(path+"/head.png"))
        holder.itemName.text=name

        //var new_view= ImageView(activity)
        //new_view.setScaleType(ImageView.ScaleType.FIT_XY)
        //new_view.setImageBitmap(BitmapFactory.decodeFile(name+"/head.png"))//icon
        //new_view.setLayoutParams(ViewGroup.LayoutParams(widthPixels/7,widthPixels/7))
        //new_view.layout((i%7)*new_view.layoutParams.width,(i/7)*new_view.layoutParams.height,(i%7+1)*new_view.layoutParams.width,(i/7+1)*new_view.layoutParams.height)
        //new_view.setScaleType(ImageView.ScaleType.FIT_XY)

        holder.itemImage.setOnClickListener {
            function(name,path)
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