package me.khrys.dnd.charcreator.server.mongo

import com.mongodb.client.MongoCollection
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.justRun
import io.mockk.mockkStatic
import io.mockk.verify
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.User
import me.khrys.dnd.charcreator.common.models.emptyEquipment
import me.khrys.dnd.charcreator.common.models.initialAbilities
import me.khrys.dnd.charcreator.common.models.initialSavingThrows
import me.khrys.dnd.charcreator.server.authentication.EMAIL
import org.bson.conversions.Bson
import org.litote.kmongo.findOne
import org.litote.kmongo.save
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

val character = Character(
    name = "name",
    image = "image",
    hitPoints = 10,
    abilities = initialAbilities(),
    savingThrows = initialSavingThrows(),
    speed = 10,
    race = "",
    subrace = "",
    equipment = emptyEquipment()
)
val user = User(EMAIL, listOf(character))

class TestUserService {

    @MockK
    lateinit var users: MongoCollection<User>

    private lateinit var service: UserService

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic("org.litote.kmongo.MongoCollectionsKt")

        service = UserService(users)
    }

    @Test
    fun testStoreUserWhenUserDoesNotExist() {
        every { users.findOne(any<Bson>()) } returns null
        justRun { users.save(any()) }

        service.storeUser(user)

        verify { users.save(user) }
        confirmVerified(users)
    }

    @Test
    fun testStoreUserWhenUserExists() {
        every { users.findOne(any<Bson>()) } returns user

        service.storeUser(user)

        confirmVerified(users)
    }

    @Test
    fun testReadCharactersWhenUserIsNotExists() {
        every { users.findOne(any<Bson>()) } returns null

        val characters = service.readCharacters(EMAIL)

        assertTrue(characters.isEmpty(), "Response should be empty")
    }

    @Test
    fun testReadCharactersWhenUserExists() {
        every { users.findOne(any<Bson>()) } returns user

        val characters = service.readCharacters(EMAIL)

        assertEquals(listOf(character), characters, "Incorrect characters received")
    }

    @Test
    fun testStoreCharacter() {
        every { users.findOne(any<Bson>()) } returns user
        justRun { users.findOneAndUpdate(any<Bson>(), any<Bson>()) }

        service.storeCharacter(user._id, character)

        verify { users.findOneAndUpdate(any<Bson>(), any<Bson>()) }
        confirmVerified(users)
    }
}
