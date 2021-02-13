package kr.co.nexters.winepick.ui.survey;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import kr.co.nexters.winepick.R;

public class SurveyActivity extends FragmentActivity {

    SurveyFragment mainFragment;
    ScreenSlidePageFragment subFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        mainFragment = (SurveyFragment) getSupportFragmentManager().findFragmentById(R.id.surveyFragment);
        subFragment = new ScreenSlidePageFragment();
    }

    public void onFragmentChange(int fragmentNum) {
        //프래그먼트의 번호에 따라 다르게 작동하는 조건문
        if(fragmentNum == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.action_container, subFragment).commit();
        } else if(fragmentNum == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.action_container, mainFragment).commit();
        }
    }
}