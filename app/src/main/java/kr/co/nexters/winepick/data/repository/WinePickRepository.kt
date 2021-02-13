package kr.co.nexters.winepick.data.repository


import kr.co.nexters.winepick.network.WinePickService

class WinePickRepository(private val api: WinePickService) {

//    fun getSchoolList(
//        onSuccess: (List<SchoolSet>) -> Unit,
//        onFailure: () -> Unitgit
//    ) {
//        api.getSchoolList().safeEnqueue(
//            onSuccess = {
//                onSuccess(it.data!!)
//            },
//            onFailure = { onFailure() },
//            onError = { onFailure() }
//        )
//    }
}