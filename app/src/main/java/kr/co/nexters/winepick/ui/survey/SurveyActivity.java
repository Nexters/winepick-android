package kr.co.nexters.winepick.ui.survey;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import kr.co.nexters.winepick.R;
import kr.co.nexters.winepick.data.repository.WinePickRepository;
import kr.co.nexters.winepick.data.repository.WineRepository;
import kr.co.nexters.winepick.di.AuthManager;

public class SurveyActivity extends FragmentActivity {

    SurveyFragment mainFragment;
    ScreenSlidePageFragment subFragment;
    AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        //TODO home화면 test를 위해 임시로 만든 것
        testType();

        mainFragment = (SurveyFragment) getSupportFragmentManager().findFragmentById(R.id.surveyFragment);
        subFragment = new ScreenSlidePageFragment();
    }

    public void testType() {
        authManager = new AuthManager(getApplicationContext());
        authManager.setTest_type("A");
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