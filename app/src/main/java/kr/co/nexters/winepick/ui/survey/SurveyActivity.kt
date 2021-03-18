package kr.co.nexters.winepick.ui.survey

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.databinding.ActivitySurveyBinding
import kr.co.nexters.winepick.network.WinePickService
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.base.BaseViewModel
import kr.co.nexters.winepick.ui.base.navigate
import kr.co.nexters.winepick.util.setOnSingleClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class SurveyActivity() : BaseActivity<ActivitySurveyBinding>(R.layout.activity_survey) {
    override val viewModel: BaseViewModel? = null

    // fragment 초기화
    private var fragmentManager: FragmentManager? = null
    private var transaction: FragmentTransaction? = null

    private var surveyFragment: SurveyFragment? = null
    private var sampleFragment: SampleFragment? = null

    // 통신
    var winePickService: WinePickService? = null

    // 설문 데이터 변경 관련
    var currentStage: Int = 1
    var type: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.i("화긴, $currentStage")
        SurveyFragment(R.layout.fragment_survey).apply {
            arguments = Bundle().apply { putInt("currentStage", currentStage) }
        }.navigate(supportFragmentManager, binding.surveyContent.id)

        binding.homeButton.setOnSingleClickListener {
            sampleFragment = SampleFragment(R.layout.fragment_survey)

            SurveyFragment(R.layout.fragment_survey).apply {
                arguments = Bundle().apply { putInt("currentStage", currentStage + 1) }
            }.next(
                supportFragmentManager,
                binding.surveyContent.id
            )
        }
    }

    /**
     * 특정 프레그먼트를 띄워준다.
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
