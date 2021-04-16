package kr.co.nexters.winepick.ui.survey;

import android.os.Bundle;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import kotlin.Unit;
import kr.co.nexters.winepick.data.model.SurveyAnswerType;
import kr.co.nexters.winepick.data.model.SurveyInfo;
import kr.co.nexters.winepick.data.repository.SurveyRepository;
import kr.co.nexters.winepick.databinding.FragmentSurveyBinding;
import kr.co.nexters.winepick.ui.base.BaseFragment;
import kr.co.nexters.winepick.ui.base.BaseViewModel;
import kr.co.nexters.winepick.util.ViewExtKt;
import timber.log.Timber;

@AndroidEntryPoint
public class SurveyFragment extends BaseFragment<FragmentSurveyBinding> {
    public SurveyFragment(int layoutResId) {
        super(layoutResId);
    }

    @Nullable
    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }

    int currentStage = 0;
    @Inject
    SurveyRepository surveyRepository;
    SurveyInfo surveyInfo;

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        surveyInfo = surveyRepository.getCurrentSurvey();

        if (surveyInfo != null) {
            Timber.i("data: %s", surveyInfo);
            currentStage = surveyInfo.getNumber();
            setData(surveyInfo.getSurvey().getContent(),
                    surveyInfo.getSurvey().getAnswersA(),
                    surveyInfo.getSurvey().getAnswersB(),
                    (currentStage + 1) + "");
        }

        ViewExtKt.setOnSingleClickListener(binding.surveyAnswerA, () -> {
            ((SurveyActivity) requireActivity()).nextSurvey(SurveyAnswerType.ANSWER_A);
            return Unit.INSTANCE;
        });

        ViewExtKt.setOnSingleClickListener(binding.surveyAnswerB, () -> {
            ((SurveyActivity) requireActivity()).nextSurvey(SurveyAnswerType.ANSWER_B);
            return Unit.INSTANCE;
        });
    }

    public void setData(String text, String ansA, String ansB, String num) {
        Timber.i(ansA);
        Timber.i(ansB);

        binding.surveyQuestionText.setText(text);
        binding.currentSurveyNumberText.setText("0" + num);
        binding.allSurveyNumberText.setText("0" + num + " / 06");
        binding.surveyAnswerA.setText(ansA);
        binding.surveyAnswerB.setText(ansB);
    }
}
