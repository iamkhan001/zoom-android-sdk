package sg.mirobotic.zoom;

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import sg.mirobotic.zoom.data.remote.ApiResponseListener
import sg.mirobotic.zoom.utils.MyMessage

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var context: Context

    private val mainViewModel: MainViewModel by viewModels()

    private val apiResponseListener = object : ApiResponseListener<String> {
        override fun onSuccess(msg: String) {
            Log.d(TAG,"API SUCCESS")
        }

        override fun onError(msg: String) {
            Log.e(TAG,"API Error: $msg")
            MyMessage.showToast(context, msg)
        }
    }

    private val onZoomSdkStatusListener = object : OnZoomSdkStatusListener {

        override fun onZoomSDKInitializeResult(p0: Int, p1: Int) {
            mainViewModel.initZoom.postValue(true)
            mainViewModel.login()
        }

        override fun onZoomSDKLoginResult(result: Long) {
            mainViewModel.getProfile(apiResponseListener)
        }

        override fun onZoomSDKLogoutResult(result: Long) {
        }

        override fun onZoomIdentityExpired() {
        }

        override fun onZoomAuthIdentityExpired() {
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this
        navController = findNavController(R.id.nav_host_fragment)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.e(TAG, "screen ${destination.navigatorName} | ${destination.label} | ${destination.id}")
        }

        mainViewModel.init(context, onZoomSdkStatusListener)

    }


    companion object {
        private const val TAG = "MainActivity"
    }

}