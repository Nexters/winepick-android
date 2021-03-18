package kr.co.nexters.winepick.ui.survey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.Nullable;

import androidx.fragment.app.Fragment;

import kr.co.nexters.winepick.R;
import kr.co.nexters.winepick.databinding.FragmentSampleBinding;
import kr.co.nexters.winepick.ui.base.BaseFragment;
import kr.co.nexters.winepick.ui.base.BaseViewModel;

public class SampleFragment extends BaseFragment<FragmentSampleBinding> {
    public SampleFragment(int layoutResId) {
        super(layoutResId);
    }

    @Nullable
    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample, container, false);
    }
}
