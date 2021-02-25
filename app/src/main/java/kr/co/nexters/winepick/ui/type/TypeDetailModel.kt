package kr.co.nexters.winepick.ui.type

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.constant.TestConstant
import kr.co.nexters.winepick.data.repository.WinePickRepository
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseViewModel
import kr.co.nexters.winepick.ui.search.SearchActivity
import timber.log.Timber

/**
 * TypeDetailModel
 *
 * @since v1.0.0 / 2021.01.28
 */
class TypeDetailModel(private val repo : WinePickRepository, private val auth : AuthManager) : BaseViewModel() {
    private var _typeName = MutableLiveData<String>()
    var typeName : LiveData<String> = _typeName

    private var _typeDesc = MutableLiveData<String>()
    var typeDesc : LiveData<String> = _typeDesc

    private var _isUser : MutableLiveData<Boolean> = MutableLiveData()
    val isUser : LiveData<Boolean>
        get() = _isUser

    private var _isSearch : MutableLiveData<Boolean> = MutableLiveData()
    val isSearch : LiveData<Boolean>
        get() = _isSearch

    private var _testImg : MutableLiveData<Int> = MutableLiveData()
    val testImg : LiveData<Int> = _testImg

    private var _backButton = MutableLiveData<Boolean>()
    var backButton : LiveData<Boolean> = _backButton


    /** 생성자 */
    init {
        _isUser.value = false
        _testImg.value = 0
        _backButton.value = false

        if (auth.token != "guest") {
            _isUser.value = true
        }
        _isSearch.value = false
        if (auth.recentSearch1 != null && !auth.recentSearch1.isNullOrBlank() ) {
            _isSearch.value = true
        }
    }

    fun searchClick() {
        WinePickApplication.getGlobalApplicationContext().startActivity(
            Intent(WinePickApplication.appContext, SearchActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    fun backClick() {
        _backButton.value = true
    }

    fun setUserPersonalType() {
        return when(auth.testType) {
            "A" -> {
                auth.typeName = WinePickApplication.getGlobalApplicationContext().getString(R.string.type_a_name)
                getUserType(TestConstant.A)
                _testImg.value = R.drawable.img_test_mid_a
            }
            "B" -> {
                auth.typeName = WinePickApplication.getGlobalApplicationContext().getString(R.string.type_b_name)
                getUserType(TestConstant.B)
                _testImg.value = R.drawable.img_test_mid_b
            }
            "C" -> {
                auth.typeName = WinePickApplication.getGlobalApplicationContext().getString(R.string.type_c_name)
                getUserType(TestConstant.C)
                _testImg.value = R.drawable.img_test_mid_c
            }
            "D" -> {
                auth.typeName = WinePickApplication.getGlobalApplicationContext().getString(R.string.type_d_name)
                getUserType(TestConstant.D)
                _testImg.value = R.drawable.img_test_mid_d
            }
            "E" -> {
                auth.typeName = WinePickApplication.getGlobalApplicationContext().getString(R.string.type_e_name)
                getUserType(TestConstant.E)
                _testImg.value = R.drawable.img_test_mid_e
            }
            "F" -> {
                auth.typeName = WinePickApplication.getGlobalApplicationContext().getString(R.string.type_f_name)
                getUserType(TestConstant.F)
                _testImg.value = R.drawable.img_test_mid_f
            }
            else -> {}

        }
    }
    fun getUserType(resultId : Int) {
        repo.getResult(
            resultId = resultId,
            onSuccess = {
                _typeName.value = it.personDetail + ", " + auth.typeName
                _typeDesc.value = it.description
            },
            onFailure = {

            }
        )
    }
    fun userLogout() {

    }

    fun userLogin() {

    }

    /** UI 의 onDestroy 개념으로 생각하면 편할듯 */
    override fun onCleared() {
        super.onCleared()
    }
}
