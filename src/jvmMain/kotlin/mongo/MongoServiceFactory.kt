package me.khrys.dnd.charcreator.server.mongo

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import me.khrys.dnd.charcreator.common.models.Class
import me.khrys.dnd.charcreator.common.models.Feat
import me.khrys.dnd.charcreator.common.models.Maneuver
import me.khrys.dnd.charcreator.common.models.Race
import me.khrys.dnd.charcreator.common.models.Spell
import me.khrys.dnd.charcreator.common.models.Translation
import me.khrys.dnd.charcreator.common.models.User
import org.litote.kmongo.getCollection

const val DB_NAME = "dnd"

class MongoServiceFactory(client: MongoClient) {

    private val db: MongoDatabase = client.getDatabase(DB_NAME)

    fun getUsers(): MongoCollection<User> = db.getCollection()

    fun getTranslations(): MongoCollection<Translation> = db.getCollection()

    fun getRaces(): MongoCollection<Race> = db.getCollection()

    fun getClasses(): MongoCollection<Class> = db.getCollection()

    fun getFeats(): MongoCollection<Feat> = db.getCollection()

    fun getManeuvers(): MongoCollection<Maneuver> = db.getCollection()

    fun getSpells(): MongoCollection<Spell> = db.getCollection()
}
