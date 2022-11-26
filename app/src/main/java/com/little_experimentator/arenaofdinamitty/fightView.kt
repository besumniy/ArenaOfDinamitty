package com.little_experimentator.arenaofdinamitty

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.shapes.Shape
import android.os.Build
import android.os.Environment
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import org.json.JSONArray
import org.json.JSONObject

class fightView: View {
    lateinit var activity:FightActivity
    lateinit var get: JSONObject
    lateinit var world: JSONArray
    var face=true
    var isInitilized=false
    //var sizeFormed=false
    var X=0
    var Y=0

    var side_width=0
    var icon_size= 0


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context):super(context) {
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet):super(context,attrs) {
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyle:Int):super(context, attrs, defStyle) {
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) /**/{
        //
        side_width=((width-height*1.5)/3.0).toInt()
        icon_size=side_width

        //sizeFormed=true
    }

    /*SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
     super.onDraw(canvas)
        Toast.makeText(activity,isInitilized.toString(), Toast.LENGTH_SHORT).show()
        if(isInitilized){
            //var for_draw= mutableListOf<ForDraw>()//MutableList<ForDraw>
            for (i in 0..world.length()-1) {//maybe change!!!
                var w = world.getJSONObject(i)

                drawArea(canvas!!,
                        BitmapDrawable(BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"//sources//"+w.getString("p"))),
                        w.getJSONObject("c").getInt("x"),w.getJSONObject("c").getInt("y"),w.getJSONObject("c").getInt("w"),w.getJSONObject("c").getInt("h"),
                        w.getJSONObject("c").getDouble("r").toFloat()
                )
            }

        drawArea(canvas!!,activity.getDrawable(R.drawable.control_fone)!!, 0, 0, side_width*2, height,0f)

        drawArea(canvas!!,activity.getDrawable(R.drawable.control_go)!!, (side_width*0.1).toInt(), (height-side_width*1.9).toInt(), (side_width*1.8).toInt(),(side_width*1.8).toInt(),0f)

        drawArea(canvas!!,activity.getDrawable(R.drawable.control_fone)!!, width-side_width, 0, side_width, height,0f)

        drawArea(canvas!!,activity.getDrawable(R.drawable.control_reverse)!!, side_width, (height-side_width*2.5).toInt(),side_width,side_width,0f)


        var icons= get.getJSONArray("i")
        //var for_draw_ic= mutableListOf<ForDraw>()//MutableList<ForDraw>
        for (i in 0..icons.length()-1) {
            var ics = icons.getJSONObject(i)
        drawArea(canvas!!,
                BitmapDrawable(BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"//sources//"+ics.getString("l"))),
                ics.getInt("x"),ics.getInt("y"),icon_size.toInt(),icon_size.toInt(),0f)
        }
        var health= get.getJSONObject("h").getJSONArray("l")
        //var for_draw_h= mutableListOf<ForDraw>()//MutableList<ForDraw>
        for (i in 0..health.length()-1) {
            var h = health.getJSONObject(i)//maybe "i"

            drawHealth(canvas!!,
                    BitmapDrawable(BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"//sources//"+h.getString("p"))),
                    h.getInt("x"),h.getInt("y"),h.getInt("w"),h.getInt("h"),
                    h.getDouble("r").toFloat(),
                    h.getDouble("v").toFloat()
            )

        }

        var i=0

            var messages= get.getJSONArray("m")
            //var for_draw_ic= mutableListOf<ForDraw>()//MutableList<ForDraw>
            for (i in 0..messages.length()-1) {
                var msg = messages.getJSONObject(i)
                drawArea(canvas!!,
                        BitmapDrawable(BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"//sources//"+msg.getString("l"))),
                        msg.getInt("x"),msg.getInt("y"),msg.getInt("w"),msg.getInt("h"),0f)
            }
        //activity.game_screen.removeAllViews
    }
    invalidate()}*/
    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        //Toast.makeText(activity,side_width.toString(), Toast.LENGTH_SHORT).show()
        if(isInitilized){
        //var for_draw= mutableListOf<ForDraw>()//MutableList<ForDraw>
        for (i in 0..world.length()-1) {//maybe change!!!
            var w = world.getJSONObject(i)

            drawArea(canvas!!,
                BitmapDrawable(BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"//sources//"+w.getString("p"))),
                w.getJSONObject("c").getInt("x"),w.getJSONObject("c").getInt("y"),w.getJSONObject("c").getInt("w"),w.getJSONObject("c").getInt("h"),
                w.getJSONObject("c").getDouble("r").toFloat()
            )
        }

            var darks= get.getJSONArray("d")
            //var for_draw_ic= mutableListOf<ForDraw>()//MutableList<ForDraw>
            for (i in 0..darks.length()-1) {
                var dark = darks.getJSONObject(i)
                drawDarkness(canvas!!,
                    dark.getDouble("x").toFloat(),dark.getDouble("y").toFloat(),
                    dark.getDouble("x1").toFloat(),dark.getDouble("y1").toFloat(),
                    dark.getDouble("x2").toFloat(),dark.getDouble("y2").toFloat(),
                    dark.getDouble("x3").toFloat(),dark.getDouble("y3").toFloat()
                )
            }

        drawArea(canvas!!,activity.getDrawable(R.drawable.control_fone)!!, 0, 0, side_width*2, height,0f)

        drawArea(canvas!!,activity.getDrawable(R.drawable.control_go)!!, (side_width*0.1).toInt(), (height-side_width*1.9).toInt(), (side_width*1.8).toInt(),(side_width*1.8).toInt(),0f)

        drawArea(canvas!!,activity.getDrawable(R.drawable.control_fone)!!, width-side_width, 0, side_width, height,0f)

        drawArea(canvas!!,activity.getDrawable(R.drawable.control_reverse)!!, side_width, (height-side_width*3).toInt(),side_width,side_width,0f)


        var icons= get.getJSONArray("i")
        //var for_draw_ic= mutableListOf<ForDraw>()//MutableList<ForDraw>
        for (i in 0..icons.length()-1) {
            var ics = icons.getJSONObject(i)
            drawArea(canvas!!,
                BitmapDrawable(BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"//sources//"+ics.getString("l"))),
                ics.getInt("x"),ics.getInt("y"),icon_size.toInt(),icon_size.toInt(),0f)
        }
        var health= get.getJSONObject("h").getJSONArray("l")
             //var for_draw_h= mutableListOf<ForDraw>()//MutableList<ForDraw>
        for (i in 0..health.length()-1) {
            var h = health.getJSONObject(i)//maybe "i"

            drawHealth(canvas!!,
                BitmapDrawable(BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"//sources//"+h.getString("p"))),
                h.getInt("x"),h.getInt("y"),h.getInt("w"),h.getInt("h"),
                h.getDouble("r").toFloat(),
                h.getDouble("v").toFloat()
            )

        }

        var i=0

        var messages= get.getJSONArray("m")
        //var for_draw_ic= mutableListOf<ForDraw>()//MutableList<ForDraw>
        for (i in 0..messages.length()-1) {
            var msg = messages.getJSONObject(i)
            drawArea(canvas!!,
                BitmapDrawable(BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path+"//sources//"+msg.getString("l"))),
                msg.getInt("x"),msg.getInt("y"),msg.getInt("w"),msg.getInt("h"),0f)
        }
        //activity.game_screen.removeAllViews()
    }
        invalidate()}
    fun drawArea(canvas: Canvas, drawable: Drawable, x:Int,y:Int,w:Int,h:Int, i:Float){
        if(canvas==null)return

        var rect=Rect(x,y,x+w,y+h)
        drawable.setBounds(rect)
        canvas.save()
        canvas.rotate(i,x+w/2f,y+h/2f)
        drawable.draw(canvas)
        canvas.restore()
    }

    fun drawHealth(canvas: Canvas, drawable: Drawable, x:Int,y:Int,w:Int,h:Int, i:Float,health:Float){
        if(canvas==null)return

        //Toast.makeText(activity,"sooooooo", Toast.LENGTH_SHORT).show()

        if(health>=0.5)drawable.setColorFilter(Color.rgb((255-(health*255)).toInt()*2,255,0),PorterDuff.Mode.MULTIPLY)
        //new_view.color="#"+(ff-(health.getInt(i)-health_max.getInt(i)/2)/(health_max.getInt(i)))+"ff00"
        else if(health>0)drawable.setColorFilter(Color.rgb(255,(health*2*255).toInt(),0),PorterDuff.Mode.MULTIPLY)
        else drawable.setColorFilter(Color.rgb(255,0,0),PorterDuff.Mode.MULTIPLY)

        var rect=Rect(x,y,x+w,y+h)
        //drawable.setColorFilter(Color.rgb(1,2,3),PorterDuff.Mode.OVERLAY)//add or other

        drawable.setBounds(rect)
        canvas.save()
        canvas.rotate(i,x+w/2f,y+h/2f)
        drawable.draw(canvas)
        canvas.restore()
    }
    fun drawDarkness(canvas: Canvas, x:Float,y:Float,x1:Float,y1:Float,x2:Float,y2:Float,x3:Float,y3:Float/*drawable: Drawable, x:Int,y:Int*/){
        //var shape= Shape()
        //drawable.setColorFilter(Color.rgb(0,0,0),PorterDuff.Mode.MULTIPLY)

        //drawable.setBounds(Quad(0,0,5,5,10,10))
        var path=Path()
        path.reset()
        path.moveTo(x,y)
        path.lineTo(x1,y1)
        path.lineTo(x2,y2)
        path.lineTo(x3,y3)
        path.lineTo(x,y)

        var paint=Paint()
        paint.setColor(Color.BLACK)
        //paint.style=Style.FILL


        canvas.drawPath(path,paint)
        canvas.save()
        //drawable.draw(canvas)
        canvas.restore()
    }
}