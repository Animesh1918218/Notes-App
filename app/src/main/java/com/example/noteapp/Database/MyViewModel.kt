package com.example.noteapp.Database

import android.app.Application
import android.graphics.Canvas
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.MyAdapter
import com.example.noteapp.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


class MyViewModel(application: Application) :AndroidViewModel(application) {
    private var myRepository: MyRepository? = null

    init {
        val myDao = MyTableDatabase.getDatabase(application).getDao()
        myRepository = MyRepository(myDao)
    }

    fun getAllData(): Flow<ArrayList<MyTextData>> {
        return flow {
            myRepository?.getAllElements()?.collect {
                emit(it)
            }
        }
    }

    fun addData(myTextData: MyTextData) {
        viewModelScope.launch(Dispatchers.IO) {
            myRepository?.addElement(myTextData)
        }
    }

    fun updateData(myTextData: MyTextData) {
        viewModelScope.launch(Dispatchers.IO) {
            myRepository?.updateElement(myTextData)
        }
    }

    fun getItemTouchCallback(): ItemTouchHelper.SimpleCallback {
        return object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val id = (viewHolder as (MyAdapter.MainViewHolder)).myid
                when (direction) {
                    ItemTouchHelper.LEFT-> {
                        viewModelScope.launch {
                            myRepository?.deleteElement(MyTextData(id = id))
                        }
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(Color.parseColor("#F5F5F5"))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .create()
                    .decorate()


                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }


        }
    }




}