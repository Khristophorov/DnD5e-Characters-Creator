package me.khrys.dnd.charcreator.server.mongo

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import me.khrys.dnd.charcreator.common.models.Translation
import me.khrys.dnd.charcreator.common.models.User
import org.litote.kmongo.getCollection

const val DB_NAME = "dnd"

class MongoServiceFactory(client: MongoClient) {

    private val db: MongoDatabase = client.getDatabase(DB_NAME)

    fun getUsers():MongoCollection<User> = db.getCollection()

    fun getTranslations():MongoCollection<Translation> = db.getCollection()
}