package kr.co.nexters.winepick.data.source

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.data.constant.Constant.PREF_KEY_WINE_INFOS
import kr.co.nexters.winepick.data.model.local.SearchFilterCategory
import kr.co.nexters.winepick.data.model.local.SearchFilterGroup
import kr.co.nexters.winepick.data.model.local.SearchFilterItem
import kr.co.nexters.winepick.util.SharedPrefs
import org.koin.core.context.GlobalContext

/**
 * 와인 api 요청 시 사용하는 DataSource
 *
 * @since v1.0.0 / 2021.02.08
 */
class SearchDataSource(private val sharedPrefs: SharedPrefs) {
    /**
     * SharedPreference 내에 저장되어 있는 wine_info 정보들을 가져온다.
     * SharedPrefs 에 저장된 내용이 없을 시 [R.array.wine_info] 에서 값을 가져와 set 한 후 그 값을 리턴한다.
     */
    fun getWineInfos(): List<String> {
        if (!sharedPrefs.contains(PREF_KEY_WINE_INFOS)) {
            val wineInfos = WinePickApplication.appContext!!
                .resources.getStringArray(R.array.wine_info)

            val jsonString = Json.encodeToString(
                ListSerializer(String.serializer()), wineInfos.toList()
            )

            sharedPrefs[PREF_KEY_WINE_INFOS] = jsonString

            return wineInfos.toList()
        } else {
            return Json.decodeFromString(
                ListSerializer(String.serializer()),
                sharedPrefs[PREF_KEY_WINE_INFOS, "[]"] ?: "[]"
            )
        }
    }

    /**
     * 초기 SearchFilter 아이템 정보들을 가져온다
     * TODO 만약 이전에 설정했던 필터값들을 기억하도록 한다면 SharePrefs 를 통해 저장 예정
     *      (ROOM 은 마이그레이션 변수가 우려되어 사용하지 않음)
     */
    fun getSearchFilterItems(): List<SearchFilterItem> =listOf(
        SearchFilterItem(0, 0, "5", SearchFilterCategory.TASTE, SearchFilterGroup.CONTENT, true),
        SearchFilterItem(1, 1, "16", SearchFilterCategory.TASTE, SearchFilterGroup.CONTENT, true),
        SearchFilterItem(2, 2, "레드", SearchFilterCategory.TASTE, SearchFilterGroup.TYPE, false),
        SearchFilterItem(3, 3, "화이트", SearchFilterCategory.TASTE, SearchFilterGroup.TYPE, false),
        SearchFilterItem(4, 4, "스파클링", SearchFilterCategory.TASTE, SearchFilterGroup.TYPE, false),
        SearchFilterItem(5, 5, "달콤한", SearchFilterCategory.TASTE, SearchFilterGroup.TASTE, false),
        SearchFilterItem(6, 6, "쌉싸름한", SearchFilterCategory.TASTE, SearchFilterGroup.TASTE, false),
        SearchFilterItem(7, 7, "부드러운", SearchFilterCategory.TASTE, SearchFilterGroup.TASTE, false),
        SearchFilterItem(8, 8, "상큼한", SearchFilterCategory.TASTE, SearchFilterGroup.TASTE, false),
        SearchFilterItem(9, 9, "파티", SearchFilterCategory.SITUATION, SearchFilterGroup.EVENT, false),
        SearchFilterItem(10,10, "휴식", SearchFilterCategory.SITUATION, SearchFilterGroup.EVENT, false),
        SearchFilterItem(11,11, "근황 토크", SearchFilterCategory.SITUATION, SearchFilterGroup.EVENT, false),
        SearchFilterItem(12,12, "고기", SearchFilterCategory.SITUATION, SearchFilterGroup.FOOD, false),
        SearchFilterItem(13,13, "닭고기", SearchFilterCategory.SITUATION, SearchFilterGroup.FOOD, false),
        SearchFilterItem(14,14, "스테이크", SearchFilterCategory.SITUATION, SearchFilterGroup.FOOD, false),
        SearchFilterItem(15,15, "생선", SearchFilterCategory.SITUATION, SearchFilterGroup.FOOD, false),
        SearchFilterItem(16,16, "새우", SearchFilterCategory.SITUATION, SearchFilterGroup.FOOD, false),
        SearchFilterItem(17,17, "조개", SearchFilterCategory.SITUATION, SearchFilterGroup.FOOD, false),
        SearchFilterItem(18,18, "과일", SearchFilterCategory.SITUATION, SearchFilterGroup.FOOD, false),
        SearchFilterItem(19,19, "이탈리아", SearchFilterCategory.SITUATION, SearchFilterGroup.FOOD, false),
        SearchFilterItem(20,20, "빵", SearchFilterCategory.SITUATION, SearchFilterGroup.FOOD, false),
        SearchFilterItem(21,21, "치즈", SearchFilterCategory.SITUATION, SearchFilterGroup.FOOD, false),
        SearchFilterItem(22,22, "한식", SearchFilterCategory.SITUATION, SearchFilterGroup.FOOD, false),
        SearchFilterItem(23,23, "디저트", SearchFilterCategory.SITUATION, SearchFilterGroup.FOOD, false),
        SearchFilterItem(24,24, "야채", SearchFilterCategory.SITUATION, SearchFilterGroup.FOOD, false),
        SearchFilterItem(25,25, "랍스타", SearchFilterCategory.SITUATION, SearchFilterGroup.FOOD, false),
        SearchFilterItem(26,26, "피자", SearchFilterCategory.SITUATION, SearchFilterGroup.FOOD, false),
        SearchFilterItem(27,27, "햄버거", SearchFilterCategory.SITUATION, SearchFilterGroup.FOOD, false),
        SearchFilterItem(28,28, "아시아 요리", SearchFilterCategory.SITUATION, SearchFilterGroup.FOOD, false),
        SearchFilterItem(29,29, "카레", SearchFilterCategory.SITUATION, SearchFilterGroup.FOOD, false),
        SearchFilterItem(30,30, "스시", SearchFilterCategory.SITUATION, SearchFilterGroup.FOOD, false),
        SearchFilterItem(31,31, "CU", SearchFilterCategory.WHERE, SearchFilterGroup.CONVENIENCE, false),
        SearchFilterItem(32,32, "GS25", SearchFilterCategory.WHERE, SearchFilterGroup.CONVENIENCE, false),
        SearchFilterItem(33,33, "이마트 24", SearchFilterCategory.WHERE, SearchFilterGroup.CONVENIENCE, false),
        SearchFilterItem(34,34, "세븐일레븐", SearchFilterCategory.WHERE, SearchFilterGroup.CONVENIENCE, false),
    )
}
