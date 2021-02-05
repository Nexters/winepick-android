package kr.co.nexters.winepick.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.launch

/**
 * BaseViewModel
 *
 * @since v1.0.0 / 2020.12.29
 */
open class BaseViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    /** 생성자 개념으로 생각하면 편할듯 */
    init {
        /**
         * viewModelScope 를 사용하면 lifecycle 을 인식하는 CoroutineScope 를 만들 수 있다.
         * viewModelScope 블록에서 실행되는 작업은 별도의 처리를 하지 않아도
         * ViewModel 이 clear 되는 순간 자동으로 취소된다.
         *
         * @see [[Android & Coroutine] ViewModelScope, LiveData Builder 사용하기](https://zion830.tistory.com/64)
         */
        viewModelScope.launch {}
    }

    /** UI 의 onDestroy 개념으로 생각하면 편할듯 */
    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    /**
     * [Disposable] 객체를 [compositeDisposable] 에 넣는다.
     * [compositeDisposable] 에 추가된 [Disposable] 들은
     * [onCleared] 단계에서 모두 clear 된다.
     */
    protected fun Disposable.addDisposable() {
        compositeDisposable.add(this)
    }
}
