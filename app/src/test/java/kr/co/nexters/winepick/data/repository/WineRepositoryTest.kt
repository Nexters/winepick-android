package kr.co.nexters.winepick.data.repository

import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kr.co.nexters.winepick.base.AndroidBaseTest
import kr.co.nexters.winepick.constant.Constant
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.data.model.remote.wine.getWineResponse
import org.junit.Assert
import org.junit.Test

/**
 * [WineRepository] 테스트
 *
 * @since v1.0.0 / 2021.02.24
 */
@HiltAndroidTest
class WineRepositoryTest : AndroidBaseTest() {
    private val userViewWines: List<WineResult>
        get() = wineRepository.userViewWines

    val dummyWine1 = getWineResponse().result.copy(id = 0, nmKor = "A")
    val dummyWine2 = getWineResponse().result.copy(id = 1, nmKor = "B")
    val dummyWine3 = getWineResponse().result.copy(id = 2, nmKor = "C")

    @Test
    fun userViewWinesTest() {
        // THEN : 맨 처음 유저가 본 와인은 0개여야 한다.
        Assert.assertTrue(userViewWines.isEmpty())

        // GIVEN : 유저가 dummyWine1 을 본다.
        wineRepository.addViewWine(dummyWine1)
        // THEN : 유저가 본 와인 목록에 dummyWine1 정보가 들어가 있어야 한다.
        Assert.assertTrue(userViewWines.isNotEmpty() && userViewWines.first().nmKor == "A")

        // GIVEN : 유저가 dummyWine2, dummyWine3 을 본다.
        wineRepository.addViewWine(dummyWine2)
        wineRepository.addViewWine(dummyWine3)
        // THEN : 유저가 본 와인 목록은 2개여야 한다 (dummyWine2, dummyWine3)
        Assert.assertTrue(userViewWines.size == 2)
        // THEN : 맨 첫번째 데이터는 dummyWine2 여야 한다.
        Assert.assertTrue(userViewWines.first().nmKor == "B")

        // THEN : sharedPref 내에 정상적으로 데이터가 들어가 있어야 한다.
        val userViewWinesJsonString = sharedPrefs[Constant.PREF_KEY_USER_VIEW_WINES, "[]"] ?: "[]"
        val prefUserViewWines = Json.decodeFromString(
            ListSerializer(WineResult.serializer()),
            userViewWinesJsonString
        )
        Assert.assertTrue(prefUserViewWines.size == 2)
        Assert.assertTrue(prefUserViewWines.first().nmKor == "B")
    }
}
