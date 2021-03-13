package kr.co.nexters.winepick.data.model

import kr.co.nexters.winepick.data.response.PersonalityType

/**
 * addUser Request Data
 */
data class AccessTokenData (
    val accessToken : String,
    val personalityType: String?,
    val userId : Long
)