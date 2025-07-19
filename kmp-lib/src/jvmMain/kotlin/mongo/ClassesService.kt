package me.khrys.dnd.charcreator.server.mongo

import com.mongodb.client.MongoCollection
import me.khrys.dnd.charcreator.common.models.Class

class ClassesService(private val classes: MongoCollection<Class>) {
    fun readClasses() = classes.find().toList()
}
