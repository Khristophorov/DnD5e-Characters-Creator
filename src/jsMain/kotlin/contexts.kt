package me.khrys.dnd.charcreator.client

import me.khrys.dnd.charcreator.common.models.Character
import react.RContext
import react.createContext

val TranslationsContext: RContext<Map<String, String>> = createContext()

val CharactersContext: RContext<Array<Character>> = createContext()
