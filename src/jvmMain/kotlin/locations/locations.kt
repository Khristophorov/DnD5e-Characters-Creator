@file:Suppress("EXPERIMENTAL_API_USAGE")

package me.khrys.dnd.charcreator.server.locations

import io.ktor.locations.Location
import me.khrys.dnd.charcreator.server.utils.LOGIN_URL
import me.khrys.dnd.charcreator.server.utils.LOGOUT_URL
import me.khrys.dnd.charcreator.server.utils.ROOT_URL

@Location(ROOT_URL)
class Index

@Location(LOGIN_URL)
class Login

@Location(LOGOUT_URL)
class Logout
