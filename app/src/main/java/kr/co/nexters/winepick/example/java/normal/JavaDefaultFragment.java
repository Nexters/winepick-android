package kr.co.nexters.winepick.example.java.normal;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kr.co.nexters.winepick.R;
import kr.co.nexters.winepick.ui.base.BaseFragment;
import kr.co.nexters.winepick.ui.base.BaseViewModel;
import kr.co.nexters.winepick.databinding.FragmentJavaDefaultBinding;

/**
 * Java 에서 사용하는 일반 프레그먼트 예
 *
 * @since v1.0.0 / 2021.01.28
 */
public class JavaDefaultFragment extends BaseFragment<FragmentJavaDefaultBinding> {
    /**
     * 빈 생성자를 통해 layout ID 를 주입
     */
    public JavaDefaultFragment() {
        super(R.layout.fragment_java_default);
    }

    /**
     * ViewModel 을 사용하지 않으므로 null 리턴
     */
    @Nullable
    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 텍스트 값 세팅
        binding.tvTitle.setText("자바 프레그먼트 테스트");

        // 클릭 리스너 사용 예 (with Lambda)
        binding.tvTitle.setOnClickListener(v -> {
            Toast.makeText(getActivity(), binding.tvTitle.getText(), Toast.LENGTH_SHORT).show();
        });
    }
}
