package sg.mirobotic.zoom.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import sg.mirobotic.zoom.data.Meeting

object DataConverter {

    fun toMeetingList(obj: JSONArray): ArrayList<Meeting> {
        val typeToken = object : TypeToken<ArrayList<Meeting>>() {}.type
        return Gson().fromJson(obj.toString(), typeToken)
    }
}