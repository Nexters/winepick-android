@file:UseContextualSerialization(URL::class)
@file:UseSerializers(URLSerializer::class)

package kr.co.nexters.winepick.feature.riflockle7

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.net.URL

/**
 * kotlinx.serialization 를 테스트하기 위한 더미 데이터 클래스
 *
 * @since v1.0.0 / 2021.02.02
 */
@Serializable
data class DummyMovie(
    @SerialName(value = "date")
    val date: String,
    @SerialName(value = "grade")
    val grade: Int,
    @SerialName(value = "id")
    val id: String,
    @SerialName(value = "reservation_grade")
    val reservationGrade: Int,
    @SerialName(value = "reservation_rate")
    val reservationRate: Double,
    @SerialName(value = "thumb")
    val thumb: URL?,
    @SerialName(value = "title")
    val title: String,
    @SerialName(value = "user_rating")
    val userRating: Int
) {
    // property with a backing field -- serialized
    var stars: Int = 37

    // getter only, no backing field -- not serialized
    val path: String
        get() = "kotlin"

    // delegated property -- not serialized
    val delegatedId by ::id
}

fun getDummy(): DummyMovie = DummyMovie(
    date = "2017-11-22",
    grade = 15,
    id = "5a54be21e8a71d136fb536a1",
    reservationGrade = 6,
    reservationRate = 61.69,
    thumb = URL("https://raw.githubusercontent.com/riflockle7/ref/master/1.%20ImageRef/padakpadak/6.jpg"),
    title = "꾼",
    userRating = 5,
)

object URLSerializer : KSerializer<URL?> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("URL", PrimitiveKind.STRING)

    // 역직렬화 하는 시나리오를 명세 (JsonString -> Object)
    override fun deserialize(decoder: Decoder): URL? {
        return try {
            URL(decoder.decodeString())
        } catch (throwable: Throwable) {
            null
        }
    }

    // 직렬화 하는 시나리오를 명세 (Object -> JsonString)
    override fun serialize(encoder: Encoder, value: URL?) {
        val string = value.toString()
        encoder.encodeString(string)
    }
}
