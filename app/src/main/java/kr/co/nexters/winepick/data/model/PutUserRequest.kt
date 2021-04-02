package kr.co.nexters.winepick.data.model

import com.google.gson.annotations.SerializedName

data class PutUserRequest(
    @SerializedName("accessToken")
    val accessToken: String,

    @SerializedName("personalityType")
    val personalityType: String,

    @SerializedName("userId")
    val userId: Int? = null
)
