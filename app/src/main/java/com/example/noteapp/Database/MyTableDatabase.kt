package com.example.noteapp.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [MyTextData::class], version = 1)
abstract class MyTableDatabase:RoomDatabase() {

        abstract fun getDao(): MyTableDao

        companion object{
            @Volatile
            private var INSTANCE:MyTableDatabase?=null

            fun getDatabase(context: Context):MyTableDatabase{
                synchronized(this){
                    var instance = INSTANCE
                    if(instance==null){
                        instance =Room.databaseBuilder(context.applicationContext
                        ,MyTableDatabase::class.java,"Mytabedatabase").fallbackToDestructiveMigration().build()
                        INSTANCE=instance
                    }
                    return instance
                }
            }
        }

}