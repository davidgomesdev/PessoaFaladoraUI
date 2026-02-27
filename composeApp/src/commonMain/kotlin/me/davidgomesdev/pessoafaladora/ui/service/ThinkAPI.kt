package me.davidgomesdev.pessoafaladora.ui.service

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.preparePut
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.flow.channelFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

const val pessoaUrl = "http://l3n:8080/pensa"

class ThinkAPI {
    private val client = HttpClient {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.v("HTTP Client", null, message)
                }
            }
            level = LogLevel.HEADERS
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
            })
        }
    }.also { Napier.base(DebugAntilog()) }

    fun sendThinkRequest(
        query: String
    ) = channelFlow {
        try {
            client.preparePut(pessoaUrl) {
                accept(ContentType.Any)
                contentType(ContentType.Application.Json)
                setBody(ThinkPayload(query))
            }.execute { httpResponse ->
                val channel: ByteReadChannel = httpResponse.body()
                while (!channel.isClosedForRead) {
                    val buffer = ByteArray(4096)

                    channel.readAvailable(buffer)

                    val trimmed = buffer
                        // removes extra bytes in the buffer (since the size is so big)
                        .dropLastWhile { it == 0.toByte() }
                        .toByteArray().decodeToString()

                    send(trimmed)
                }
            }
        } catch (e: Exception) {
            Napier.e("Request failed", e)
            send("Ho-oh, este não é o Pessoa a falar \uD83D\uDE2C. Ocorreu um erro!")
        }
    }
}

@Serializable
data class ThinkPayload(val input: String)
