package kr.co.nexters.winepick.data.model

/**
 * postUser Request Data
 */
data class TokenInfo(
    val expiredAccessToken : String,
    val newAccessToken : String
)
