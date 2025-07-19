package me.khrys.dnd.charcreator.server.mongo

import com.mongodb.client.MongoCollection
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import me.khrys.dnd.charcreator.common.models.Maneuver
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

val maneuversList = listOf(Maneuver(_id = "name", description = "description", source = "source"))

class TestManeuversService {
    @MockK
    lateinit var maneuvers: MongoCollection<Maneuver>

    private lateinit var service: ManeuversService

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic("kotlin.collections.KMongoIterableKt")

        every { maneuvers.find().toList() } returns maneuversList

        this.service = ManeuversService(maneuvers)
    }

    @Test
    fun testReadFeats() {
        val receivedManeuvers = service.readManeuvers()

        assertEquals(
            maneuversList, receivedManeuvers,
            "Received maneuvers does not correspond to the expected"
        )
    }
}
