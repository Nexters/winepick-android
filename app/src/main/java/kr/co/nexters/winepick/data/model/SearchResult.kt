package kr.co.nexters.winepick.data.model

import kotlin.random.Random

/**
 * 검색 결과에서 보여주는 아이템 data class
 *
 * @since v1.0.0 / 2021.02.07
 */
data class SearchResult(
    val name: String,
    val madeIn: String,
    val hearted: Boolean,
    val price: Int,
    val taste1: Int,
    val taste2: Int,
    val taste3: Int,
    val taste4: Int,
)

fun getSearchResultDummy(): SearchResult = SearchResult(
    name = "라포데리나 브루넬로 디 몬탈치노 ${Random.nextInt()}",
    madeIn = "몰도바",
    hearted = false,
    price = 31300,
    taste1 = 3,
    taste2 = 2,
    taste3 = 5,
    taste4 = 5,
)
