package kr.co.nexters.winepick.data.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*


data class getUserData(
    @SerializedName("id")
    val id : Int? = null,
    @SerializedName("likes")
    val likes : Int? = null,
    @SerializedName("personalityType")
    val personalityType : String? = null
)