package kr.co.nexters.winepick.ui.login

import android.content.Context
import com.kakao.sdk.common.util.Utility

/**
 * 카카오에서 해시키를 뽑아오기 위해 실행하는 로직
 *
 * @since v1.0.0 / 2021.02.08
 */
fun getKakaoHashKey(context: Context) : String = Utility.getKeyHash(context)
