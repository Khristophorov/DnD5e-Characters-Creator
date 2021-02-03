package me.khrys.dnd.charcreator.server.mongo

import com.mongodb.client.MongoCollection
import me.khrys.dnd.charcreator.common.models.User
import org.litote.kmongo.save

class UserService(private val users: MongoCollection<User>) {

    fun storeUser(user: User) {
        users.save(user)
    }
}