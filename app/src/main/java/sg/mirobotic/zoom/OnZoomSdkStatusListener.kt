package sg.mirobotic.zoom

interface OnZoomSdkStatusListener {

    fun onZoomSDKInitializeResult(p0: Int, p1: Int)
    fun onZoomSDKLoginResult(result: Long)
    fun onZoomSDKLogoutResult(result: Long)
    fun onZoomIdentityExpired()
    fun onZoomAuthIdentityExpired()

}