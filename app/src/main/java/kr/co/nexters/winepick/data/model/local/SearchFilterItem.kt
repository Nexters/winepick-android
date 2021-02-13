package kr.co.nexters.winepick.data.model.local

/**
 * 검색 필터 설정 화면에서 사용하는 아이템이 가지고 있는 data class
 *
 * @param id 식별자
 * @param serverId 서버에서 사용하는 식별자
 * @param value UI 에서 보여주는 값
 * @param category 해당 아이템이 속하는 대분류 카테고리
 * @param group 해당 아이템이 속하는 소분류 카테고리
 * @param selected 유저가 선택했는지에 대한 flag
 *
 * @since v1.0.0 / 2021.02.11
 */
data class SearchFilterItem(
    val id: Int,
    val serverId: Int,
    val value: String,
    val category: SearchFilterCategory,
    val group: SearchFilterGroup,
    val selected: Boolean
)

/** 해당 아이템이 속하는 대분류 카테고리 */
enum class SearchFilterCategory(priority: Int) {
    TASTE(0),          // 맛
    SITUATION(1),      // 상황
    WHERE(2)           // 살 곳
}

/** 해당 아이템이 속하는 소분류 카테고리 */
enum class SearchFilterGroup(priority: Int) {
    CONTENT(0),        // 도수
    TYPE(1),           // 종류
    TASTE(2),          // 맛
    EVENT(0),          // 이벤트
    FOOD(1),           // 음식
    CONVENIENCE(0)     // 편의점
}
