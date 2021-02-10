package kr.co.nexters.winepick.data.source

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kr.co.nexters.winepick.data.constant.Constant.PREF_KEY_SEARCH_CURRENT
import kr.co.nexters.winepick.data.model.local.SearchCurrent
import kr.co.nexters.winepick.util.SharedPrefs

/**
 * 와인 api 요청 시 사용하는 DataSource
 *
 * @since v1.0.0 / 2021.02.08
 */
object SearchDataSource {
    /**
     * SharedPreference 내에 저장되어 있는 [SearchCurrent] 데이터를 모두 불러온다.
     * 초기 단계에서만 진행한다.
     */
    fun getSearchCurrents(): List<SearchCurrent> = Json.decodeFromString(
        ListSerializer(SearchCurrent.serializer()),
        SharedPrefs[PREF_KEY_SEARCH_CURRENT, "[]"]!!
    )

    /** [SearchCurrent] 를 새로 세팅한다. */
    private suspend fun updateSearchCurrents(
        list: List<SearchCurrent>
    ) = withContext(Dispatchers.IO) {
        val jsonString = Json.encodeToString(ListSerializer(SearchCurrent.serializer()), list)

        SharedPrefs[PREF_KEY_SEARCH_CURRENT] = jsonString
    }

    /** [list] 에 새로운 [searchCurrent] 를 추가한다. */
    suspend fun addSearchCurrent(
        list: List<SearchCurrent>, searchCurrent: SearchCurrent
    ): List<SearchCurrent> = withContext(Dispatchers.IO) {
        val newList =
            // 동일한 value 가 있는 경우 그 값을 뺀다.
            list.filter { searchCurrent.value != it.value }
                // 맨 앞에 새로운 데이터를 추가한다.
                .toMutableList().apply { add(0, searchCurrent) }
                // 1000 개만 가지고 오고 나머지는 버린다.
                .take(1000)

        updateSearchCurrents(newList)

        return@withContext newList
    }
}
