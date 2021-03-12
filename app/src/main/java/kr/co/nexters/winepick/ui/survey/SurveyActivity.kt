package kr.co.nexters.winepick.ui.survey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.data.model.Survey
import kr.co.nexters.winepick.network.WinePickService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SurveyActivity : AppCompatActivity() {

    // fragment 초기화
    private var fragmentManager: FragmentManager? = null
    private var transaction: FragmentTransaction? = null

    private var surveyFragment: SurveyFragment? = null
    private var sampleFragment: SampleFragment? = null

    // 통신
    var winePickService: WinePickService? = null
    var survey: Survey? = null

    // 설문 데이터 변경 관련
    var currentStage: Int = 1
    var type: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_survey)
        loadSurvey()

        fragmentManager = supportFragmentManager
        /*surveyFragment = SurveyFragment().apply {

        }*/
        Log.isLoggable("화긴", currentStage)
        fragmentManager!!.beginTransaction()
            .replace(R.id.survey_content, SurveyFragment().apply {
                arguments = Bundle().apply {
                    putInt("currentStage", currentStage)
                }
            })
            .commitAllowingStateLoss()

        //transaction!!.replace(R.id.frameLayout, surveyFragment!!)
    }

    fun btnClick(view: View) {
        sampleFragment = SampleFragment()

        transaction = fragmentManager!!.beginTransaction()
        transaction!!
            .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out,
                R.anim.slide_left_in, R.anim.slide_right_out)
            .replace(R.id.survey_content, sampleFragment!!)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    private fun loadSurvey() {
        var questionText: String = ""
        var retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-3-35-107-29.ap-northeast-2.compute.amazonaws.com:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        winePickService = retrofit.create(WinePickService::class.java)

        winePickService!!.getSurveys().enqueue(object: Callback<Survey> {
            override fun onFailure(call: Call<Survey>, t: Throwable) {
                Log.e("Connect Survey Fail", t.message!!)

            }
            override fun onResponse(call: Call<Survey>, response: Response<Survey>) {
                survey = response.body()
                Log.i(survey.toString(), "서베이 데이터")
/*                for (data in survey!!.data) {
                    Log.i(data.toString(), "포문 데이터")
                }

                surveyFragment!!.setData(
                    survey!!.data.get(0).content,
                    survey!!.data.get(0).answersA,
                    survey!!.data.get(0).answersB,
                    currentStage.toString()
                )
                Log.i(survey.toString(), "surveyData")*/
            }
        })
    }

}