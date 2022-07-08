package com.example.secondhand.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.room.TypeConverter
import com.example.secondhand.model.GetBuyerProductResponseItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


// Save all utilities needed

//fun uriToFile(selectedImg: Uri, context: Context): File {
//    val contentResolver: ContentResolver = context.contentResolver
//    val myFile = createTempFile(context)
//
//    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
//    val outputStream: OutputStream = FileOutputStream(myFile)
//    val buf = ByteArray(1024)
//    var len: Int
//    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
//    outputStream.close()
//    inputStream.close()
//
//    return myFile
//}
//
//fun createTempFile(context: Context): File {
//    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//    return File.createTempFile(timeStamp, ".jpg", storageDir)
//}

//private const val FILENAME_FORMAT = "dd-MMM-yyyy"
//
//val timeStamp: String = SimpleDateFormat(
//    FILENAME_FORMAT,
//    Locale.US
//).format(System.currentTimeMillis())
//
//fun reduceFileImage(file: File): File {
//    val bitmap = BitmapFactory.decodeFile(file.path)
//    var compressQuality = 100
//    var streamLength: Int
//    do {
//        val bmpStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
//        val bmpPicByteArray = bmpStream.toByteArray()
//        streamLength = bmpPicByteArray.size
//        compressQuality -= 5
//    } while (streamLength > 1000000)
//    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
//    return file
//}

//fun <T> LiveData<T>.observeOnce(observer: (T) -> Unit) {
//    observeForever(object : Observer<T> {
//        override fun onChanged(value: T) {
//            removeObserver(this)
//            observer(value)
//        }
//    })
//}
//
//fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: (T) -> Unit) {
//    observe(owner, object : Observer<T> {
//        override fun onChanged(value: T) {
//            removeObserver(this)
//            observer(value)
//        }
//    })
//}

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        return if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            true
        } else capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }
    return false
}

class GithubTypeConverters {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<GetBuyerProductResponseItem> {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType = object : TypeToken<List<GetBuyerProductResponseItem?>?>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<GetBuyerProductResponseItem?>?): String {
        return Gson().toJson(someObjects)
    }
}

