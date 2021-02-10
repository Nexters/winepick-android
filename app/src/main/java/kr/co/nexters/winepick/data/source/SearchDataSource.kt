package kr.co.nexters.winepick.data.source

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.data.constant.Constant.PREF_KEY_WINE_INFOS
import kr.co.nexters.winepick.util.SharedPrefs

/**
 * 와인 api 요청 시 사용하는 DataSource
 *
 * @since v1.0.0 / 2021.02.08
 */
object SearchDataSource {
    /**
     * SharedPreference 내에 저장되어 있는 wine_info 정보들을 가져온다.
     * SharedPrefs 에 저장된 내용이 없을 시 [R.array.wine_info] 에서 값을 가져와 set 한 후 그 값을 리턴한다.
     */
    fun getWineInfos(): List<String> {
        if (!SharedPrefs.contains(PREF_KEY_WINE_INFOS)) {
            val wineInfos = WinePickApplication.getGlobalApplicationContext()
                .resources.getStringArray(R.array.wine_info)

            val jsonString = Json.encodeToString(
                ListSerializer(String.serializer()), wineInfos.toList()
            )

            SharedPrefs[PREF_KEY_WINE_INFOS] = jsonString

            return wineInfos.toList()
        } else {
            return Json.decodeFromString(
                ListSerializer(String.serializer()),
                SharedPrefs[PREF_KEY_WINE_INFOS, "[]"] ?: "[]"
            )
        }
    }
}
