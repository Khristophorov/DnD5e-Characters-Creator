package me.khrys.dnd.charcreator.server.mongo

import com.mongodb.client.MongoCollection
import me.khrys.dnd.charcreator.common.models.Character
import me.khrys.dnd.charcreator.common.models.User
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
        val currentCharacters = users.findOne { User::_id eq userName }?.characters ?: emptyList()
        if (currentCharacters.map(Character::name).contains(character.name)) {
            val newCharacters = currentCharacters.filter { it.name != character.name }.toMutableList()
            newCharacters.add(character)
            users.findOneAndUpdate(User::_id eq userName, setValue(User::characters, newCharacters))
        } else {
            users.findOneAndUpdate(User::_id eq userName, push(User::characters, character))
        }
    }

    fun readCharacters(userName: String): List<Character> {
        return users.findOne { User::_id eq userName }?.characters ?: emptyList()
    }
}
