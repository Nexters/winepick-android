package kr.co.nexters.winepick.example.java.viewmodel;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import androidx.lifecycle.ViewModelProvider;
import kr.co.nexters.winepick.BR;
import kr.co.nexters.winepick.R;
import kr.co.nexters.winepick.base.BaseFragment;
import kr.co.nexters.winepick.databinding.FragmentJavaViewmodelBinding;

/**
 * Java 에서 사용하는 일반 프레그먼트 예
 *
 * @since v1.0.0 / 2021.01.28
 */
public class JavaViewModelFragment extends BaseFragment<FragmentJavaViewmodelBinding> {
    /**
     * 빈 생성자를 통해 layout ID 를 주입
     */
    public JavaViewModelFragment() {
        super(R.layout.fragment_java_viewmodel);
    }

    /**
     * ViewModel 을 사용하지 않으므로 null 리턴
     */
    @NotNull
    @Override
    protected JavaViewModel getViewModel() {
        return new ViewModelProvider(this, getViewModelFactory()).get(JavaViewModel.class);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setVariable(BR.vm, getViewModel());

        // 텍스트 값 세팅 (ViewModel 에 요청하여 바꾸는 것도 가능하긴 함)
        getViewModel().editTitle("자바 프레그먼트 테스트");

        // 클릭 리스너 사용 예 (with Lambda)
        binding.tvTitle.setOnClickListener(v -> {
            Toast.makeText(getActivity(), binding.tvTitle.getText(), Toast.LENGTH_SHORT).show();
        });
    }
}
