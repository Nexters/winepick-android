package kr.co.nexters.winepick.data.model

data class WineList(
    val acidity: Int,
    val body: Int,
    val category: String,
    val country: String,
    val feeling: String,
    val id: Int,
    val likes: Int,
    val nmEng: String,
    val nmKor: String,
    val price: Int,
    val suitEvent: String,
    val suitFood: String,
    val suitWho: String,
    val sweetness: Int,
    val tannin: Int
)