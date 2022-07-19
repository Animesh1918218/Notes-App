package com.example.noteapp.Database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MyTableDao {
    @Insert
    suspend fun insertData(mydata:MyTextData)
    @Delete
    suspend fun deleteData(myData:MyTextData)
    @Update
    suspend fun updateData(mydata: MyTextData)
    @Query("Select * from mydatatable")
     fun getAllData():Flow<List<MyTextData>>
    @Query("Select * from mydatatable where id=:id")
    fun getSelectedData(id:Int):Flow<MyTextData>
}