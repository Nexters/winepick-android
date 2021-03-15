package kr.co.nexters.winepick.ui.detail

import android.content.Intent
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.constant.TestConstant.A
import kr.co.nexters.winepick.constant.TestConstant.B
import kr.co.nexters.winepick.constant.TestConstant.C
import kr.co.nexters.winepick.constant.TestConstant.D
import kr.co.nexters.winepick.constant.TestConstant.E
import kr.co.nexters.winepick.constant.TestConstant.F
import kr.co.nexters.winepick.data.model.WineFood
import kr.co.nexters.winepick.data.model.remote.wine.WineValue
import kr.co.nexters.winepick.data.repository.WinePickRepository
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseViewModel
import kr.co.nexters.winepick.ui.like.LikeListActivity
import kr.co.nexters.winepick.ui.search.SearchActivity
import kr.co.nexters.winepick.ui.survey.SurveyActivity
import kr.co.nexters.winepick.ui.type.TypeDetailActivity
import kr.co.nexters.winepick.util.getNetworkConnected
import timber.log.Timber

/**
 * Kotlin 에서 사용하는 ViewModel 예
 *
 * @since v1.0.0 / 2021.01.28
 */
class WineDetailViewModel(private val winePickRepository: WinePickRepository) : BaseViewModel() {
    private var _backButton = MutableLiveData<Boolean>()
    var backButton : LiveData<Boolean> = _backButton

    private var _wineId = MutableLiveData<Int>()
    var wineId : LiveData<Int> = _wineId

    private val _wineKor = MutableLiveData<String>()
    val wineKor : LiveData<String> = _wineKor

    private val _wineEng = MutableLiveData<String>()
    val wineEng : LiveData<String> = _wineEng

    private val _wineCountry = MutableLiveData<String>()
    val wineCountry : LiveData<String> = _wineCountry

    private val _winePrice = MutableLiveData<String>()
    val winePrice : LiveData<String> = _winePrice

    private val _wineName = MutableLiveData<String>()
    val wineName : LiveData<String> = _wineName

    private val _wineSweetness = MutableLiveData<Int>()
    val wineSweetness : LiveData<Int> = _wineSweetness

    private val _wineAcidity = MutableLiveData<Int>()
    val wineAcidity : LiveData<Int> = _wineAcidity

    private val _wineBody = MutableLiveData<Int>()
    val wineBody : LiveData<Int> = _wineBody

    private val _wineTannin = MutableLiveData<Int>()
    val wineTannin : LiveData<Int> = _wineTannin

    private val _wineFeeling = MutableLiveData<String>()
    val wineFeeling : LiveData<String> = _wineFeeling

    private var _isCu : MutableLiveData<Boolean> = MutableLiveData()
    val isCu : LiveData<Boolean> = _isCu


    private var _isEmart : MutableLiveData<Boolean> = MutableLiveData()
    val isEmart : LiveData<Boolean> = _isEmart


    private var _isSeven : MutableLiveData<Boolean> = MutableLiveData()
    val isSeven : LiveData<Boolean> = _isSeven


    private var _isGs : MutableLiveData<Boolean> = MutableLiveData()
    val isGs : LiveData<Boolean> = _isGs

    private var _wineFood : MutableLiveData<List<WineFood>> = MutableLiveData()
    val wineFood : LiveData<List<WineFood>> = _wineFood

    private var _wineFeature : MutableLiveData<List<WineFood>> = MutableLiveData()
    val wineFeature : LiveData<List<WineFood>> = _wineFeature

    private var _wineValue : MutableLiveData<List<WineValue>> = MutableLiveData()
    val wineValue : LiveData<List<WineValue>> = _wineValue

    private var _wineLike : MutableLiveData<Boolean> = MutableLiveData()
    var wineLike : LiveData<Boolean> = _wineLike


    /** 생성자 */
    init {
        showLoading()
        _backButton.value = false
        _isCu.value = false
        _isGs.value = false
        _isEmart.value = false
        _isSeven.value = false

    }
    fun getWineResult() {
        winePickRepository.getWine(wineId= wineId.value!!,
            onSuccess = {
                _wineFeeling.value = it.feeling
                _winePrice.value = it.price.toString() + "원"
                _wineCountry.value = it.country
                _wineKor.value = it.nmKor
                _wineEng.value = it.nmEng
                _wineSweetness.value = it.sweetness
                _wineAcidity.value = it.acidity
                _wineBody.value = it.body
                _wineTannin.value = it.tannin
                _wineName.value = it.nmKor

            },
            onFailure = {

            }
        )
    }

    fun wineImageCheck() {
        //TODO: 와인 이미지 확인하기 클릭버튼
    }

    fun getWineId(wineId : Int) {
        _wineId.value = wineId
        getWineResult()
    }

    fun backClick() {
        _backButton.value = true
    }

    /** UI 의 onDestroy 개념으로 생각하면 편할듯 */
    override fun onCleared() {
        super.onCleared()
    }

    override fun onResume() {
        super.onResume()

    }

}
