package sg.mirobotic.zoom.data.remote

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import sg.mirobotic.zoom.data.MeetingDetails

interface ApiInterface {

    @GET("v2/users/{userId}")
    fun getProfile(@Path("userId") userId: String): Call<ResponseBody>

    @GET("v2/users/{userId}/meetings")
    fun getMeetings(@Path("userId") userId: String): Call<ResponseBody>

    @GET("v2/meetings/{meetingId}")
    fun getMeetingInfo(@Path("meetingId") meetingId: String): Call<ResponseBody>

    @POST("v2/users/{userId}/meetings")
    fun addMeeting(@Path("userId") userId: String, @Body meetingDetails: MeetingDetails): Call<ResponseBody>

}