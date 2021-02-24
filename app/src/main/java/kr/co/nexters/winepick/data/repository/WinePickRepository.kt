package kr.co.nexters.winepick.data.repository


import kr.co.nexters.winepick.data.model.AccessTokenData
import kr.co.nexters.winepick.data.model.LikeWine
import kr.co.nexters.winepick.data.model.TokenInfo
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.data.response.PersonalityType
import kr.co.nexters.winepick.data.response.UserData
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
        onSuccess: (List<WineResult>) -> Unit,
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
            data : AccessTokenData,
            onSuccess: (UserData) -> Unit,
            onFailure: () -> Unit
    ) {
        api.postUser(data).safeEnqueue(
            onSuccess = { onSuccess(it!!.result!!) },
            onFailure = { onFailure() },
            onError = { onFailure() }
        )
    }

    fun getUser(
            userId: Int,
            accessToken: String,
            onSuccess: (UserData) -> Unit,
            onFailure: () -> Unit
    ) {
        api.getUser(userId, accessToken).safeEnqueue(
            onSuccess = { onSuccess(it!!.result!!) },
            onFailure = { onFailure() },
            onError = { onFailure() }
        )
    }

    fun getResult(
        resultId : Int,
        onSuccess: (PersonalityType) -> Unit,
        onFailure: () -> Unit
    ) {
        api.getResult(resultId).safeEnqueue(
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