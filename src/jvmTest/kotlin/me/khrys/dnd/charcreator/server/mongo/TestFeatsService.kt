package me.khrys.dnd.charcreator.server.mongo

import com.mongodb.client.MongoCollection
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import me.khrys.dnd.charcreator.common.models.Feat

val featsList = listOf(Feat(_id = "name", description = "description", functions = emptyArray()))

class TestFeatsService {

    @MockK
    lateinit var feats: MongoCollection<Feat>

    private lateinit var service: FeatsService

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic("kotlin.collections.KMongoIterableKt")

        every { feats.find().toList() } returns featsList

        this.service = FeatsService(feats)
    }

    @Test
    fun testReadFeats() {
        val receivedFeats = service.readFeats()

        assertEquals(
            featsList, receivedFeats,
            "Received feats does not correspond to the expected"
        )
    }
}
