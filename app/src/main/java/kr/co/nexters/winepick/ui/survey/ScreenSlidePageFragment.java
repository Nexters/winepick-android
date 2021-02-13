package kr.co.nexters.winepick.ui.survey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import javax.annotation.Nullable;

import kr.co.nexters.winepick.R;


public class ScreenSlidePageFragment extends Fragment {

    public static ScreenSlidePageFragment newInstance() {
        return new ScreenSlidePageFragment();
    }
    SurveyActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);

        activity = (SurveyActivity) getActivity();

        return rootView;
    }
}