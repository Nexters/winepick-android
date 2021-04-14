package kr.co.nexters.winepick.data.model

/**
 * addUser Request Data
 */
data class AccessTokenData (
    val accessToken : String,
    val personalityType: String?,
    val userId : Long
)
