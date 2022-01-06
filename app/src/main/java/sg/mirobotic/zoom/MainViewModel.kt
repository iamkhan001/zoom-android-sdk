package sg.mirobotic.zoom

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sg.mirobotic.zoom.conf.Credentials
import sg.mirobotic.zoom.data.*
import sg.mirobotic.zoom.data.local.UserDataProvider
import sg.mirobotic.zoom.data.remote.ApiResponseListener
import sg.mirobotic.zoom.data.remote.WebRepository
import sg.mirobotic.zoom.zoom.JoinMeetingHelper
import us.zoom.sdk.*
import us.zoom.sdk.ZoomSDK

class MainViewModel: ViewModel(), ZoomSDKInitializeListener, ZoomSDKAuthenticationListener, PreMeetingServiceListener, MeetingServiceListener {

    lateinit var zoom: ZoomSDK
    private var onZoomSdkStatusListener: OnZoomSdkStatusListener? = null

    private lateinit var webRepository: WebRepository
    private lateinit var userDataProvider: UserDataProvider
    private var loginListener: ApiResponseListener<String>? = null

    var email = MutableLiveData("")
    private var password = ""

    var initZoom = MutableLiveData(false)
    var loginResult = MutableLiveData(false)

    fun init(context: Context, onZoomSdkStatusListener: OnZoomSdkStatusListener) {
        zoom = ZoomSDK.getInstance()
        val initParams = ZoomSDKInitParams().apply {
            appKey = Credentials.SDK_KEY
            appSecret = Credentials.SDK_SECRET
            domain = Credentials.SDK_DOMAIN
        }
        userDataProvider = UserDataProvider.getInstance(context)
        zoom.initialize(context, this, initParams)
        zoom.addAuthenticationListener(this)
        this.onZoomSdkStatusListener = onZoomSdkStatusListener

        Log.e(TAG,"version > ${zoom.getVersion(context)}")
        email.value = userDataProvider.getEmail()
        webRepository = WebRepository.getInstance(email)
    }

    fun login() {
        val email = userDataProvider.getEmail()
        val password = userDataProvider.getPassword()

        Log.e(TAG,"LOGIN > $email / $password")

        if (email.isNotEmpty() && password.isNotEmpty()) {
            zoom.loginWithZoom(email, password)
        }
    }

    fun setListener() {
        zoom.preMeetingService.addListener(this)
    }

    fun login(email: String, password: String, loginListener: ApiResponseListener<String>) {
        this.loginListener = loginListener
        this.email.value = email
        this.password = password
        zoom.loginWithZoom(email, password)
    }

    override fun onZoomSDKInitializeResult(p0: Int, p1: Int) {
        Log.e(TAG,"onZoomSDKInitializeResult $p0 / $p1")
        onZoomSdkStatusListener?.onZoomSDKInitializeResult(p0, p1)
    }

    override fun onZoomSDKLoginResult(result: Long) {
        Log.e(TAG,"onZoomSDKLoginResult $result")
        onZoomSdkStatusListener?.onZoomSDKLoginResult(result)
        when (result.toInt()) {
            ZoomAuthenticationError.ZOOM_AUTH_ERROR_SUCCESS -> {
                if (email.value!!.isNotEmpty() && password.isNotEmpty()) {
                    userDataProvider.saveCredentials(email.value!!, password)
                }
                loginResult.postValue(true)
                loginListener?.onSuccess("")
            }
            ZoomAuthenticationError.ZOOM_AUTH_ERROR_USER_NOT_EXIST,
            ZoomAuthenticationError.ZOOM_AUTH_ERROR_WRONG_PASSWORD -> {
                loginResult.postValue(false)
                loginListener?.onSuccess("Wrong email or password")
            }
        }
    }

    override fun onZoomSDKLogoutResult(result: Long) {
        Log.e(TAG,"onZoomSDKLogoutResult $result")
        onZoomSdkStatusListener?.onZoomSDKLogoutResult(result)
    }

    override fun onZoomIdentityExpired() {
        Log.e(TAG,"onZoomIdentityExpired")
        onZoomSdkStatusListener?.onZoomIdentityExpired()
    }

    override fun onZoomAuthIdentityExpired() {
        Log.e(TAG,"onZoomAuthIdentityExpired")
        onZoomSdkStatusListener?.onZoomAuthIdentityExpired()
    }

    fun joinMeeting(context: Context, meetingDetails: MeetingDetails) {
        Log.e(TAG,"joinMeeting $meetingDetails")

        JoinMeetingHelper().joinMeeting(
            context,
            zoom,
            meetingDetails.id.toString(),
            meetingDetails.password,
            meetingDetails.topic
        )

    }

    fun getProfile(apiResponseListener: ApiResponseListener<String>) {
        if (email.value!!.isNotEmpty()) {
            webRepository.getProfile(apiResponseListener)
        }
    }

    fun createMeetings(meetingDetails: MeetingDetails, apiResponseListener: ApiResponseListener<MeetingDetails>) {
        webRepository.addMeeting(meetingDetails, apiResponseListener)
    }

    var mMeetings = MutableLiveData<ArrayList<Meeting>>()

    fun getMeetings(apiResponseListener: ApiResponseListener<String>) {
        webRepository.getMeetings(mMeetings, apiResponseListener)
    }

    override fun onListMeeting(p0: Int, list: MutableList<Long>) {
        for (id in list) {
            Log.d(TAG,"onListMeeting $p0: $id ")
        }
    }

    override fun onScheduleMeeting(p0: Int, p1: Long) {
        Log.e(TAG,"onScheduleMeeting $p0 $p1")
    }

    override fun onUpdateMeeting(p0: Int, p1: Long) {
        Log.e(TAG,"onUpdateMeeting $p0 $p1")
    }

    override fun onDeleteMeeting(p0: Int) {
        Log.e(TAG,"onDeleteMeeting $p0")
    }

    companion object {
        private const val TAG = "MVM"
    }

    override fun onMeetingStatusChanged(p0: MeetingStatus, p1: Int, p2: Int) {
        Log.e(TAG,"onMeetingStatusChanged ${p0.name}")
        val meetingService = zoom.meetingService
        Log.e(TAG,"currentRtcMeetingID ${meetingService.currentRtcMeetingID}")
    }

    fun getMeetingDetails(id: Long, apiResponseListener: ApiResponseListener<MeetingDetails>) {
        webRepository.getMeetingDetails(id.toString(), apiResponseListener)
    }

}