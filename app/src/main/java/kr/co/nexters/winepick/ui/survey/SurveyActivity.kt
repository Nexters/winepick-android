package kr.co.nexters.winepick.ui.survey

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentActivity
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.data.model.Survey
import kr.co.nexters.winepick.network.WinePickService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SurveyActivity : FragmentActivity() {
    var survey: Survey? = null
    var winePickService: WinePickService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey)

        var retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-3-35-107-29.ap-northeast-2.compute.amazonaws.com:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        winePickService = retrofit.create(WinePickService::class.java)
}

    fun btnClick(view: View) {
        winePickService!!.getSurveys().enqueue(object: Callback<Survey> {
            override fun onFailure(call: Call<Survey>, t: Throwable) {
                Log.e("Survey conn fail", t.message!!)

            }
            override fun onResponse(call: Call<Survey>, response: Response<Survey>) {
                survey = response.body()
                Log.i("Survey conn success", survey.toString())
            }
        })
    }
}