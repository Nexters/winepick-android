package kr.co.nexters.winepick.ui.type

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.constant.testConstant
import kr.co.nexters.winepick.data.repository.WinePickRepository
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseViewModel
import kr.co.nexters.winepick.ui.search.SearchActivity
import timber.log.Timber

/**
 * Kotlin 에서 사용하는 ViewModel 예
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

    /** 생성자 */
    init {
        _isUser.value = false
        if (auth.token != "guest") {
            _isUser.value = true
        }
    }
    fun searchClick() {
        WinePickApplication.getGlobalApplicationContext().startActivity(
            Intent(WinePickApplication.appContext, SearchActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }



    fun setUserPersonalType() {
        return when(auth.test_type) {
            "A" -> {getUserType(testConstant.A)}
            "B" -> {getUserType(testConstant.B)}
            "C" -> {getUserType(testConstant.C)}
            "D" -> {getUserType(testConstant.D)}
            "E" -> {getUserType(testConstant.E)}
            "F" -> {getUserType(testConstant.F)}
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
