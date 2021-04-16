package kr.co.nexters.winepick.util.ext

import androidx.core.content.ContextCompat
import kr.co.nexters.winepick.WinePickApplication

/**
 * Resource Int 확장함수
 *
 * @since v1.0.0 / 2021.02.24
 */

/** [Res Int 값][Int] 을 기반으로 색상값을 가져온다. */
fun Int.getColor() = ContextCompat.getColor(WinePickApplication.getGlobalApplicationContext(), this)
