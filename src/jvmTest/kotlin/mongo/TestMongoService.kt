package me.khrys.dnd.charcreator.server.mongo

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import io.mockk.verify
import kotlin.test.BeforeTest
import kotlin.test.Test
import me.khrys.dnd.charcreator.common.models.User
import me.khrys.dnd.charcreator.server.authentication.EMAIL
import org.litote.kmongo.getCollection
import org.litote.kmongo.save

val user = User(EMAIL)

class TestMongoService {

    @MockK
    lateinit var client: MongoClient

    @MockK
    lateinit var db: MongoDatabase

    @RelaxedMockK
    lateinit var users: MongoCollection<User>

    private lateinit var service: MongoService

    @BeforeTest
    fun setup() {
        mockkStatic("org.litote.kmongo.MongoDatabasesKt", "org.litote.kmongo.MongoCollectionsKt")
        MockKAnnotations.init(this)

        every { client.getDatabase(DB_NAME) } returns db
        every { db.hint(MongoCollection::class).getCollection<User>() } returns users

        service = MongoService(client)
    }

    @Test
    fun testStoreUser() {
        service.storeUser(user)

        verify { users.save(user) }
    }
}