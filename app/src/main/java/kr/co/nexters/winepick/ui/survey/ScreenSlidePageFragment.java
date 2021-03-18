package kr.co.nexters.winepick.ui.survey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import javax.annotation.Nullable;

import kr.co.nexters.winepick.R;
import kr.co.nexters.winepick.databinding.FragmentScreenSlidePageBinding;
import kr.co.nexters.winepick.ui.base.BaseFragment;
import kr.co.nexters.winepick.ui.base.BaseViewModel;


public class ScreenSlidePageFragment extends BaseFragment<FragmentScreenSlidePageBinding> {
    public ScreenSlidePageFragment(int layoutResId) {
        super(layoutResId);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false
        );

        return rootView;
    }
}
