package kr.co.nexters.winepick.ui.type

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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


    /** 생성자 */
    init {
        _isUser.value = false
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

    fun setUserPersonalType() {
        return when(auth.testType) {
            "A" -> {getUserType(TestConstant.A)}
            "B" -> {getUserType(TestConstant.B)}
            "C" -> {getUserType(TestConstant.C)}
            "D" -> {getUserType(TestConstant.D)}
            "E" -> {getUserType(TestConstant.E)}
            "F" -> {getUserType(TestConstant.F)}
            else -> {}

        }
    }
    fun getUserType(resultId : Int) {
        repo.getResult(
            resultId = resultId,
            onSuccess = {
                Timber.e("ddd")
                _typeName.value = it.personDetail + ", " + it.personName
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
