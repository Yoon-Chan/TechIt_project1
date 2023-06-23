package com.example.ex2

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.*;
//카테고리 데이터

@Parcelize
data class Category(val title: String?, val memo: ArrayList<String> = arrayListOf()) : Parcelable{

}
