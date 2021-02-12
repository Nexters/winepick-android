package kr.co.nexters.winepick.example.java.normal;

import android.os.Bundle;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import kr.co.nexters.winepick.R;
import kr.co.nexters.winepick.ui.base.BaseActivity;
import kr.co.nexters.winepick.ui.base.BaseFragmentKt;
import kr.co.nexters.winepick.ui.base.BaseViewModel;
import kr.co.nexters.winepick.databinding.ActivityJavaDefaultBinding;

/**
 * Java 에서 사용하는 일반 액티비티 예
 *
 * @since v1.0.0 / 2021.01.23
 */
public class JavaDefaultActivity extends BaseActivity<ActivityJavaDefaultBinding> {
    /**
     * 빈 생성자를 통해 layout ID 를 주입
     */
    public JavaDefaultActivity() {
        super(R.layout.activity_java_default);
    }

    /**
     * ViewModel 을 사용하지 않으므로 null 리턴
     */
    @Nullable
    @Override
    public BaseViewModel getViewModel() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 텍스트 값 세팅
        binding.tvTitle.setText("자바 액티비티 테스트");

        // 클릭 리스너 사용 예 (with Lambda)
        binding.tvTitle.setOnClickListener(v -> {
            Toast.makeText(this, binding.tvTitle.getText(), Toast.LENGTH_SHORT).show();
        });


        // 특정 프레임 레이아웃에 Fragment 을 로딩하기 위해 사용하는 로직 (Fragment 사용안할 시 필요 없는 코드)
        BaseFragmentKt.navigate(new JavaDefaultFragment(), getSupportFragmentManager(), binding.flFragment.getId());
    }
}
