package kr.co.nexters.winepick.ui.search

import android.text.Editable
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.launch
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.data.repository.SearchRepository
import kr.co.nexters.winepick.data.repository.WineRepository
import kr.co.nexters.winepick.ui.base.BaseViewModel
import timber.log.Timber

/**
 * 검색 화면 ViewModel
 *
 * @author ricky
 * @since v1.0.0 / 2021.02.06
 */
class SearchFilterViewModel : BaseViewModel() {
    val tag = this::class.java.canonicalName

}
