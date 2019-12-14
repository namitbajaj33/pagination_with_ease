package com.example.mypagination.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "inbox_msg", indices = arrayOf(
    )
)
data class InboxMsg(
    @PrimaryKey(autoGenerate = true)
    var primaryId: Long,
    var id: Int,
    var fromId: Int,
    var fromName: String,
    var fromFirmName: String,
    var message: String,
    var timestamp: Long,

    var file: List<String>? = listOf(),

    var createdAt: String,
    var isRead: Int?,
    var isSelf: Boolean,
    var unreadCount: Int?,
    var enquiryGroupId: Int = 0,

    @SerializedName("toData")
    @Expose
    var toData: List<ToInfo>? = listOf(),

    @Embedded
    @Expose
    @SerializedName("replyInfo")
    var reply: ReplyInfo?,

    @Expose
    @SerializedName("shareStickersInfo")
    var shareSticker: List<ShareInfo>? = listOf(),

    var messageInfoId: Int = 0,
    @SerializedName("messageId")
    var msgId: Int = 0,
    var projectId: Int = 0,
    var spaceId: String = "",
    var istickerId: Int = 0,
    var optionId: Int = 0,
    var isForwarded: Int = 0,
    var isDeleted: Int = 0,
    var isLocalDeleted: Int = 0

) {

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun toString(): String {
        return "InboxList(id=$primaryId)"
    }


    data class ToInfo(

        var toId:Int,
        var toName: String,
        var toFirmName: String,
        var toType: String

    )

    data class ReplyInfo(

        var messageId: Int?,
        @SerializedName("message")
        var messageReply: String?,
        @SerializedName("file")
        var fileReply: List<String>? = listOf(),
        @SerializedName("toUsers")
        var toUsers: List<String>? = listOf(),
        var fromUser: String?

    )

    data class ShareInfo(

        @SerializedName("id")
        var sId: Int?,
        @SerializedName("name")
        var sName: String?,
        @SerializedName("image")
        var sImage: String?,
        @SerializedName("videoId")
        var videoId: String?,
        @SerializedName("videoUrl")
        var videoUrl: String?

    )

}