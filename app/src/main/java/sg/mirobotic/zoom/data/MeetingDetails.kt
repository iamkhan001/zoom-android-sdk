package sg.mirobotic.zoom.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Meeting(
    @SerializedName("uuid") val uuid: String,
    @SerializedName("id") val id: Long,
    @SerializedName("host_id") val hostId: String,
    @SerializedName("topic") val topic: String,
    @SerializedName("type") val type: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("duration") val duration: String,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("agenda") val agenda: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("pmi") val pmi: String,
): Serializable

data class MeetingDetails(
    @SerializedName("topic") val topic: String,
    @SerializedName("type") val type: Int,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("duration") val duration: String,
    @SerializedName("schedule_for") val scheduleFor: String,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("password") val password: String,
    @SerializedName("agenda") val agenda: String,
    @SerializedName("settings") val settings: Settings
): Serializable {

    @SerializedName("created_at") var createdAt = ""
    @SerializedName("host_id") var hostId = ""
    @SerializedName("host_email") var hostEmail = ""
    @SerializedName("id") var id = 0L
    @SerializedName("join_url") var joinUrl = ""
    @SerializedName("start_url") var startUrl = ""
    @SerializedName("status") var status = ""
    @SerializedName("uuid") var uuid = ""
    @SerializedName("breakout_room") var breakoutRoom: BreakoutRoom? = null

    override fun toString(): String = "Meeting: $id $uuid $topic $password $startTime $duration"
}

data class Settings(
    @SerializedName("host_video") val hostVideo: Boolean,
    @SerializedName("participant_video") val participantVideo: Boolean,
    @SerializedName("cn_meeting") val cnMeeting: Boolean,
    @SerializedName("in_meeting") val inMeeting: Boolean,
    @SerializedName("join_before_host") val joinBeforeHost: Boolean,
    @SerializedName("mute_upon_entry") val muteUponEntry: Boolean,
    @SerializedName("watermark") val watermark: Boolean,
    @SerializedName("use_pmi") val usePmi: Boolean,
    @SerializedName("registrants_email_notification") val registrantsEmailNotification: Boolean,
): Serializable {

    companion object {
        fun getDefaultSettings(): Settings = Settings(
            hostVideo = true,
            participantVideo = true,
            cnMeeting = false,
            inMeeting = false,
            joinBeforeHost = true,
            muteUponEntry = false,
            watermark = false,
            usePmi = true,
            registrantsEmailNotification = true
        )
    }

}

data class BreakoutRoom(
    @SerializedName("enable") val enable: Boolean,
    @SerializedName("host_video") val host_video: Boolean,
    @SerializedName("in_meeting") val in_meeting: Boolean,
    @SerializedName("join_before_host") val join_before_host: Boolean,
    @SerializedName("mute_upon_entry") val mute_upon_entry: Boolean,
    @SerializedName("registrants_confirmation_email") val registrants_confirmation_email: Boolean,
    @SerializedName("use_pmi") val use_pmi: Boolean,
    @SerializedName("waiting_room") val waiting_room: Boolean,
    @SerializedName("watermark") val watermark: Boolean,
    @SerializedName("registrants_email_notification") val registrants_email_notification: Boolean,
    @SerializedName("rooms") val rooms: Room,
    )

data class Room (
    @SerializedName("name") val name: String,
    @SerializedName("participants") val participants: ArrayList<String>,
)

class MeetingTypes {
    companion object {
        const val MEETING_TYPE_INSTANT = 1
        const val MEETING_TYPE_SCHEDULE = 2
        const val MEETING_TYPE_RECURRING_NO_FIX_TIME = 3
        const val MEETING_TYPE_RECURRING_WITH_FIX_TIME = 8
    }
}