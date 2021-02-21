package kr.co.nexters.winepick.data.repository


import com.kakao.auth.authorization.accesstoken.AccessToken
import kr.co.nexters.winepick.data.model.LikeWine
import kr.co.nexters.winepick.data.model.TokenInfo
import kr.co.nexters.winepick.data.model.UserData
import kr.co.nexters.winepick.data.model.WineList
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.data.response.PersonalityType
import kr.co.nexters.winepick.data.response.getUserData
import kr.co.nexters.winepick.data.response.postUserData
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
        data : UserData,
        onSuccess: (postUserData) -> Unit,
        onFailure: () -> Unit
    ) {
        api.postUser(data).safeEnqueue(
            onSuccess = { onSuccess(it!!.result!!) },
            onFailure = { onFailure() },
            onError = { onFailure() }
        )
    }

    fun getUser(
        accessToken: String,
        onSuccess: (getUserData) -> Unit,
        onFailure: () -> Unit
    ) {
        api.getUser(accessToken).safeEnqueue(
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