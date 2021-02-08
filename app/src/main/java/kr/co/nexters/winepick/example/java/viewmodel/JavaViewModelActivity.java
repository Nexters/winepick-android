package kr.co.nexters.winepick.example.java.viewmodel;

import android.os.Bundle;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import androidx.lifecycle.ViewModelProvider;
import kr.co.nexters.winepick.BR;
import kr.co.nexters.winepick.R;
import kr.co.nexters.winepick.base.BaseActivity;
import kr.co.nexters.winepick.base.BaseFragmentKt;
import kr.co.nexters.winepick.databinding.ActivityJavaViewmodelBinding;

/**
 * Java 에서 사용하는 일반 액티비티 예
 *
 * @since v1.0.0 / 2021.01.23
 */
public class JavaViewModelActivity extends BaseActivity<ActivityJavaViewmodelBinding> {
    /**
     * 빈 생성자를 통해 layout ID 를 주입
     */
    public JavaViewModelActivity() {
        super(R.layout.activity_java_viewmodel);
    }

    /**
     * ViewModel 을 사용하지 않으므로 null 리턴
     */
    @NotNull
    @Override
    public JavaViewModel getViewModel() {
        return new ViewModelProvider(this, getViewModelFactory()).get(JavaViewModel.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setVariable(BR.vm, getViewModel());

        // 텍스트 값 세팅 (ViewModel 에서 진행하므로 해당 코드 제거)
        // binding.tvTitle.setText("자바 액티비티 테스트");

        // 클릭 리스너 사용 예 (with Lambda)
        binding.tvTitle.setOnClickListener(v -> {
            Toast.makeText(this, binding.tvTitle.getText(), Toast.LENGTH_SHORT).show();
        });

        // 특정 프레임 레이아웃에 Fragment 을 로딩하기 위해 사용하는 로직 (Fragment 사용안할 시 필요 없는 코드)
        BaseFragmentKt.navigate(new JavaViewModelFragment(), getSupportFragmentManager(), binding.flFragment.getId());

        // 아래와 같은 방식으로 viewModel 내 LiveData 값이 바뀔때마다 observing 가능
        getViewModel().title.observe(this, title ->
                Toast.makeText(this, title + "변경 완료", Toast.LENGTH_SHORT).show()
        );
    }
}
