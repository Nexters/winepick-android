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

    lateinit var surveyFragment: SurveyFragment
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
        surveyFragment = SurveyFragment().apply {
            arguments = Bundle().apply {
                putInt("currentStage", currentStage)
            }
        }
        Log.isLoggable("화긴", currentStage)
        transaction = fragmentManager!!.beginTransaction()
        transaction!!.replace(R.id.frameLayout, surveyFragment!!)
            .commitAllowingStateLoss()
    }

    fun btnClick(view: View) {
        sampleFragment = SampleFragment()

        transaction = fragmentManager!!.beginTransaction()
        transaction!!
            .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out,
                R.anim.slide_left_in, R.anim.slide_right_out)
            .replace(R.id.frameLayout, sampleFragment!!)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    fun loadSurvey(): String {
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
                Log.i("Connect Survey Success", survey.toString())
                Log.i("Connect Survey Success", survey!!.data.get(0).content)

                surveyFragment!!.setData(
                    survey!!.data.get(0).content, currentStage.toString()
                )
            }
        })
        return questionText
    }

}