package kr.co.nexters.winepick.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.data.model.LikeWine
import kr.co.nexters.winepick.data.model.WineFood
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.data.model.remote.wine.WineValue
import kr.co.nexters.winepick.data.repository.WinePickRepository
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseViewModel
import timber.log.Timber
import java.lang.StringBuilder

/**
 * WineDetailViewModel
 *
 * @since v1.0.0 / 2021.01.28
 */
class WineDetailViewModel(private val winePickRepository: WinePickRepository, private val authManager: AuthManager) : BaseViewModel() {
    private var _backButton = MutableLiveData<Boolean>()
    var backButton : LiveData<Boolean> = _backButton

    private var _countryImage = MutableLiveData<Int>()
    var countryImage : LiveData<Int> = _countryImage

    private val _wineName = MutableLiveData<String>()
    val wineName : LiveData<String> = _wineName

    private var _isFeeling : MutableLiveData<Boolean> = MutableLiveData()
    val isFeeling : LiveData<Boolean> = _isFeeling

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

    private var _winePurpose : MutableLiveData<String> = MutableLiveData()
    var winePurpose : LiveData<String> = _winePurpose

    private var _wineFoodSize : MutableLiveData<Int> = MutableLiveData()
    var wineFoodSize : LiveData<Int> = _wineFoodSize

    private var _wineFoodList : MutableLiveData<String> = MutableLiveData()
    var wineFoodList : LiveData<String> = _wineFoodList

    /** 좋아요 토스트 **/
    val _toastMessage = MutableLiveData<Boolean>()
    var toastMessage: LiveData<Boolean> = _toastMessage

    /** 취소 토스트 **/
    val _cancelMessage = MutableLiveData<Boolean>()
    var cancelMessage : LiveData<Boolean> = _cancelMessage

    private var _loginWarningDlg : MutableLiveData<Boolean> = MutableLiveData()
    val loginWarningDlg : LiveData<Boolean> = _loginWarningDlg

    /** 생성자 */
    init {
        showLoading()
        _toastMessage.value = false
        _cancelMessage.value = false
        _backButton.value = false
        _isCu.value = false
        _isGs.value = false
        _isEmart.value = false
        _isSeven.value = false
        _isFeeling.value = false
        _wineFoodSize.value = 0
        _loginWarningDlg.value = false


    }
    fun getWineResult(wineResult: WineResult) {
        val tmpWineFoods : MutableList<WineFood> = mutableListOf()
        val tmpWineFeatures : MutableList<WineFood> = mutableListOf()

        _wineResult.value = wineResult
        _wineLike.value = wineResult.likeYn ?: false
        _wineName.value = _wineResult.value?.nmKor ?: ""
        _wineResult.value?.feeling?.let { _isFeeling.value = true }
        wineCountryImage(_wineResult.value?.country ?: "")
        wineStore(_wineResult.value?.store)

        val winePurposeData = _wineResult.value!!.purpose!!.split(",")

        tmpWineFeatures.add(WineFood(title = _wineResult.value?.category ?: "", img = null))
        for (str in winePurposeData) {
            tmpWineFeatures.add(WineFood(title = str, img = null))
        }
        _wineFeature.value = tmpWineFeatures
        var purposeStr = ""
        for (index in winePurposeData.indices) {
            purposeStr += when {
                winePurposeData[index] == "디저트 와인" -> "디저트"
                winePurposeData[index] == "테이블 와인" -> "테이블"
                else -> "에피타이저"
            }
            if (index != winePurposeData.size - 1) {
                purposeStr += (", ")
            }
        }
        _winePurpose.value = purposeStr
        val wineFoods = _wineResult.value?.suitFood?.split(", ") ?: listOf()
        for (str in wineFoods) {
            val imgRes = winePickRepository.getResourceFromFoodName(str)
            tmpWineFoods.add(WineFood(title = str, img = imgRes))
        }
        _wineFood.value = tmpWineFoods
        _wineFoodSize.value = tmpWineFoods.size

        val windFoodListStr = StringBuilder()
        tmpWineFoods.forEach {
            windFoodListStr.append("${it.title}, ")
        }
        _wineFoodList.value = windFoodListStr.removeSuffix(", ").toString()

        val context = WinePickApplication.appContext!!
        _wineValue.value = mutableListOf<WineValue>().apply {
            _wineResult.value?.sweetness?.let { add(WineValue("당도",context.getString(R.string.sweetness_detail),it,"높음","낮음",false)) }
            _wineResult.value?.acidity?.let { add(WineValue("산도",context.getString(R.string.acidity_detail),it,"높음","낮음",false)) }
            _wineResult.value?.body?.let { add(WineValue("바디",context.getString(R.string.body_detail),it,"무거움","가벼움",false)) }
            _wineResult.value?.tannin?.let { add(WineValue("타닌",context.getString(R.string.tannin_detail),it,"많음","적음",false)) }
        }

        hideLoading()
    }
    fun wineStore(store : String?) {
        when(store) {
            "이마트 24" -> { _isEmart.value = true}
            "GS25" -> {_isGs.value = true }
            "CU" -> {_isCu.value = true}
            "세븐일레븐" -> {_isSeven.value = true}
            else -> {return}
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

    fun likeClick() {
        if (_wineLike.value == true) {
            wineResult.value?.let { cancelLike(it) }
        } else {
            wineResult.value?.let { addLike(it) }
        }
    }

    fun addLike(wineResult: WineResult) {
        if(authManager.token == "guest") {
            _loginWarningDlg.value = true
        } else {
            winePickRepository.postLike(
                    data = LikeWine(
                            userId = authManager.id,
                            wineId = wineResult.id ?: -1
                    ),
                    onSuccess = {
                        Timber.d("와인 좋아요 저장 성공")
                        _toastMessage.value = true
                        _wineLike.value = true
                        hideLoading()
                    },
                    onFailure = {
                        Timber.d("와인 좋아요 저장 실패")
                        hideLoading()
                    }
            )
        }

    }

    /** 좋아요 취소 서버 통신 - deleteLike */
    fun cancelLike(wineResult: WineResult) {
        if (authManager.token == "guest") {
            _loginWarningDlg.value = true
        } else {
            winePickRepository.deleteLike(
                    wineId = wineResult.id ?: -1,
                    userId = authManager.id,
                    onSuccess = {
                        Timber.d("와인 좋아요 취소 성공")
                        _cancelMessage.value = true
                        _wineLike.value = false
                        hideLoading()
                    },
                    onFailure = {
                        Timber.d("와인 좋아요 취소 실패")
                        hideLoading()
                    }
            )
        }
    }

    fun backClick() {
        _backButton.value = true
    }

    /** UI 의 onDestroy 개념으로 생각하면 편할듯 */
    override fun onCleared() {
        super.onCleared()
    }
}
