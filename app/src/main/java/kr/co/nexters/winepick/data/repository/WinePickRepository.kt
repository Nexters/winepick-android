package kr.co.nexters.winepick.data.repository


import io.reactivex.rxjava3.internal.operators.single.SingleDoOnSuccess
import kr.co.nexters.winepick.data.model.LikeWine
import kr.co.nexters.winepick.data.model.TokenInfo
import kr.co.nexters.winepick.data.model.UserData
import kr.co.nexters.winepick.data.model.WineList
import kr.co.nexters.winepick.data.response.UserInfoData
import kr.co.nexters.winepick.network.WinePickService
import kr.co.nexters.winepick.util.safeEnqueue

class WinePickRepository(private val api: WinePickService) {

    fun postUserAccessToken(
        data:TokenInfo,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        api.postUserAccessToken(data).safeEnqueue(
            onSuccess = { onSuccess() },
            onFailure = { onFailure() },
            onError = { onFailure() }
        )
    }

    fun postLike(
        data: LikeWine,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        api.postLike(data).safeEnqueue (
            onSuccess = { onSuccess() },
            onFailure = { onFailure() },
            onError = { onFailure() }
        )
    }

    fun getLikeWineList(
        userId : Int,
        onSuccess: (List<WineList>) -> Unit,
        onFailure: () -> Unit
    ) {
        api.getLikesWineList(userId).safeEnqueue (
            onSuccess = { onSuccess(it.result!!) },
            onFailure = { onFailure() },
            onError = { onFailure() }
        )
    }

    fun deleteLike(
        userId: Int,
        wineId: Int,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    )  {
        api.deleteLike(userId,wineId).safeEnqueue (
            onSuccess = { onSuccess() },
            onFailure = { onFailure() },
            onError = { onFailure() }
        )
    }

    fun postUser(
        data : UserData,
        onSuccess: (UserInfoData) -> Unit,
        onFailure: () -> Unit
    ) {
        api.postUser(data).safeEnqueue(
            onSuccess = { onSuccess(it!!.result!!) },
            onFailure = { onFailure() },
            onError = { onFailure() }
        )
    }

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