package me.davidgomesdev.pessoafaladora.ui.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.dp
import me.davidgomesdev.pessoafaladora.ui.componentsBackgroundColor
import me.davidgomesdev.pessoafaladora.ui.focusedIndicatorColor
import me.davidgomesdev.pessoafaladora.ui.isActionInputType

@Composable
fun ThinkQueryTextField(
    state: TextFieldState,
    isLoading: Boolean,
    onSubmit: () -> Unit
) {
    TextField(
        state,
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
}

@Composable
fun ThinkButton(onSubmit: () -> Unit, isLoading: Boolean) {
    Button(
        onClick = onSubmit,
        enabled = !isLoading,
        modifier = Modifier.padding(vertical = 16.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            disabledContainerColor = Color.DarkGray,
            disabledContentColor = Color.Gray
        )
    ) {
        Text(if (isLoading) "A pensar..." else "Pensar")
    }
}
