package me.khrys.dnd.charcreator.server.mongo

import com.mongodb.client.MongoCollection
import io.ktor.server.sessions.CurrentSession
import me.khrys.dnd.charcreator.common.IMAGE_URL
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.User
import me.khrys.dnd.charcreator.server.retrieveUserName
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.push
import org.litote.kmongo.save
import org.litote.kmongo.setValue

class UserService(private val users: MongoCollection<User>) {

    fun storeUser(user: User) {
        if (users.findOne(User::_id eq user._id) == null) {
            users.save(user)
        }
    }

    fun storeCharacter(userName: String, character: Character) {
        val image = character.image
        val currentCharacters = users.findOne { User::_id eq userName }?.characters ?: emptyList()
        if (image.startsWith(IMAGE_URL)) {
            storeCharacter(currentCharacters, character, userName)
        }
        else {
            val charWithImageName = character.copy(image = "$IMAGE_URL/${character.name}")
            val currentImages = users.findOne { User::_id eq userName }?.images ?: emptyMap()
            storeCharacter(currentCharacters, charWithImageName, userName)
            storeImage(character.name, currentImages, image, userName)
        }
    }

    fun readCharacters(userName: String) =
        users.findOne { User::_id eq userName }?.characters ?: emptyList()

    fun readImage(userName: String, name: String) =
        (users.findOne { User::_id eq userName }?.images ?: emptyMap())[name]

    fun requireUserName(session: CurrentSession) =
        retrieveUserName(session) ?: throw IllegalStateException("Invalid session")

    private fun storeCharacter(
        currentCharacters: List<Character>,
        character: Character,
        userName: String
    ) {
        if (currentCharacters.map(Character::name).contains(character.name)) {
            val newCharacters = currentCharacters.filter { it.name != character.name } + character
            users.findOneAndUpdate(User::_id eq userName, setValue(User::characters, newCharacters))
        } else {
            users.findOneAndUpdate(User::_id eq userName, push(User::characters, character))
        }
    }

    private fun storeImage(
        name: String,
        currentImages: Map<String, String>,
        image: String,
        userName: String
    ) {
        val newImages = currentImages + (name to image)
        users.findOneAndUpdate(User::_id eq userName, setValue(User::images, newImages))
    }
}
