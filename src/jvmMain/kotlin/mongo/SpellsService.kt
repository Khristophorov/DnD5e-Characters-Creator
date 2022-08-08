package me.khrys.dnd.charcreator.server.mongo

import com.mongodb.client.MongoCollection
import me.khrys.dnd.charcreator.common.models.Spell

class SpellsService(private val spells: MongoCollection<Spell>) {

    fun readSpells() = spells.find().toList()
}
