package kr.co.nexters.winepick.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.data.repository.WinePickRepository
import timber.log.Timber

/**
 * wineResult 목록을 다루어야 할 시 상속받아야 하는 ViewModel
 * wineResult 에 대한 관리 변수 및 함수들을 관리하고 있으며 추상클래스로 두어야 한다.
 *
 * @since v1.0.0 / 2021.02.24
 */
abstract class WineResultViewModel constructor(
    winePickRepository: WinePickRepository
) : BaseViewModel(winePickRepository) {
    /** 검색 결과 list */
    protected val _results = MutableLiveData<List<WineResult>>(listOf())
    open val results: LiveData<List<WineResult>> = _results

    private val _wineImg = MutableLiveData<Int>()
    val wineImg : LiveData<Int> = _wineImg

    /** wineResult 아이템뷰를 클릭시 동작하는 로직 */
    abstract fun wineItemViewClick(wineResult: WineResult)

    /**
     * wineResult 내 heartView 를 클릭 시 동작하는 로직
     *
     * @param newWineResult 좋아요 버튼을 클릭하면 얻게 될 [WineResult] 데이터 값
     */
    abstract fun wineHeartClick(newWineResult: WineResult)

    /** [_results] 내 특정 아이템을 추가한다. */
    fun addWineResult(wineResult: WineResult) {
        val results: MutableList<WineResult> = _results.value?.toMutableList() ?: mutableListOf()

        results.add(wineResult)

        _results.value = results
    }

    /** wineImage 처리 **/
     fun wineImage() {

     }

    /** [_results] 내 특정 아이템을 [wineResult] 변경한다. */
    fun replaceWineResult(wineResult: WineResult) {
        val results: MutableList<WineResult> = _results.value?.toMutableList() ?: return

        val prevResult = (results.filter { wineResult.id == it.id }).firstOrNull()

        prevResult?.let {
            val replaceIndex = results.indexOf(it)

            if (replaceIndex == -1) {
                Timber.i("$it isn't exist")
                return
            } else {
                results.removeAt(replaceIndex)
                results.add(replaceIndex, wineResult)
                _results.value = results
            }
        }
    }

    /** [_results] 내 특정 아이템의 좋아요 클릭 여부 내용만 바꿔준다. */
    fun toggleWineResult(prevWineResult: WineResult) {
        val results: MutableList<WineResult> = _results.value?.toMutableList() ?: return

        val replaceIndex = results.indexOf(prevWineResult)

        if (replaceIndex == -1) {
            Timber.i("$prevWineResult isn't exist")
            return
        } else {
            results.removeAt(replaceIndex)
            results.add(replaceIndex, prevWineResult.copy(likeYn = prevWineResult.clickedLikeYn))
            _results.value = results
        }
    }

    /** [_results] 내 특정 아이템을 삭제한다. */
    fun deleteWineResult(wineResult: WineResult) {
        val results: MutableList<WineResult> = _results.value?.toMutableList() ?: return

        val deleteIndex = results.indexOf(wineResult)

        if (deleteIndex == -1) {
            Timber.i("$wineResult isn't exist")
            return
        } else {
            results.removeAt(deleteIndex)
            _results.value = results
        }
    }
}
