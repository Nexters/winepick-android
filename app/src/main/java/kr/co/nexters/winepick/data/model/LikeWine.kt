package kr.co.nexters.winepick.data.model

import com.google.gson.annotations.SerializedName

/**
 * addLike Request Data
 */
data class LikeWine(
    @SerializedName("userId")
    val userId : Int,
    @SerializedName("wineId")
    val wineId : Int
)