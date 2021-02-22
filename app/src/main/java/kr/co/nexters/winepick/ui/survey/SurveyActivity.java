package kr.co.nexters.winepick.ui.survey;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import kr.co.nexters.winepick.R;

public class SurveyActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        // TODO home화면 test를 위해 임시로 만든 것
        // testType();
    }

    /*public void testType() {
        authManager = new AuthManager(getApplicationContext());
        authManager.setTest_type("A");
        authManager.setRecent_search1("최근검색어1");
        authManager.setRecent_search2("최근검색어2");
    }*/

}