package kr.co.nexters.winepick.util.ext

import android.R.string
import android.os.Build
import android.text.Html
import android.text.Spanned
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * String 확장함수
 *
 * @since v1.0.0 / 2021.02.10
 */
fun String.htmlStyling(): Spanned {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        Html.fromHtml(this)
    } else Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
}

fun String.htmlUnStyling(): String {
    val tagsMatcher: Matcher = Pattern.compile("<.+?>").matcher(this)
    return tagsMatcher.replaceAll("")
}
