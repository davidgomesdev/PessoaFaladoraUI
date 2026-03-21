package me.davidgomesdev.pessoafaladora.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.davidgomesdev.pessoafaladora.ui.cardBorderColor
import me.davidgomesdev.pessoafaladora.ui.devChipBorderColor
import me.davidgomesdev.pessoafaladora.ui.devChipColor
import me.davidgomesdev.pessoafaladora.ui.devChipTextColor
import org.jetbrains.compose.resources.painterResource
import pessoafaladora.composeapp.generated.resources.Res
import pessoafaladora.composeapp.generated.resources.logo

@Composable
fun AppHeader(devMode: Boolean, onDevModeToggle: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Pessoa Faladora",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
                Text(
                    "Fala com Fernando Pessoa",
                    color = Color.White.copy(alpha = 0.35f),
                    fontSize = 11.sp,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
            DevModeToggle(active = devMode, onToggle = onDevModeToggle)
        }
        HorizontalDivider(color = cardBorderColor, thickness = 1.dp)
    }
}

@Composable
private fun DevModeToggle(active: Boolean, onToggle: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(if (active) devChipColor else Color.Transparent)
            .border(
                1.dp,
                if (active) devChipBorderColor else Color.White.copy(alpha = 0.1f),
                RoundedCornerShape(4.dp)
            )
            .clickable(onClick = onToggle)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            "Modo DEV",
            color = if (active) devChipTextColor else Color.White.copy(alpha = 0.2f),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.5.sp
        )
    }
}

@Composable
fun FernandoPessoaLogo(modifier: Modifier = Modifier.Companion) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(Res.drawable.logo),
            contentDescription = "Fernando Pessoa",
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .aspectRatio(0.69f)
                .clip(RoundedCornerShape(16.dp))
        )
    }
}
