package me.khrys.dnd.charcreator.server.mongo

import com.mongodb.client.MongoClient
import me.khrys.dnd.charcreator.common.models.User
import org.litote.kmongo.getCollection
import org.litote.kmongo.save

const val DB_NAME = "dnd"

class MongoService(client: MongoClient) {
    private val db = client.getDatabase(DB_NAME)
    private val users = db.getCollection<User>()

    fun storeUser(user: User) {
        users.save(user)
    }
}