package me.khrys.dnd.charcreator.server.mongo

import com.mongodb.client.MongoCollection
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import me.khrys.dnd.charcreator.common.models.Race
import mongo.RacesService

val racesList = listOf(Race("key", "description", features = emptyArray()))

class TestRacesService {

    @MockK
    lateinit var races: MongoCollection<Race>

    private lateinit var service: RacesService

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic("kotlin.collections.KMongoIterableKt")

        every { races.find().toList() } returns racesList

        this.service = RacesService(races)
    }

    @Test
    fun testReadRaces() {
        val receivedRaces = service.readRaces()

        assertEquals(
            racesList, receivedRaces,
            "Received races does not correspond to the expected"
        )
    }
}
