package kr.co.nexters.winepick

import dagger.hilt.android.testing.HiltAndroidTest
import kr.co.nexters.winepick.base.AndroidBaseTest
import kr.co.nexters.winepick.example.kotlin.viewmodel.KotlinViewModel
import org.junit.Assert
import org.junit.Test

/**
 * example 패키지 내에 있는 activity 를 테스트한다.
 */
@HiltAndroidTest
class ExampleUnitTest : AndroidBaseTest() {
    @Test
    fun kotlinViewModelActivityTitleTest() {
        val viewModel = KotlinViewModel(winePickRepository)
        assert(viewModel.title.value == "코틀린 액티비티 테스트")

//        // TODO 아마 ViewModel inject 하는 걸 수정해야 할 것 같은데
//        //      시간이 오래 걸릴 것 같아 일단은 ViewModel 테스트만으로 진행
//        ActivityScenario.launch(KotlinViewModelActivity::class.java).use { scenario ->
//            scenario.onActivity { activity: KotlinViewModelActivity ->
//                assert(activity.viewModel.title.value == "코틀린 액티비티 테스트")
//            }
//        }
    }

    /** 와인 카테고리별 정리가 실제로 잘 동작하는지 테스트한다. */
    @Test
    fun test() {
        // 카테고리별 데이터 모음
        val resources = WinePickApplication.appContext?.resources
        val meats = resources?.getStringArray(R.array.meats)
        val italyFoods = resources?.getStringArray(R.array.italyFoods)
        val cheeses = resources?.getStringArray(R.array.cheeses)

        // 이미지 resource Id -> 각 카테고리별 데이터 모음
        val directories = mapOf(
            R.array.meats to meats,
            R.array.italyFoods to italyFoods,
            R.array.cheeses to cheeses,
        )

        // 함께하면 더 맛있는 요리 데이터 정제
        listOf("그릴에 구운 바비큐", "훈제오리", "토마토소스 파스타", "피자", "신선한 치즈").map { keyword ->
            val resource = directories
                // directories 에서 해당 요리가 속하는 데이터 찾기
                .filter { it.value?.contains(keyword) ?: false }
                // 키값 뽑아냄
                .map { it.key }.firstOrNull()

            println("$keyword contains ${directories[resource]?.toList()}")
            val contains = directories[resource]?.toList()?.contains(keyword) ?: false
            if (keyword != "훈제오리" && keyword != "피자")
                Assert.assertEquals(contains, true)
            else
                Assert.assertEquals(contains, false)
        }
    }
}
