package sg.mirobotic.zoom.data.remote

interface ApiResponseListener<T> {

    fun onData(data: T){
        onSuccess("")
    }

    fun onSuccess(msg: String)

    fun onError(msg: String)

}