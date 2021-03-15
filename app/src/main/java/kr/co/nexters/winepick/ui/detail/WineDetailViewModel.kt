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
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
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

    private var _countryImage = MutableLiveData<Int>()
    var countryImage : LiveData<Int> = _countryImage

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

    private var _wineResult : MutableLiveData<WineResult> = MutableLiveData()
    var wineResult : LiveData<WineResult> = _wineResult


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
                _wineSweetness.value = it.sweetness
                _wineAcidity.value = it.acidity
                _wineBody.value = it.body
                _wineTannin.value = it.tannin
                _wineName.value = it.nmKor
                _wineResult.value = it
                wineCountryImage(_wineResult.value!!.country!!)
                wineStore(_wineResult.value!!.store!!)

            },
            onFailure = {

            }
        )
    }
    fun wineStore(store : String) {
        when(store) {
            "이마트 24" -> { _isEmart.value = true}
            "GS25" -> {_isGs.value = true }
            "CU" -> {_isCu.value = true}
            "세븐일레븐" -> {_isSeven.value = true}
        }
    }
    fun wineCountryImage(country : String) {
        when(country) {
            "미국" -> {_countryImage.value = R.drawable.us_icon}
            "이탈리아" -> {_countryImage.value = R.drawable.italy_icon}
            "남아프리카공화국" -> {_countryImage.value = R.drawable.south_africa_icon}
            "호주" -> { _countryImage.value = R.drawable.australia_icon }
            "이탈리아" -> {_countryImage.value = R.drawable.italy_icon}
            "칠레" -> {_countryImage.value = R.drawable.chile_icon}
            "스페인" -> {_countryImage.value = R.drawable.spain_icon}
            "뉴질랜드" -> {_countryImage.value = R.drawable.new_zealand_icon}
            "캐나다" -> {_countryImage.value = R.drawable.canada_icon}
            "프랑스" -> {_countryImage.value = R.drawable.france_icon}
            "오스트리아" -> {_countryImage.value = R.drawable.australia_icon}
            "독일" -> {_countryImage.value = R.drawable.germany_icon}
            "포루투갈" -> {_countryImage.value = R.drawable.portugal_icon}
            "아르헨티나" -> {_countryImage.value = R.drawable.argentina_icon}

        }

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
