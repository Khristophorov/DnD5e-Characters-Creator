package mongo

import com.mongodb.client.MongoCollection
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import me.khrys.dnd.charcreator.common.models.Translation
import me.khrys.dnd.charcreator.server.mongo.TranslationService

val translationsList = listOf(Translation("key", "value"))

class TestTranslationService {

    @MockK
    lateinit var translations: MongoCollection<Translation>

    private lateinit var translationService: TranslationService

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic("kotlin.collections.KMongoIterableKt")

        every { translations.find().toList() } returns translationsList

        this.translationService = TranslationService(translations)
    }

    @Test
    fun testReadTranslations() {
        val receivedTranslations = translationService.readTranslations()

        assertEquals(
            translationsList, receivedTranslations,
            "Received translations does not correspond to the expected"
        )
    }
}