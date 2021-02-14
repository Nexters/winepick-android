package kr.co.nexters.winepick.ui.base

import android.app.Activity
import android.content.Intent

/**
 * [Activity.startActivityForResult]로 실행 후 결과 data class
 *
 * @since v1.0.0 / 2021.02.11
 */
data class ActivityResult(val resultCode: Int, val data: Intent?)
