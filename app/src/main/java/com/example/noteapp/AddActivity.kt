package com.example.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.Database.MyTextData
import com.example.noteapp.Database.MyViewModel
import com.example.noteapp.databinding.ActivityAddBinding
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.google.android.material.transition.platform.MaterialFade

class AddActivity : AppCompatActivity() {
    private var myview:ActivityAddBinding?=null
    private var myViewModel:MyViewModel?=null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        myview = ActivityAddBinding.inflate(layoutInflater)
        setContentView(myview?.root)
        setSupportActionBar(myview?.myToolBar)
       transitionConfig()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        myview?.myToolBar?.setNavigationOnClickListener {
            onBackPressed()
        }
        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        val item = intent.getParcelableExtra<MyTextData>(MainActivity.myParcelableData)
        if(item!=null){
            myview?.mytext?.setText(item.myText)
            myview?.newadbtn?.setOnClickListener {
                item.myText = myview?.mytext?.text.toString()
                myViewModel?.updateData(item)
                onBackPressed()
            }
        }
        else {
            myview?.newadbtn?.setOnClickListener {
                addText()
            }
        }
    }

    private fun addText(){
        val mytext = myview?.mytext?.text.toString()
        if(mytext.isEmpty()){
            Toast.makeText(this, "Plz Enter Something", Toast.LENGTH_SHORT).show()
        }
        else{
            myViewModel?.addData(MyTextData(myText = mytext))
            onBackPressed()
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        myview = null
        myViewModel=null
    }

    private fun transitionConfig(){
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = false
        val animation = MaterialContainerTransform()
        animation.duration = 500
        animation.addTarget(myview?.mylayoutnew)
        animation.pathMotion = MaterialArcMotion()
        animation.fadeMode = MaterialContainerTransform.FADE_MODE_THROUGH
        window.sharedElementEnterTransition =  animation
        window.sharedElementExitTransition = animation
        window.sharedElementReenterTransition = animation
    }
}