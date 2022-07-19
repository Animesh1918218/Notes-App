package com.example.noteapp.Database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class MyRepository(private val mydao:MyTableDao) {
    fun getAllElements():Flow<ArrayList<MyTextData>>{
        return flow {
            mydao.getAllData().collect {
                emit(ArrayList(it))
            }
        }
    }


    suspend fun addElement(myTextData: MyTextData){
        mydao.insertData(myTextData)
    }

    suspend fun updateElement(myTextData: MyTextData){
        mydao.updateData(myTextData)
    }

    suspend fun deleteElement(myTextData: MyTextData){
        mydao.deleteData(myTextData)
    }
}