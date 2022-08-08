package me.khrys.dnd.charcreator.server.mongo

import com.mongodb.client.MongoCollection
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import me.khrys.dnd.charcreator.common.models.Spell
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

val spellsList = listOf(
    Spell(
        _id = "spellId",
        description = "test spell",
        level = 5,
        castingTime = "some time",
        school = "evocation",
        range = "100 fts",
        components = listOf("V"),
        duration = "encounter",
        classes = listOf("rouge"),
        source = "test source"
    )
)

class TestSpellsService {

    @MockK
    lateinit var spells: MongoCollection<Spell>

    private lateinit var service: SpellsService

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic("kotlin.collections.KMongoIterableKt")

        every { spells.find().toList() } returns spellsList

        this.service = SpellsService(spells)
    }

    @Test
    fun testReadSpells() {
        val receivedSpells = service.readSpells()

        assertEquals(
            spellsList, receivedSpells,
            "Received spells does not correspond to the expected"
        )
    }
}
