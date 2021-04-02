package kr.co.nexters.winepick.data.model.remote.user

import com.google.gson.annotations.SerializedName

data class UserResult(
    @SerializedName("accessToken")
    val accessToken: String?,

    @SerializedName("id")
    val id: Int?,

    @SerializedName("likes")
    val likes: Int?,

    @SerializedName("personality")
    val personality: String?,

    @SerializedName("userId")
    val userId: Int?
)
