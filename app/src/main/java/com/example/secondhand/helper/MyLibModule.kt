package com.example.secondhand.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.io.ByteArrayOutputStream

fun <T> LiveData<T>.observeOnce(observer: (T) -> Unit) {
    observeForever(object : Observer<T> {
        override fun onChanged(value: T) {
            removeObserver(this)
            observer(value)
        }
    })
}

fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: (T) -> Unit) {
    observe(owner, object : Observer<T> {
        override fun onChanged(value: T) {
            removeObserver(this)
            observer(value)
        }
    })
}

fun Bitmap.convertBitmapToString(): String{
    val byteArray = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 100, byteArray)
    val b: ByteArray = byteArray.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}

fun String.convertStringToBitmap(): Bitmap{
    val byteArray1: ByteArray = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(
        byteArray1, 0,
        byteArray1.size
    )
}

fun String.convertStringToBinaryString(): String{
    val n = this.length -1
    var result = ""
    for(i in 0..n){
        var a = this[i].code
        var bin = ""
        while(a > 0){
            bin += if(a % 2 == 0){
                "0"
            }else{
                "1"
            }
            a /= 2
        }
        bin = bin.reverse()

        result += bin
    }
    return result
}

fun String.reverse(): String{
    val a: CharArray = this.toCharArray()
    var r = a.size - 1

    for(i in 0..r){
        val temp = a[i]
        a[i] = a[r]
        a[r] = temp
        r--
    }
    return a.toString()
}