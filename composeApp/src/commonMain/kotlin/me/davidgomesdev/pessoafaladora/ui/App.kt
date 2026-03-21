package me.davidgomesdev.pessoafaladora.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import me.davidgomesdev.pessoafaladora.ui.dto.ChatEvent
import me.davidgomesdev.pessoafaladora.ui.model.Persona
import me.davidgomesdev.pessoafaladora.ui.model.Source
import me.davidgomesdev.pessoafaladora.ui.service.ThinkAPI
import me.davidgomesdev.pessoafaladora.ui.service.isDevMode
import me.davidgomesdev.pessoafaladora.ui.widget.AppHeader
import me.davidgomesdev.pessoafaladora.ui.widget.PersonaSidebar
import me.davidgomesdev.pessoafaladora.ui.widget.ResponseCard
import me.davidgomesdev.pessoafaladora.ui.widget.ThinkInputCard

data class PessoaResponse(val sources: List<Source> = emptyList(), val message: String = "")

@Composable
@Preview
fun App() {
    val thinkAPI = ThinkAPI()

    MaterialTheme(typography = RobotoTypography()) {
        val isDevModeEnabled = remember { isDevMode() }
        val textFieldState = remember { TextFieldState("") }
        var isLoading by remember { mutableStateOf(false) }
        var response by remember { mutableStateOf(PessoaResponse()) }
        var selectedPersona by remember { mutableStateOf(Persona.FERNANDO_PESSOA) }
        var devMode by remember { mutableStateOf(false) }
        val scrollState = rememberScrollState()
        val coroutineScope = rememberCoroutineScope()

        val onDevModeToggle: () -> Unit = {
            devMode = !devMode
            if (!devMode && selectedPersona == Persona.O_FINGIDOR) {
                selectedPersona = Persona.FERNANDO_PESSOA
            }
        }

        val onSubmit: () -> Unit = onSubmit@{
            if (textFieldState.text.isBlank()) return@onSubmit

            coroutineScope.launch {
                isLoading = true
                response = PessoaResponse()

                thinkAPI.sendThinkRequest(
                    query = textFieldState.text.toString().trim(),
                    persona = selectedPersona
                ).onCompletion {
                    isLoading = false
                }.collect { event ->
                    when (event) {
                        is ChatEvent.Token -> response = response.copy(message = response.message + event.value)
                        is ChatEvent.Sources -> response = response.copy(
                            sources = event.items.map { Source(it.title, it.author, it.category, it.score) }
                        )

                        is ChatEvent.Done -> {
                            println("Completed")
                        }
                    }
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
            AppHeader(
                devMode = devMode,
                onDevModeToggle = if (isDevModeEnabled) onDevModeToggle else null
            )
            Row(
                modifier = Modifier
                    .requiredWidthIn(min = 320.dp)
                    .padding(horizontal = 32.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                PersonaSidebar(
                    selectedPersona = selectedPersona,
                    onPersonaSelected = { selectedPersona = it },
                    devMode = devMode,
                    modifier = Modifier.weight(1f)
                )
                ThinkForm(textFieldState, isLoading, onSubmit, response.sources, response.message)
            }
        }
    }
}

@Composable
fun RowScope.ThinkForm(
    textFieldState: TextFieldState,
    isLoading: Boolean,
    onSubmit: () -> Unit,
    sources: List<Source>,
    response: String
) {
    Column(
        modifier = Modifier
            .weight(2f)
            .padding(start = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ThinkInputCard(textFieldState, isLoading, onSubmit)

        if (isLoading || response.isNotBlank()) {
            SelectionContainer {
                ResponseCard(
                    response = response,
                    sources = sources,
                    isLoading = isLoading
                )
            }
        }
    }
}
