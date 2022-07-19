package com.example.noteapp

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.Database.MyTextData
import com.example.noteapp.databinding.MyrecyclerviewBinding

class MyAdapter(private val myItems:ArrayList<MyTextData>,val onClickListenerRl:(myText:MyTextData,text:TextView,layout:ConstraintLayout)->Unit):RecyclerView.Adapter<MyAdapter.MainViewHolder>(){
    private var context:Context?=null
    inner class MainViewHolder(item:MyrecyclerviewBinding):RecyclerView.ViewHolder(item.root){
        val mytxt = item.textView
        var myid:Int = 0
        val myLayout = item.myLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        context = parent.context
      return  MainViewHolder(MyrecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.myid = myItems[position].id
        if(myItems[position].myText?.length!!>=30){
            holder.mytxt.text = myItems[position].myText?.substring(0,19)+"....."
        }
        else {
            holder.mytxt.text = myItems[position].myText
        }
        holder.myLayout.animation = AnimationUtils.loadAnimation(context,R.anim.customanimation)
        holder.mytxt.animation = AnimationUtils.loadAnimation(context,R.anim.mytxtanimation)
        holder.mytxt.transitionName = position.toString()
        holder.myLayout.transitionName= String.format("mylayout${position}")
        holder.myLayout.setOnClickListener {
            onClickListenerRl.invoke(myItems[position],holder.mytxt,holder.myLayout)
        }
    }

    override fun getItemCount(): Int {
           return myItems.size
    }


}