package me.khrys.dnd.charcreator.server.mongo

import com.mongodb.client.MongoCollection
import me.khrys.dnd.charcreator.common.models.Maneuver

class ManeuversService (private val maneuvers: MongoCollection<Maneuver>) {
    fun readManeuvers() = maneuvers.find().toList()
}
