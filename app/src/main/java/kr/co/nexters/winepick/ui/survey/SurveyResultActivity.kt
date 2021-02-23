package kr.co.nexters.winepick.ui.survey

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.link.LinkClient
import com.kakao.sdk.template.model.*
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.util.KakaoLink

class SurveyResultActivity : AppCompatActivity() {

    private val TAG = "kakao link"
    private val defaultFeed = FeedTemplate(
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey_result)

        val shareImgBtn : ImageButton = findViewById(R.id.share_button)
        shareImgBtn.setOnClickListener {
            Log.i("Share OnClick", "Share OnClick")

            // 피드 메시지 보내기
            LinkClient.instance.defaultTemplate(this, defaultFeed) { linkResult, error ->
                if (error != null) {
                    Log.e(TAG, "카카오링크 보내기 실패", error)
                }
                else if (linkResult != null) {
                    Log.d(TAG, "카카오링크 보내기 성공 ${linkResult.intent}")
                    startActivity(linkResult.intent)

                    // 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                    Log.w(TAG, "Warning Msg: ${linkResult.warningMsg}")
                    Log.w(TAG, "Argument Msg: ${linkResult.argumentMsg}")
                }
            }
        }
    }

}