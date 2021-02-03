package me.khrys.dnd.charcreator.server.mongo

import com.mongodb.client.MongoCollection
import me.khrys.dnd.charcreator.common.models.Translation

class TranslationService(private val translations: MongoCollection<Translation>) {

    fun readTranslations(): List<Translation> {
        return translations.find().toList()
    }
}