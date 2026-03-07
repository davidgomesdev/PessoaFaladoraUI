package me.davidgomesdev.pessoafaladora.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import org.jetbrains.compose.resources.painterResource
import pessoafaladora.composeapp.generated.resources.Res
import pessoafaladora.composeapp.generated.resources.logo

@Composable
fun AppTitle() {
    Text(
        "Pessoa Faladora",
        color = Color.White,
        fontWeight = FontWeight(400),
        fontSize = 2.em,
        modifier = Modifier.padding(vertical = 32.dp)
    )
}

@Composable
fun FernandoPessoaLogo(modifier: Modifier = Modifier.Companion) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(Res.drawable.logo),
            contentDescription = "Fernando Pessoa",
            modifier = Modifier
                .sizeIn(maxHeight = 450.dp)
                .padding(top = 8.dp)
                .aspectRatio(0.69f)
                .clip(RoundedCornerShape(16.dp))
        )
    }
}
