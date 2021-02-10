package kr.co.nexters.winepick.ui.login

import android.content.Context
import com.kakao.sdk.common.util.Utility


fun getKakaoHashKey(context: Context) : String? = Utility.getKeyHash(context)
