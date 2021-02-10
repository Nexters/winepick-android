package kr.co.nexters.winepick.data.constant

/**
 * WinePick 앱에서 사용하는 상수 모음

 * @since v1.0.0 / 2021.02.09
 */
object Constant {
    const val PREF_KEY_SEARCH_CURRENT = "PREF_KEY_SEARCH_CURRENT"
}

/**
 * 서버에서 지정한 모델을 아직 반영하지 않은 경우에는
 * 해당 Data class 에 Annotation 을 붙여준다.
 */
@RequiresOptIn(
    message = "해당 데이터 class 내 property 는 서버에서 주는 응답값에 따라 변경될 수 있음",
    level = RequiresOptIn.Level.WARNING
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class DependencyServer
