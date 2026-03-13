package me.davidgomesdev.pessoafaladora.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import me.davidgomesdev.pessoafaladora.ui.service.ThinkAPI
import me.davidgomesdev.pessoafaladora.ui.widget.AppTitle
import me.davidgomesdev.pessoafaladora.ui.widget.FernandoPessoaLogo
import me.davidgomesdev.pessoafaladora.ui.widget.ThinkButton
import me.davidgomesdev.pessoafaladora.ui.widget.ThinkQueryTextField

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
            AppTitle()
            Row(
                modifier = Modifier.requiredWidthIn(min = 320.dp).padding(32.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                FernandoPessoaLogo(Modifier.weight(1f))
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
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .weight(2f)
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(componentColumnBackgroundColor),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ThinkQueryTextField(textFieldState, isLoading, onSubmit)
        ThinkButton(onSubmit, isLoading)
        SelectionContainer {
            Column() {
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
                Text(
                    "Fontes usadas:\n$responseSources",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 120.dp)
                )
            }
        }
    }
}
