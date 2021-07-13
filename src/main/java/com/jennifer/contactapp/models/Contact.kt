package com.jennifer.contactapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val fName: String,
    val PhoneNo: String
)
