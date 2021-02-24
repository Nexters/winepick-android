package kr.co.nexters.winepick.ui.survey;

import android.os.Bundle;

import org.koin.java.KoinJavaComponent;

import androidx.fragment.app.FragmentActivity;

import kr.co.nexters.winepick.R;
import kr.co.nexters.winepick.data.repository.WinePickRepository;
import kr.co.nexters.winepick.data.repository.WineRepository;
import kr.co.nexters.winepick.di.AuthManager;
import kr.co.nexters.winepick.util.SharedPrefs;
import timber.log.Timber;

public class SurveyActivity extends FragmentActivity {

    SurveyFragment mainFragment;
    ScreenSlidePageFragment subFragment;
//    private final AuthManager authManager = KoinJavaComponent.inject(AuthManager.class).getValue();

    private final SharedPrefs sharedPrefs = new SharedPrefs();
    private final AuthManager authManager = new AuthManager(sharedPrefs);

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
        authManager.setTestType("A");
        authManager.setRecentSearch1("최근검색어1");
        authManager.setRecentSearch2("최근검색어2");
        Timber.e("Survey - authTestType "+authManager.getTestType());
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
