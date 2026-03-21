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
import me.davidgomesdev.pessoafaladora.ui.service.ThinkAPI
import me.davidgomesdev.pessoafaladora.ui.widget.AppHeader
import me.davidgomesdev.pessoafaladora.ui.widget.Persona
import me.davidgomesdev.pessoafaladora.ui.widget.PersonaSidebar
import me.davidgomesdev.pessoafaladora.ui.widget.ResponseCard
import me.davidgomesdev.pessoafaladora.ui.widget.ThinkInputCard

data class PessoaResponse(var sources: String = "", var message: String = "") {
    fun clear() {
        sources = ""
        message = ""
    }
}

@Composable
@Preview
fun App() {
    val thinkAPI = ThinkAPI()

    MaterialTheme(typography = RobotoTypography()) {
        val textFieldState = remember { TextFieldState("") }
        var isLoading by remember { mutableStateOf(false) }
        var response by remember { mutableStateOf(PessoaResponse()) }
        var selectedPersona by remember { mutableStateOf(Persona.FERNANDO_PESSOA) }
        val scrollState = rememberScrollState()
        val coroutineScope = rememberCoroutineScope()

        val onSubmit: () -> Unit = onSubmit@{
            if (textFieldState.text.isBlank()) return@onSubmit

            coroutineScope.launch {
                isLoading = true
                response.clear()

                thinkAPI.sendThinkRequest(textFieldState.text.toString().trim()).onCompletion {
                    isLoading = false
                }.collect {
                    if (it.contains("<sources>")) {
                        response.sources = it
                            .removePrefix("<sources>")
                            .removeSuffix("</sources>")

                        return@collect
                    }

                    response.message += it
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
            AppHeader()
            Row(
                modifier = Modifier
                    .requiredWidthIn(min = 320.dp)
                    .padding(horizontal = 32.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                PersonaSidebar(
                    selectedPersona = selectedPersona,
                    onPersonaSelected = { selectedPersona = it },
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
    responseSources: String,
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
                    sources = responseSources,
                    isLoading = isLoading
                )
            }
        }
    }
}
