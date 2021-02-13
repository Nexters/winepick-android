package kr.co.nexters.winepick.ui.survey;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kr.co.nexters.winepick.R;

public class SurveyFragment extends Fragment {

    SurveyActivity activity;

    @Override
    public void onAttach(@Nonnull Context context) {
        super.onAttach(context);
        activity = (SurveyActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_survey, container, false);

        Button button = rootView.findViewById(R.id.survey_answer_A);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onFragmentChange(1);
            }
        });

        return rootView;
    }
}
