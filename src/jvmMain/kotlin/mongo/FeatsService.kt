package me.khrys.dnd.charcreator.server.mongo

import com.mongodb.client.MongoCollection
import me.khrys.dnd.charcreator.common.models.Feat

class FeatsService (private val feats: MongoCollection<Feat>) {

    fun readFeats() = feats.find().toList()
}