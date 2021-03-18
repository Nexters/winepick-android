package kr.co.nexters.winepick.ui.home

import android.content.Intent
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
import kr.co.nexters.winepick.data.repository.WinePickRepository
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseViewModel
import kr.co.nexters.winepick.ui.like.LikeListActivity
import kr.co.nexters.winepick.ui.search.SearchActivity
import kr.co.nexters.winepick.ui.survey.SurveyActivity
import kr.co.nexters.winepick.ui.type.TypeDetailActivity

/**
 * Kotlin 에서 사용하는 ViewModel 예
 *
 * @since v1.0.0 / 2021.01.28
 */
class HomeViewModel(private val repo: WinePickRepository, private val auth: AuthManager) : BaseViewModel() {
    private var _likecnt = MutableLiveData<String>()
    var likeCnt : LiveData<String> = _likecnt

    private var _isTest : MutableLiveData<Boolean> = MutableLiveData()
    val isTest : LiveData<Boolean>
        get() = _isTest

    private var _isUser : MutableLiveData<Boolean> = MutableLiveData()
    val isUser : LiveData<Boolean>
        get() = _isUser

    private var _loginWarningDlg : MutableLiveData<Boolean> = MutableLiveData()
    val loginWarningDlg : LiveData<Boolean> = _loginWarningDlg

    private var _keyword1 = MutableLiveData<String>()
    var keyword1 : LiveData<String> = _keyword1
    private var _keyword2 = MutableLiveData<String>()
    var keyword2 : LiveData<String> = _keyword2

    private var _testImg = MutableLiveData<Int>()
    var testImg : LiveData<Int> = _testImg

    lateinit var keywordList1 : Array<String>
    lateinit var keywordList2: Array<String>


    /** 생성자 */
    init {
        showLoading()
        _likecnt.value = "0"
        _isTest.value = false
        _isUser.value = false
        _loginWarningDlg.value = false
        _testImg.value = 0
        if (auth.testType != "N") {
            _isTest.value = true
            setUserPersonalType()
        }
        getUserLikes()
    }
    fun getUserLikes(){
        showLoading()
        if (auth.token != "guest") {
            _isUser.value = true
            repo.getUser(
                    userId = auth.id,
                    accessToken = auth.token,
                    onSuccess = {
                        if (it.likes!! > 99) {
                            _likecnt.value = "99+"
                        }
                        _likecnt.value = it.likes.toString()
                        hideLoading()
                    },
                    onFailure = {
                        hideLoading()
                    }
            )
        }


    }



    fun setUserPersonalType() {
        return when(auth.testType) {
            "A" -> {
                getUserType(A)
                _testImg.value = R.drawable.img_test_a
            }
            "B" -> {
                getUserType(B)
                _testImg.value = R.drawable.img_test_b
            }
            "C" -> {
                getUserType(C)
                _testImg.value = R.drawable.img_test_c
            }
            "D" -> {
                getUserType(D)
                _testImg.value = R.drawable.img_test_d
            }
            "E" -> {
                getUserType(E)
                _testImg.value = R.drawable.img_test_e
            }
            "F" -> {
                getUserType(F)
                _testImg.value = R.drawable.img_test_f
            }
            else -> {}

        }
    }
    fun getUserType(resultId: Int) {
        showLoading()
        repo.getResult(
                resultId = resultId,
                onSuccess = {
                    val tempKeyword1 = it.keyword1.split(",")
                    val tempKeyword2 = it.keyword2.split(",")
                    _keyword1.value = tempKeyword1[0]!!
                    _keyword2.value = tempKeyword2[0]!!
                    keywordList1 = tempKeyword1.subList(1, tempKeyword1.size).toTypedArray()
                    keywordList2 = tempKeyword2.subList(1, tempKeyword2.size).toTypedArray()

                    hideLoading()
                },
                onFailure = {
                    hideLoading()
                }
        )

    }
    fun keyword1Click() {
        Intent(WinePickApplication.appContext, SearchActivity::class.java).apply {
            putExtra("keyword",keywordList1)
        }.run {
            WinePickApplication.getGlobalApplicationContext().startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }

    }
    fun keyword2Click() {
        Intent(WinePickApplication.appContext, SearchActivity::class.java).apply {
            putExtra("keyword",keywordList2)
        }.run {
            WinePickApplication.getGlobalApplicationContext().startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }

    }
    fun testClick() {
        WinePickApplication.getGlobalApplicationContext().startActivity(Intent(WinePickApplication.appContext, SurveyActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    fun searchClick() {
        WinePickApplication.getGlobalApplicationContext().startActivity(Intent(WinePickApplication.appContext, SearchActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
    fun likeClick() {
        if(isUser.value!!) {
            WinePickApplication.getGlobalApplicationContext().startActivity(
                    Intent(
                            WinePickApplication.appContext,
                            LikeListActivity::class.java
                    )
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        } else {
            _loginWarningDlg.value = true
        }
    }
    fun myTypeClick() {
        WinePickApplication.getGlobalApplicationContext().startActivity(Intent(WinePickApplication.appContext,
                TypeDetailActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
    /** UI 의 onDestroy 개념으로 생각하면 편할듯 */
    override fun onCleared() {
        super.onCleared()
    }

    override fun onResume() {
        super.onResume()
        getUserLikes()
    }

}
