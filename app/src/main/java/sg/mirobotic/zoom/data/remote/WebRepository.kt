package sg.mirobotic.zoom.data.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sg.mirobotic.zoom.conf.ApiClientSecure
import sg.mirobotic.zoom.conf.Credentials
import sg.mirobotic.zoom.data.Meeting
import sg.mirobotic.zoom.data.MeetingDetails
import sg.mirobotic.zoom.utils.DataConverter.toMeetingList
import java.util.ArrayList

class WebRepository(private val userId: MutableLiveData<String>) {

    companion object {

        private const val TAG = "WebRepository"

        private var webRepository: WebRepository? = null

        fun getInstance(userId: MutableLiveData<String>): WebRepository {

            if (webRepository == null) {
                webRepository = WebRepository(userId)
            }

            return webRepository!!
        }

    }

    private val apiInterface = ApiClientSecure(Credentials.getJWTToken()).getRetrofitInstance().create(
        ApiInterface::class.java)

    fun getProfile(apiResponseListener: ApiResponseListener<String>) {

        val call = apiInterface.getProfile(userId.value!!)

        call.enqueue(object : Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, "Error: ${t.printStackTrace()}")
                apiResponseListener.onError("No internet access")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.code() == 200 && response.body() != null) {
                    Log.e(TAG,"profile ??> ${response.body()?.string()}")
                    apiResponseListener.onSuccess("")
                    return
                }
                var error = "Invalid username or password"
                try {
                    if (response.errorBody() != null) {
                        val obj = JSONObject(response.errorBody()!!.string())
                        if (obj.has("message")) {
                            error = obj.getString("message")
                        }
                    }
                }catch (e: Exception) {
                    e.printStackTrace()
                }
                Log.e(TAG,"res: ${response.errorBody()?.string()}")
                apiResponseListener.onError(error)
            }
        })

    }

    fun addMeeting(meetingDetails: MeetingDetails, apiResponseListener: ApiResponseListener<MeetingDetails>) {

        val call = apiInterface.addMeeting(userId.value!!, meetingDetails)

        call.enqueue(object : Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, "Error: ${t.printStackTrace()}")
                apiResponseListener.onError("No internet access")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.body() != null) {
                    val body = response.body()?.string()
                    Log.e(TAG,"meeting ??> $body")

                    try {
                        val data: MeetingDetails = Gson().fromJson(body, MeetingDetails::class.java)
                        apiResponseListener.onData(data)
                        return
                    }catch (e: Exception) {
                        e.printStackTrace()
                    }

                    apiResponseListener.onError("Internal Error!")
                    return
                }
                var error = "Server error"
                try {
                    if (response.errorBody() != null) {
                        val obj = JSONObject(response.errorBody()!!.string())
                        if (obj.has("message")) {
                            error = obj.getString("message")
                        }
                    }
                }catch (e: Exception) {
                }
                Log.e(TAG,"res: ${response.errorBody()?.string()}")
                apiResponseListener.onError(error)
            }
        })

    }

    fun getMeetings(
        mMeetings: MutableLiveData<ArrayList<Meeting>>,
        apiResponseListener: ApiResponseListener<String>
    ) {

        val call = apiInterface.getMeetings(userId.value!!)

        call.enqueue(object : Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, "Error: ${t.printStackTrace()}")
                apiResponseListener.onError("No internet access")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.body() != null) {
                    val body = response.body()?.string()
                    Log.e(TAG,"meeting ??> $body")
                    try {
                        val obj = JSONObject(body!!)
                        val list = toMeetingList(obj.getJSONArray("meetings"))
                        mMeetings.postValue(list)
                        return
                    }catch (e: Exception) {
                        e.printStackTrace()
                    }

                    apiResponseListener.onError("Internal Error!")
                    return
                }
                var error = "Server error"
                try {
                    if (response.errorBody() != null) {
                        val obj = JSONObject(response.errorBody()!!.string())
                        if (obj.has("message")) {
                            error = obj.getString("message")
                        }
                    }
                }catch (e: Exception) {
                }
                Log.e(TAG,"res: ${response.errorBody()?.string()}")
                apiResponseListener.onError(error)
            }
        })

    }

    fun getMeetingDetails(
        meetingId: String,
        apiResponseListener: ApiResponseListener<MeetingDetails>
    ) {

        val call = apiInterface.getMeetingInfo(meetingId)

        call.enqueue(object : Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, "Error: ${t.printStackTrace()}")
                apiResponseListener.onError("No internet access")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.body() != null) {
                    val body = response.body()?.string()
                    Log.e(TAG,"meeting ??> $body")
                    try {
                        val meetingDetails: MeetingDetails = Gson().fromJson(body, MeetingDetails::class.java)
                        apiResponseListener.onData(meetingDetails)
                        return
                    }catch (e: Exception) {
                        e.printStackTrace()
                    }

                    apiResponseListener.onError("Internal Error!")
                    return
                }
                var error = "Server error"
                try {
                    if (response.errorBody() != null) {
                        val obj = JSONObject(response.errorBody()!!.string())
                        if (obj.has("message")) {
                            error = obj.getString("message")
                        }
                    }
                }catch (e: Exception) {
                }
                Log.e(TAG,"res: ${response.errorBody()?.string()}")
                apiResponseListener.onError(error)
            }
        })

    }
}