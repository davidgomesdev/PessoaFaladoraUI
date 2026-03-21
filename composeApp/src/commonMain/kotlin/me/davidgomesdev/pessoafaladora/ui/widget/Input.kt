package me.davidgomesdev.pessoafaladora.ui.widget

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.davidgomesdev.pessoafaladora.ui.cardBorderColor
import me.davidgomesdev.pessoafaladora.ui.componentsBackgroundColor
import me.davidgomesdev.pessoafaladora.ui.focusedIndicatorColor
import me.davidgomesdev.pessoafaladora.ui.inputCardBackgroundColor
import me.davidgomesdev.pessoafaladora.ui.isActionInputType
import me.davidgomesdev.pessoafaladora.ui.service.isMobileDevice

@Composable
fun ThinkInputCard(
    state: TextFieldState,
    isLoading: Boolean,
    onSubmit: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val borderColor by animateColorAsState(
        targetValue = if (isFocused) focusedIndicatorColor else cardBorderColor
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(inputCardBackgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
    ) {
        TextField(
            state = state,
            placeholder = {
                Text(
                    "Escreve o que te inquieta a alma...",
                    color = Color.White.copy(alpha = 0.35f),
                    fontSize = 14.sp
                )
            },
            enabled = !isLoading,
            lineLimits = TextFieldLineLimits.MultiLine(minHeightInLines = 3),
            interactionSource = interactionSource,
            modifier = Modifier
                .fillMaxWidth()
                .onPreviewKeyEvent { keyEvent ->
                    if (isActionInputType(keyEvent) && !isLoading) {
                        onSubmit()
                        true
                    } else {
                        false
                    }
                },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                disabledTextColor = Color.White.copy(alpha = 0.4f),
                cursorColor = Color.White,
            )
        )
        HorizontalDivider(color = cardBorderColor, thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = if (isMobileDevice()) Arrangement.Center else Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isMobileDevice()) {
                Text(
                    "Control + Enter para enviar",
                    color = Color.White.copy(alpha = 0.25f),
                    fontSize = 11.sp
                )
            }
            ThinkButton(onSubmit, isLoading)
        }
    }
}

@Composable
fun ThinkButton(onSubmit: () -> Unit, isLoading: Boolean) {
    Button(
        onClick = onSubmit,
        enabled = !isLoading,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = componentsBackgroundColor,
            contentColor = Color.White,
            disabledContainerColor = inputCardBackgroundColor,
            disabledContentColor = Color.White.copy(alpha = 0.3f)
        )
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Always reserve space for the wider label so the button never resizes
            Text("A pensar…", fontSize = 13.sp, modifier = Modifier.alpha(0f))
            Text(
                if (isLoading) "A pensar…" else "Pensar",
                fontSize = 13.sp
            )
        }
    }
}
