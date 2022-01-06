package sg.mirobotic.zoom.data.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import sg.mirobotic.zoom.data.Meeting
import sg.mirobotic.zoom.data.MeetingDetails

class FirestoreRepository() {

    private val db = Firebase.firestore

    companion object {

        private const val MEETINGS = "meetings"
        private const val INSTANT_MEETINGS = "instantMeeting"
        private const val TAG = "Repository"

        private var FirestoreRepository: FirestoreRepository? = null

        fun getInstance(): FirestoreRepository {

            if (FirestoreRepository == null) {
                FirestoreRepository = FirestoreRepository()
            }

            return FirestoreRepository!!
        }

    }

    fun saveMeeting(meetingDetails: MeetingDetails, apiResponseListener: ApiResponseListener<Any>?) {
        db.collection(MEETINGS)
            .add(meetingDetails)
            .addOnSuccessListener { documentReference ->
                apiResponseListener?.onSuccess("Meeting added successfully")
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                apiResponseListener?.onSuccess("Failed to save meeting")
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun getMeetings(mMeetings: MutableLiveData<ArrayList<MeetingDetails>>, apiResponseListener: ApiResponseListener<String>){

        db.collection(MEETINGS)
            .get()
            .addOnSuccessListener { result ->
                val meetings = ArrayList<MeetingDetails>()
                for (document in result) {
                    val meeting = document.toObject(MeetingDetails::class.java)
                    meetings.add(meeting)
                }
                Log.e(TAG,"Meetings > ${meetings.size}")
                mMeetings.postValue(meetings)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                apiResponseListener.onSuccess("Failed to get meetings")
            }
    }

    fun saveInstantMeeting(meetingDetails: MeetingDetails) {
        db.collection(MEETINGS)
            .document(INSTANT_MEETINGS)
            .set(meetingDetails)
            .addOnSuccessListener { result ->
                Log.e(TAG,"save meeting > $result")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error save meeting.", exception)
            }

    }

    fun getInstantMeetingUpdates(apiResponseListener: ApiResponseListener<MeetingDetails>) {
        db.collection(MEETINGS)
            .document(INSTANT_MEETINGS)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e(TAG,"INSTANT_MEETINGS > ${error.message}")
                    return@addSnapshotListener
                }

                if (value != null && value.data != null) {

                    val meeting = value.toObject(MeetingDetails::class.java)
                    meeting?.let {
                        apiResponseListener.onData(meeting)
                    }
                }

            }

    }

}