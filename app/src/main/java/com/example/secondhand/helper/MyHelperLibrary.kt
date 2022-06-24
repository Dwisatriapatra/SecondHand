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

fun String.convertStringToBitmap(): Bitmap {
    val byteArray1: ByteArray = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(
        byteArray1, 0,
        byteArray1.size
    )
}

fun String.convertStringToBinaryString() : String {
    val n = this.length
    var a = ""
    for (i in 0 until n) {
        // convert each char to
        // ASCII value
        var x = Integer.valueOf(this[i].code)

        // Convert ASCII value to binary
        var bin = ""
        while (x > 0) {
            bin += if (x % 2 == 1) {
                '1'
            } else '0'
            x /= 2
        }
        bin = bin.myReverse()
        a += bin
    }
    return a
}

fun String.myReverse(): String {
    val a = this.toCharArray()
    var r: Int = a.size - 1
    var l= 0
    while (l < r) {

        // Swap values of l and r
        val temp = a[l]
        a[l] = a[r]
        a[r] = temp
        l++
        r--
    }
    return String(a)
}