package me.khrys.dnd.charcreator.server.mongo

import com.mongodb.client.MongoCollection
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import io.mockk.verify
import kotlin.test.BeforeTest
import kotlin.test.Test
import me.khrys.dnd.charcreator.common.models.User
import me.khrys.dnd.charcreator.server.authentication.EMAIL
import org.litote.kmongo.save

val user = User(EMAIL)

class TestUserService {

    @RelaxedMockK
    lateinit var users: MongoCollection<User>

    private lateinit var service: UserService

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic("org.litote.kmongo.MongoCollectionsKt")

        service = UserService(users)
    }

    @Test
    fun testStoreUser() {
        service.storeUser(user)

        verify { users.save(user) }
    }
}