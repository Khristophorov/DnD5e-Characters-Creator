package me.khrys.dnd.charcreator.server.mongo

import com.mongodb.client.MongoCollection
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import me.khrys.dnd.charcreator.common.models.Class
import me.khrys.dnd.charcreator.common.models.Dice
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

val classesList = listOf(Class("key", "description", features = emptyMap(), hitDice = Dice.D4))

class TestClassesService {

    @MockK
    lateinit var classes: MongoCollection<Class>

    private lateinit var service: ClassesService

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic("kotlin.collections.KMongoIterableKt")

        every { classes.find().toList() } returns classesList

        this.service = ClassesService(classes)
    }

    @Test
    fun testReadClasses() {
        val receivedClasses = service.readClasses()

        assertEquals(
            classesList, receivedClasses,
            "Received classes does not correspond to the expected"
        )
    }
}
