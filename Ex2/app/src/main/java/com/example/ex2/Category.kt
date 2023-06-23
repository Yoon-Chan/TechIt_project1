package com.example.ex2

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.*;
//카테고리 데이터

//애너테이션
@Parcelize
data class Category(val title: String?, val memo: MutableList<Memo> = mutableListOf()) : Parcelable{

}

@Parcelize
data class Memo(val title : String, val content : String) : Parcelable {

}
