package kr.co.nexters.winepick

import androidx.test.core.app.ActivityScenario
import kr.co.nexters.winepick.example.kotlin.viewmodel.KotlinViewModelActivity
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


/**
 * example 패키지 내에 있는 activity 를 테스트한다.
 */
@RunWith(RobolectricTestRunner::class)
class ExampleUnitTest {
    @Test
    fun kotlinViewModelActivityTitleTest() {
        ActivityScenario.launch(KotlinViewModelActivity::class.java).use { scenario ->
            scenario.onActivity { activity: KotlinViewModelActivity ->
                assert(activity.viewModel.title.value == "코틀린 액티비티 테스트")
            }
        }
    }
}
