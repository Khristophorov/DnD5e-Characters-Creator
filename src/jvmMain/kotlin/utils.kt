package me.khrys.dnd.charcreator.server

import io.ktor.sessions.CurrentSession
import me.khrys.dnd.charcreator.common.LOGIN_SESSION
import me.khrys.dnd.charcreator.server.models.LoginSession

fun retrieveUserName(session: CurrentSession): String? = (session.get(LOGIN_SESSION) as LoginSession?)?.username
