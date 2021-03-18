package kr.co.nexters.winepick.ui.survey;

import android.os.Bundle;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import kr.co.nexters.winepick.R;
import kr.co.nexters.winepick.databinding.ActivityScreenSlideBinding;
import kr.co.nexters.winepick.ui.base.BaseActivity;
import kr.co.nexters.winepick.ui.base.BaseViewModel;

public class ScreenSlidePagerActivity extends BaseActivity<ActivityScreenSlideBinding> {
    /**
     * 빈 생성자를 통해 layout ID 를 주입
     */
    public ScreenSlidePagerActivity() {
        super(R.layout.activity_screen_slide);
    }

    @Nullable
    @Override
    public BaseViewModel getViewModel() {
        return null;
    }

    private static final int NUM_PAGES = 6;

    private ViewPager2 viewPager;

    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public @NotNull Fragment createFragment(int position) {
            return new ScreenSlidePageFragment(R.layout.fragment_screen_slide_page);
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}
