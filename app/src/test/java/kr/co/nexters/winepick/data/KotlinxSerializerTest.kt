package kr.co.nexters.winepick.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kr.co.nexters.winepick.test.DummyMovie
import kr.co.nexters.winepick.test.getDummy
import org.junit.Assert
import org.junit.Test

/**
 * kotlinx.serializer 를 테스트
 *
 * @since v1.0.0 / 2021.02.02
 */
class KotlinxSerializerTest {
    @Test
    fun jsonSerializerParseTest() {
        // Serializing (object -> jsonString)
        val data = getDummy()
        val string = Json.encodeToString(data)
        Assert.assertEquals(string, "{\"date\":\"2017-11-22\",\"grade\":15,\"id\":\"5a54be21e8a71d136fb536a1\",\"reservation_grade\":6,\"reservation_rate\":61.69,\"thumb\":\"https://raw.githubusercontent.com/riflockle7/ref/master/1.%20ImageRef/padakpadak/6.jpg\",\"title\":\"꾼\",\"user_rating\":5}")

        // Deserializing (jsonString -> object)
        val obj = Json.decodeFromString<DummyMovie>(string)
        Assert.assertEquals(obj, getDummy())

        // backingField Serializing (object -> jsonString)
        val backingFieldData = getDummy().apply { stars = 40 }
        Assert.assertEquals(backingFieldData.stars, 40)
        Assert.assertEquals(backingFieldData.path, "kotlin")
        Assert.assertEquals(backingFieldData.delegatedId, "5a54be21e8a71d136fb536a1")

        // backingField Deserializing (jsonString -> object)
        val backingFieldJsonStr = Json.encodeToString(getDummy().apply { stars = 40 })
        Assert.assertTrue(backingFieldJsonStr.contains("stars"))
        Assert.assertTrue(!backingFieldJsonStr.contains("path"))
        Assert.assertTrue(!backingFieldJsonStr.contains("delegateId"))

        // KSerializer 인터페이스의 인스턴스 출력하기
        val colorSerializer: KSerializer<DummyMovie> = DummyMovie.serializer()
        println(colorSerializer.descriptor)
    }
}
