package kr.co.nexters.winepick.ui.survey;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import androidx.fragment.app.Fragment;
import kr.co.nexters.winepick.R;
import kr.co.nexters.winepick.databinding.FragmentSurveyBinding;
import kr.co.nexters.winepick.ui.base.BaseFragment;
import kr.co.nexters.winepick.ui.base.BaseViewModel;
import timber.log.Timber;

public class SurveyFragment extends BaseFragment<FragmentSurveyBinding> {
    public SurveyFragment(int layoutResId) {
        super(layoutResId);
    }

    @Nullable
    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }

    private int currentStage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = new Bundle();
        bundle.getBundle("currentStage");

        int i = getArguments().getInt("currentStage");
        Log.isLoggable("argument", i);

        if (getArguments() != null) {
            currentStage = getArguments().getInt("currentStage");
            Log.isLoggable("data: ", currentStage);
        }
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public String answerClick(View view) {
        switch (view.getId()) {
            case R.id.survey_answer_A:
                return "A";
            case R.id.survey_answer_B:
                return "B";
        }
        return "";
    }

    public void setData(String text, String ansA, String ansB, String num) {
        Timber.i(ansA);
        Timber.i(ansB);

        binding.surveyQuestionText.setText(text);
        binding.currentSurveyNumberText.setText("0" + num);
        binding.allSurveyNumberText.setText("0" + num + " / 09");
        binding.surveyAnswerA.setText(ansA);
        binding.surveyAnswerB.setText(ansB);
    }
}
