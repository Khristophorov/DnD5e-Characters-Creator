package me.khrys.dnd.charcreator.client

import me.khrys.dnd.charcreator.common.models.Character
import react.Context
import react.createContext

val TranslationsContext: Context<Map<String, String>> = createContext()

val CharactersContext: Context<List<Character>> = createContext()
