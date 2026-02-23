package me.davidgomesdev.pessoafaladora.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
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

    MaterialTheme(typography = RobotoTypography()) {
        var showContent by remember { mutableStateOf(false) }
        var input by remember { mutableStateOf("") }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.background(backgroundColor).fillMaxSize()
                .sizeIn(maxWidth = 700.dp)
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
                        .weight(1f)
                        .padding(4.dp)
                        .background(componentColumnBackgroundColor),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    TextField(
                        TextFieldState(input),
                        placeholder = {
                            Text(
                                "Escreve o que te inquieta a alma...",
                                color = Color.White.copy(alpha = 0.5f)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(8.dp)),
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
                        onClick = { showContent = !showContent },
                        modifier = Modifier.padding(vertical = 16.dp),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text("Pensar", color = Color.White)
                    }
                    Text(
                        "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(componentsBackgroundColor)
                            .defaultMinSize(minHeight = 120.dp)
                    )
                }
            }
        }
    }
}

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
    Image(
        painter = painterResource(Res.drawable.logo),
        contentDescription = "Fernando Pessoa",
        modifier = modifier.fillMaxSize().padding(16.dp).clip(RoundedCornerShape(32.dp))
    )
}
