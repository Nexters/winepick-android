package kr.co.nexters.winepick.data.repository


import android.content.Intent
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.data.model.AccessTokenData
import kr.co.nexters.winepick.data.model.LikeWine
import kr.co.nexters.winepick.data.model.PutUserRequest
import kr.co.nexters.winepick.data.model.remote.user.UserResult
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.data.response.PersonalityType
import kr.co.nexters.winepick.data.response.UserData
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.network.WinePickService
import kr.co.nexters.winepick.ui.home.HomeActivity
import kr.co.nexters.winepick.util.safeEnqueue
import timber.log.Timber
import javax.inject.Inject

class WinePickRepository @Inject constructor(
    private val api: WinePickService, private val authManager: AuthManager
) {
    val dictionaries: MutableMap<Int, Array<String>> = mutableMapOf()

    init {
        val appContext = WinePickApplication.getGlobalApplicationContext()
        listOf(
            R.drawable.bread to R.array.breads,
            R.drawable.fish to R.array.fishes,
            R.drawable.meat to R.array.meats,
            R.drawable.lobster to R.array.lobstars,
            R.drawable.korean_food to R.array.koreanFoods,
            R.drawable.italy_food to R.array.italyFoods,
            R.drawable.hamburger to R.array.hamburgers,
            R.drawable.fruit to R.array.fruits,
            R.drawable.curry to R.array.curries,
            R.drawable.cheese to R.array.cheeses,
            R.drawable.cake to R.array.cakes,
            R.drawable.shellfish to R.array.shellfishes,
            R.drawable.asian_food to R.array.asianFoods,
            R.drawable.pizza to R.array.pizzas,
            R.drawable.shrimp to R.array.shrimps,
            R.drawable.stake to R.array.stakes,
            R.drawable.sushi to R.array.sushies,
            R.drawable.vegetable to R.array.vegetables,
            R.drawable.chicken to R.array.chickens,
        ).forEach { resources ->
            dictionaries[resources.first] = appContext.resources.getStringArray(resources.second)
        }
    }

    fun getResourceFromFoodName(wineFood: String): Int? {
        val resource = dictionaries
            .filter { it.value.contains(wineFood) ?: false } // directories 에서 해당 요리가 속하는 데이터 찾기
            .map { it.key }.firstOrNull()                    // 키값 뽑아냄

        Timber.i("$this contains ${dictionaries[resource]?.toList()}")
        return resource
    }

    fun getWine(
        wineId: Int,
        onSuccess: (WineResult) -> Unit,
        onFailure: () -> Unit
    ) {
        api.getWine(wineId).safeEnqueue(
            onSuccess = { onSuccess(it.result!!) },
            onFailure = { onFailure() },
            onError = { onFailure() }
        )
    }

    fun postLike(
        data: LikeWine,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        api.postLike(data).safeEnqueue(
            onSuccess = { onSuccess() },
            onFailure = { onFailure() },
            onError = { onFailure() }
        )
    }

    fun getLikeWineList(
        userId: Int,
        onSuccess: (List<WineResult>) -> Unit,
        onFailure: () -> Unit
    ) {
        api.getLikesWineList(userId).safeEnqueue(
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
    ) {
        api.deleteLike(userId, wineId).safeEnqueue(
            onSuccess = { onSuccess() },
            onFailure = { onFailure() },
            onError = { onFailure() }
        )
    }

    fun postUser(
        data: AccessTokenData,
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
        resultId: Int,
        onSuccess: (PersonalityType) -> Unit,
        onFailure: () -> Unit
    ) {
        api.getResult(resultId).safeEnqueue(
            onSuccess = { onSuccess(it!!.result!!) },
            onFailure = { onFailure() },
            onError = { onFailure() }
        )
    }

    fun putUser(
        accessToken: String,
        jsonRequest: PutUserRequest,
        onSuccess: (UserResult) -> Unit,
        onFailure: () -> Unit
    ) {
        api.putUser(accessToken, jsonRequest).safeEnqueue(
            onSuccess = { onSuccess(it.result) },
            onFailure = { onFailure() },
            onError = { onFailure() }
        )
    }

    fun updateUser(
        token: String,
        personalityType: String?,
        userId: Long,
        successAction: (() -> Unit)? = null,
        failAction: (() -> Unit)? = null
    ) {
        postUser(
            data = AccessTokenData(token, personalityType!!, userId),
            onSuccess = {
                authManager.token = it.accessToken.toString()
                authManager.autoLogin = true
                authManager.id = it.id!!
                authManager.testType = it.personalityType!!
                successAction?.let { it() }

                Intent(WinePickApplication.appContext, HomeActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run {
                    WinePickApplication.getGlobalApplicationContext().startActivity(this)
                }
            },
            onFailure = {
                authManager.testType = "N"
                failAction?.let { it() }

                // _toastMeesageText.value = WinePickApplication.getGlobalApplicationContext()
                //     .resources.getString(R.string.api_error)
            }
        )
    }
}
