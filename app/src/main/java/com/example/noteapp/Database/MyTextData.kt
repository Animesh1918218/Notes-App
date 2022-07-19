package com.example.noteapp.Database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mydatatable")
data class MyTextData(
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,
    var myText:String?=""
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(myText)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyTextData> {
        override fun createFromParcel(parcel: Parcel): MyTextData {
            return MyTextData(parcel)
        }

        override fun newArray(size: Int): Array<MyTextData?> {
            return arrayOfNulls(size)
        }
    }
}
