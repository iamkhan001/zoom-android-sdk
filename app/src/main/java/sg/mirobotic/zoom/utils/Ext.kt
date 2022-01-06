package sg.mirobotic.zoom.utils

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

fun String.toShowTime(timeZone: String): String {
    try {
        val inFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH)
        inFormat.timeZone = TimeZone.getTimeZone(timeZone)
        val outFormat = SimpleDateFormat("dd MMM yy hh:mm a", Locale.ENGLISH)
        outFormat.timeZone = TimeZone.getDefault()
        val date = inFormat.parse(this)
        return outFormat.format(date!!)
    }catch (e: Exception){
        e.printStackTrace()
    }
    return ""
}

fun Date.toServerDateFormat(): String = SimpleDateFormat("yyyy-MM-dd" , Locale.getDefault()).format(this)
fun Date.toServerTimeFormat(): String = SimpleDateFormat("HH:mm" , Locale.getDefault()).format(this)

fun RecyclerView.addDivider() {
    this.addItemDecoration(
        DividerItemDecoration(
            this.context,
            DividerItemDecoration.VERTICAL
        )
    )
}