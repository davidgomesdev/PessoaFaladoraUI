package me.davidgomesdev.pessoafaladora.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.painterResource
import pessoafaladora.composeapp.generated.resources.Res
import pessoafaladora.composeapp.generated.resources.logo


@Composable
@Preview
fun App() {
    val backgroundColor = Color(0xFF111111)
    val componentsBackgroundColor = Color(0xFF323232)
    val componentColumnBackgroundColor = Color(0xFF242424)
    val focusedIndicatorColor = Color(0xFF575757)
    val httpClient = HttpClient {
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

    MaterialTheme(typography = RobotoTypography()) {
        val textFieldState = remember { TextFieldState("") }
        var isLoading by remember { mutableStateOf(false) }
        var response by remember { mutableStateOf("") }
        val scrollState = rememberScrollState()
        val coroutineScope = rememberCoroutineScope()

        val onSubmit: () -> Unit = {
            coroutineScope.launch {
                isLoading = true
                try {
                    val result = httpClient.put("http://l3n:8080/pensa") {
                        accept(ContentType.Any)
                        contentType(ContentType.Application.Json)
                        setBody(ThinkPayload(textFieldState.text.toString()))
                    }
                    response = result.bodyAsText()
                } catch (e: Exception) {
                    Napier.e("Request failed", e)
                    response = "Ho-oh, este não é o Pessoa a falar \uD83D\uDE2C. Ocorreu um erro!"
                } finally {
                    isLoading = false
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .background(backgroundColor)
                .fillMaxSize()
                .sizeIn(maxWidth = 700.dp)
                .verticalScroll(scrollState)
        ) {
            Title()
            Row(
                modifier = Modifier.requiredWidthIn(min = 320.dp).padding(32.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                FernandoPessoaLogo(Modifier.weight(1f))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(2f)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(componentColumnBackgroundColor),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    TextField(
                        textFieldState,
                        placeholder = {
                            Text(
                                "Escreve o que te inquieta a alma...",
                                color = Color.White.copy(alpha = 0.5f)
                            )
                        },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .onPreviewKeyEvent { keyEvent ->
                                if (isActionInputType(keyEvent) && !isLoading) {
                                    onSubmit()
                                    true
                                } else {
                                    false
                                }
                            },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = componentsBackgroundColor,
                            unfocusedContainerColor = componentsBackgroundColor,
                            focusedIndicatorColor = focusedIndicatorColor,
                            unfocusedIndicatorColor = focusedIndicatorColor.copy(alpha = 0.7f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White,
                        )
                    )
                    Button(
                        onClick = onSubmit,
                        enabled = !isLoading,
                        modifier = Modifier.padding(vertical = 16.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(contentColor = Color.White)
                    ) {
                        Text(if (isLoading) "A pensar..." else "Pensar")
                    }
                    SelectionContainer {
                        Text(
                            response,
                            color = Color.White,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .background(componentsBackgroundColor)
                                .defaultMinSize(minHeight = 120.dp)
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun isActionInputType(keyEvent: KeyEvent): Boolean =
    (keyEvent.isCtrlPressed || keyEvent.isMetaPressed) &&
            keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyDown

@Composable
fun Title() {
    Text(
        "Pessoa Faladora",
        color = Color.White,
        fontWeight = FontWeight(400),
        fontSize = 2.em,
        modifier = Modifier.padding(vertical = 32.dp)
    )
}

@Composable
fun FernandoPessoaLogo(modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(Res.drawable.logo),
            contentDescription = "Fernando Pessoa",
            modifier = Modifier
                .sizeIn(maxHeight = 320.dp)
                .aspectRatio(0.69f)
                .clip(RoundedCornerShape(16.dp))
        )
    }
}

@Serializable
data class ThinkPayload(val input: String)
