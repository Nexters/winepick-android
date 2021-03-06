package kr.co.nexters.winepick.constant

/**
 * WinePick 앱에서 사용하는 상수 모음

 * @since v1.0.0 / 2021.02.09
 */
object Constant {
    // Default
    const val PREF_KEY_WINE_INFOS = "PREF_KEY_WINE_INFOS"
    const val PREF_KEY_USER_VIEW_WINES = "PREF_KEY_USER_VIEW_WINES"
    const val PREF_KEY_USER_SURVEYS = "PREF_KEY_USER_SURVEYS"

    const val PREF_KEY_INT_USER_SURVEY_RESULT = "PREF_KEY_INT_USER_SURVEY_RESULT"

    // extra
    const val BOOL_EXTRA_SEARCH_NEED_UPDATE = "BOOL_EXTRA_SEARCH_NEED_UPDATE"
    const val BOOL_EXTRA_SURVEY_RESET = "BOOL_EXTRA_SURVEY_RESET"

    const val STRING_EXTRA_SEARCH_FILTERS = "STRING_EXTRA_SEARCH_QUERYS"
    const val STRING_EXTRA_SEARCH_FILTERS_FROM_HOME = "STRING_EXTRA_SEARCH_FILTERS_FROM_HOME"
    const val STRING_EXTRA_WEB_URL_PARAMS = "STRING_EXTRA_WEB_URL_PARAMS"

    const val STRING_EXTRA_DEEP_LINK_PERSONALITY_TYPE = "STRING_EXTRA_DEEP_LINK_PERSONALITY_TYPE"

    // RequestCode
    const val REQ_CODE_GO_TO_FILTER = 1
    const val REQ_CODE_GO_TO_DETAIL = 2
    const val REQ_CODE_GO_TO_PERSONALITY_TYPE = 3
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
