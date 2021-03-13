package kr.co.nexters.winepick.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kr.co.nexters.winepick.data.constant.Constant
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.data.source.WineDataSource
import kr.co.nexters.winepick.util.SharedPrefs
import java.util.*

/**
 * Wine 관련 Repository
 *
 * @since v1.0.0 / 2021.02.04
 */
class WineRepository(private val wineDataSource: WineDataSource, val sharedPrefs: SharedPrefs) {
    /** 최근에 본 와인 목록. 2개만 저장한다. */
    private val _userViewWines: Queue<WineResult> = LinkedList()
    val userViewWines: List<WineResult>
        get() = _userViewWines.toMutableList()

    init {
        // _userViewWines 초기화
        Json.decodeFromString(
            ListSerializer(WineResult.serializer()),
            sharedPrefs[Constant.PREF_KEY_USER_VIEW_WINES, "[]"] ?: "[]"
        ).let { it.forEach { wineResult -> _userViewWines.offer(wineResult) } }
    }

    /** 유저가 확인한 와인[wineResult] 를 Queue 에 더하고 SharedPref 에 반영한다. */
    fun addViewWine(wineResult: WineResult) {
        if (_userViewWines.size >= 2)
            _userViewWines.poll()
        _userViewWines.offer(wineResult)

        val jsonString = Json.encodeToString<List<WineResult>>(_userViewWines.toList())
        sharedPrefs[Constant.PREF_KEY_USER_VIEW_WINES] = jsonString
    }

    /** [WineDataSource] 를 통해, 와인 목록 [List] 을 가져온다. */
    suspend fun getWines(
        pageSize: Int,
        pageNumber: Int = 0,
        sort: String = "",
    ) = withContext(Dispatchers.IO) {
        return@withContext wineDataSource.getWines(pageSize, pageNumber, sort)
    }

    /** [WineDataSource] 를 통해, 키워드와 매칭 되는 와인 목록 [List] 을 가져온다. */
    suspend fun getWinesKeyword(
        pageSize: Int,
        pageNumber: Int = 0,
        keyword: String = "",
    ) = withContext(Dispatchers.IO) {
        return@withContext wineDataSource.getWinesKeyword(pageSize, pageNumber, keyword)
    }

    /** [WineDataSource] 를 통해, 특정 와인에 대한 정보 [WineResult] 를 가져온다. */
    suspend fun getWine(wineId: Int) = withContext(Dispatchers.IO) {
        return@withContext wineDataSource.getWine(wineId)
    }

    /** [WineDataSource] 를 통해, 필터링을 걸쳐 가져온 와인 목록 [List] 를 가져온다. */
    suspend fun getWinesFilter(
        wineName: String? = null,
        category: String? = null,
        food: String? = null,
        store: String? = null,
        start: Int? = null,
        end: Int? = null,
        keywords: List<String> = listOf(""),
        pageSize: Int,
        pageNumber: Int = 0,
        sort: String? = null,
    ) = withContext(Dispatchers.IO) {
        return@withContext wineDataSource.getWinesFilter(
            wineName,
            category,
            food,
            store,
            start,
            end,
            keywords,
            pageSize,
            pageNumber,
            sort
        )
    }
}
