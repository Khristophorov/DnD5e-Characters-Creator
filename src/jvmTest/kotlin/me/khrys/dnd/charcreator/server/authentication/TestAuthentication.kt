package me.khrys.dnd.charcreator.server.authentication

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.OAuthAccessTokenResponse.OAuth2
import io.ktor.server.auth.authentication
import io.ktor.server.response.respondRedirect
import io.ktor.server.sessions.CurrentSession
import io.ktor.server.sessions.sessions
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import io.mockk.verifySequence
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import me.khrys.dnd.charcreator.common.LOGIN_SESSION
import me.khrys.dnd.charcreator.common.LOGIN_URL
import me.khrys.dnd.charcreator.common.ROOT_URL
import me.khrys.dnd.charcreator.server.models.LoginSession

const val VALIDATION_URL = "http://validation-url/"
const val TOKEN = "token"
const val FULL_VALIDATION_URL = VALIDATION_URL + TOKEN
const val EMAIL = "test@mail.com"
const val USER_INFO_JSON = """
    {
        "azp": null,
        "aud": null,
        "sub": null,
        "scope": null,
        "exp": null,
        "expires_in": null,
        "email": "$EMAIL",
        "email_verified": null,
        "access_type": null
    }
"""

class TestAuthentication {

    @RelaxedMockK
    lateinit var call: ApplicationCall

    @MockK
    lateinit var oauth: OAuth2

    @RelaxedMockK
    lateinit var session: CurrentSession

    private val httpClient = HttpClient(MockEngine) {
        install(ContentNegotiation) {
            json()
        }
        engine {
            val headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
            addHandler { request ->
                when (request.url.toString()) {
                    FULL_VALIDATION_URL ->
                        respond(USER_INFO_JSON, headers = headers)
                    else -> error("Unhandled ${request.url}")
                }
            }
        }
    }

    private val loginSession = LoginSession(EMAIL)

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic("io.ktor.server.auth.AuthenticationKt", "io.ktor.server.sessions.SessionDataKt")

        every { call.authentication.principal<OAuth2>() } returns oauth
        every { oauth.accessToken } returns TOKEN
        every { call.sessions } returns session
    }

    @Test
    fun testAuthenticate() {
        runBlocking { authenticate(call, VALIDATION_URL, httpClient) }

        verifySequence {
            call.authentication.principal<OAuth2>()
            oauth.accessToken
            call.sessions
            session.set(LOGIN_SESSION, loginSession)
        }
        coVerify { call.respondRedirect(ROOT_URL) }
        confirmVerified(call, oauth, session)
    }

    @Test
    fun testRetrieveUserNameUseValidUrl() {
        val receivedEmail = runBlocking { retrieveUsername(TOKEN, VALIDATION_URL, httpClient) }

        assertEquals(
            EMAIL, receivedEmail, "Incorrect email value received. Should be $EMAIL"
        )
    }

    @Test
    fun testLogout() {
        runBlocking { logout(call) }

        coVerifySequence {
            session.clear(LOGIN_SESSION)
            call.respondRedirect(LOGIN_URL)
        }
    }
}
