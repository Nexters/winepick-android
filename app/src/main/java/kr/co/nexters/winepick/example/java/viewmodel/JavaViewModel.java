package kr.co.nexters.winepick.example.java.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import kr.co.nexters.winepick.base.BaseViewModel;

/**
 * 자바에서 사용하는 ViewModel
 *
 * @since v1.0.0 / 2021.01.28
 */
public class JavaViewModel extends BaseViewModel {
    private MutableLiveData<String> _title = new MutableLiveData<>();
    LiveData<String> title = _title;

    /**
     * 생성자
     */
    public JavaViewModel() {
        _title.setValue("자바 액티비티 테스트");
    }

    /**
     * title getter
     */
    public LiveData<String> getTitle() {
        return title;
    }

    /**
     * 제목을 변경한다. UI 에서 [_title] 에 직접 접근하는 것을 막기 위해 사용한다.
     */
    public void editTitle(String title) {
        _title.setValue(title);
    }


    /**
     * UI 의 onDestroy 개념으로 생각하면 편할듯
     */
    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
