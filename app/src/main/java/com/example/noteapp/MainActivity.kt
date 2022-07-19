package com.example.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Transition

import android.util.Log
import android.view.View

import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.Database.MyTextData
import com.example.noteapp.Database.MyViewModel
import com.example.noteapp.databinding.ActivityMainBinding
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    companion object{
        const val myParcelableData = "MySentData"
    }
    private var myView:ActivityMainBinding?=null
    private var myViewModel:MyViewModel?=null
    private var myAdapter:MyAdapter?=null
    private var myItemTouchHelper:ItemTouchHelper?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myView = ActivityMainBinding.inflate(layoutInflater)
        setContentView(myView?.root)
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        setUpMyAdapter()
        myItemTouchHelper = ItemTouchHelper(myViewModel?.getItemTouchCallback() as ItemTouchHelper.Callback)

        myView?.myAddBtn?.setOnClickListener {
            Intent(this, AddActivity::class.java).also {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair(myView?.myAddBtn,"mybigscreen"))

                startActivity(it,options.toBundle())

            }
        }
        myView?.mysearch?.setOnQueryTextListener(setUpMyQuery())
        myItemTouchHelper?.attachToRecyclerView(myView?.myRlView)



    }


    private fun setUpMyAdapter(){

             lifecycleScope.launch {
                 myViewModel?.getAllData()?.collect {
                     val mydata =it
                     Log.i("DataList", "$mydata")
                     if (mydata.isEmpty()) {
                         myView?.myRlView?.visibility = View.GONE
                         myView?.myRandomTxt?.visibility = View.VISIBLE
                     } else {
                         myView?.myRlView?.visibility = View.VISIBLE
                         myView?.myRandomTxt?.visibility = View.GONE
                         myAdapter = MyAdapter(mydata){
                                 myonedata,mytext,mylayout->
                               Intent(this@MainActivity,AddActivity::class.java).also {
                                   it.putExtra(myParcelableData,myonedata)
                                   val options= ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity,
                                       Pair(mytext,"mytxttn"), Pair(mylayout,"mybigscreen")
                                   )
                                   startActivity(it,options.toBundle())
                               }
                         }
                         myView?.myRlView?.adapter = myAdapter

                         myView?.myRlView?.layoutManager =
                             StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
                     }
                 }
             }
    }


 private fun setUpMyQuery():androidx.appcompat.widget.SearchView.OnQueryTextListener {
     return object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
         override fun onQueryTextSubmit(query: String?): Boolean {
          return false
         }

         override fun onQueryTextChange(newText: String?): Boolean {

                 lifecycleScope.launch {
                         myViewModel?.getAllData()?.collect {
                             if(newText!!.isNotEmpty()) {
                                 val myOriginalData = it
                                 val myDisplayedData = ArrayList<MyTextData>()
                                 myView?.myAddBtn?.visibility = View.GONE
                             for (item in myOriginalData) {
                                 if (item.myText!!.lowercase().contains(newText.lowercase())) {
                                     myDisplayedData.add(item)
                                 }
                             }
                             myView?.myRlView?.adapter = MyAdapter(myDisplayedData) {
                                     myonedata,mytext,mylayout ->
                                 Intent(this@MainActivity, AddActivity::class.java).also {
                                     it.putExtra(myParcelableData, myonedata)
                                     val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity,
                                         Pair(mytext,"mytxttn"), Pair(mylayout,"mybigscreen")
                                         )
                                     startActivity(it,options.toBundle())
                                 }
                             }

                             myView?.myRlView?.layoutManager =
                                 StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                         }
                             else {
                                 myView?.myAddBtn?.visibility =View.VISIBLE
                                 setUpMyAdapter()
                             }
                     }
                 }
             return true
         }
     }
 }
    override fun onDestroy() {
        super.onDestroy()
        myView = null

    }

}