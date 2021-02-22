package kr.co.nexters.winepick.util

import com.kakao.sdk.template.model.*

class KakaoLink {
    val defaultFeed = FeedTemplate(
        content = Content(
            title = "딸기 치즈 케익",
            description = "#케익 #딸기 #삼평동 #카페 #분위기 #소개팅",
            imageUrl = "http://mud-kage.kakao.co.kr/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png",
            link = Link(
                webUrl = "https://developers.kakao.com",
                mobileWebUrl = "https://developers.kakao.com"
            )
        ),
        social = Social(
            likeCount = 286,
            commentCount = 45,
            sharedCount = 845
        ),
        buttons = listOf(
            Button(
                "웹으로 보기",
                Link(
                    webUrl = "https://developers.kakao.com",
                    mobileWebUrl = "https://developers.kakao.com"
                )
            ),
            Button(
                "앱으로 보기",
                Link(
                    androidExecParams = mapOf("key1" to "value1", "key2" to "value2"),
                    iosExecParams = mapOf("key1" to "value1", "key2" to "value2")
                )
            )
        )
    )
}