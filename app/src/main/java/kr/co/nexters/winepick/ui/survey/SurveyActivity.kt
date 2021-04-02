package kr.co.nexters.winepick.ui.survey

import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.launch
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.data.constant.Constant
import kr.co.nexters.winepick.data.model.SurveyAnswerType
import kr.co.nexters.winepick.data.model.SurveyInfo
import kr.co.nexters.winepick.data.repository.SurveyRepository
import kr.co.nexters.winepick.databinding.ActivitySurveyBinding
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.base.BaseViewModel
import kr.co.nexters.winepick.ui.base.navigate
import kr.co.nexters.winepick.ui.component.ConfirmDialog
import kr.co.nexters.winepick.ui.type.TypeDetailActivity
import kr.co.nexters.winepick.util.SharedPrefs
import kr.co.nexters.winepick.util.dpToPx
import kr.co.nexters.winepick.util.setOnSingleClickListener
import kr.co.nexters.winepick.util.toast
import org.koin.android.ext.android.inject

class SurveyActivity : BaseActivity<ActivitySurveyBinding>(R.layout.activity_survey) {
    override val viewModel: BaseViewModel? = null

    private val surveyRepository: SurveyRepository by inject()
    private val authManager: AuthManager by inject()
    private val sharedPrefs: SharedPrefs by inject()

    /** 초기화 된 건지에 대한 유무 */
    val surveyReset: Boolean
        get() = intent.getBooleanExtra(Constant.BOOL_EXTRA_SURVEY_RESET, false)

    var startSurvey: SurveyInfo? = null
    var currentSurvey: SurveyInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 만약 설문 내역을 초기화하는 경우 처음부터 다시 시작한다.
        uiScope.launch {
            if (surveyReset) {
                surveyRepository.resetSurvey()
            }
            startSurvey = surveyRepository.getCurrentSurvey()
            currentSurvey = startSurvey

            if (startSurvey == null) onBackPressed()

            SurveyFragment(R.layout.fragment_survey).apply {
                arguments = Bundle().apply { putInt("currentStage", startSurvey!!.number) }
            }.navigate(supportFragmentManager, binding.surveyContent.id)

            // 홈버튼 클릭 시 실행되는 리스너
            binding.homeButton.setOnSingleClickListener {
                ConfirmDialog(
                    241.dpToPx(),
                    202.dpToPx(),
                    "잠깐 나갈까요?",
                    "검사로 돌아오면\n지금 질문부터 다시 시작해요.",
                    "아니요",
                    null,
                    "예",
                    { dialogFragment: DialogFragment? ->
                        onBackPressed()
                        dialogFragment?.dismiss()
                    },
                    false
                ).show(supportFragmentManager, "TestSurveyStop")
            }
        }
    }

    fun nextSurvey(answerType: SurveyAnswerType) {
        uiScope.launch {
            currentSurvey = surveyRepository.markingSurvey(answerType)

            if (currentSurvey == null || currentSurvey!!.number == -1) {
                // currentSurvey 가 null 이면 오류이므로 강제로 설문을 종료한다.
                // 만약 설문을 다 완료한 경우, 분석 화면으로 넘겨준다.
                val result = sharedPrefs[Constant.PREF_KEY_INT_USER_SURVEY_RESULT, -1] ?: 0
                toast("$result")

                authManager.testType =
                    resources.getStringArray(R.array.personality_alphabet)[result]
                startActivity(
                    Intent(
                        WinePickApplication.appContext,
                        TypeDetailActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                finish()
            } else {
                SurveyFragment(R.layout.fragment_survey).apply {
                    arguments = Bundle().apply { putInt("currentStage", currentSurvey!!.number) }
                }.next(supportFragmentManager, binding.surveyContent.id)
            }
        }
    }

    /**
     * 특정 프레그먼트로 이동한다.
     * 설문 진행 도중, 다음 설문으로 넘어갈 시 활용된다.
     *
     * @param fm FragmentManager
     * @param id 프레그먼트가 들어갈 레이아웃 id
     */
    fun SurveyFragment.next(fm: FragmentManager, @IdRes id: Int): Int {
        return fm.run {
            beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_right_in, R.anim.slide_left_out,
                    R.anim.slide_left_in, R.anim.slide_right_out
                )
                .replace(id, this@next, this@next::class.simpleName)
                .commit()
        }
    }
}
