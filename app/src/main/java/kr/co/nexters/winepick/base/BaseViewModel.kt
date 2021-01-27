package kr.co.nexters.winepick.base

import androidx.lifecycle.ViewModel

/**
 * BaseViewModel
 *
 * @since v1.0.0 / 2020.12.29
 */
open class BaseViewModel : ViewModel() {
    /** 생성자 개념으로 생각하면 편할듯 */
    init {

    }

    /** UI 의 onDestroy 개념으로 생각하면 편할듯 */
    override fun onCleared() {
        super.onCleared()
    }
}
