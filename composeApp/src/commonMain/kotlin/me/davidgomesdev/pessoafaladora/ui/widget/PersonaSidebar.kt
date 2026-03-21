package me.davidgomesdev.pessoafaladora.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.davidgomesdev.pessoafaladora.ui.componentColumnBackgroundColor
import me.davidgomesdev.pessoafaladora.ui.focusedIndicatorColor
import me.davidgomesdev.pessoafaladora.ui.selectedPersonaChipBorderColor
import me.davidgomesdev.pessoafaladora.ui.selectedPersonaChipColor
import me.davidgomesdev.pessoafaladora.ui.selectedPersonaChipTextColor

enum class PersonaCategory(val label: String) {
    ORTONIMO("Ortónimo"),
    HETERONIMO("Heterónimos"),
    SEMI_HETERONIMO("Semi-heterónimo")
}

enum class Persona(val displayName: String, val category: PersonaCategory) {
    FERNANDO_PESSOA("Fernando Pessoa", PersonaCategory.ORTONIMO),
    ALVARO_DE_CAMPOS("Álvaro de Campos", PersonaCategory.HETERONIMO),
    RICARDO_REIS("Ricardo Reis", PersonaCategory.HETERONIMO),
    ALBERTO_CAEIRO("Alberto Caeiro", PersonaCategory.HETERONIMO),
    BERNARDO_SOARES("Bernardo Soares", PersonaCategory.SEMI_HETERONIMO),
}

@Composable
fun PersonaSidebar(
    selectedPersona: Persona,
    onPersonaSelected: (Persona) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FernandoPessoaLogo()

        Spacer(Modifier.height(16.dp))

        val categories = PersonaCategory.entries
        categories.forEachIndexed { index, category ->
            val personasInCategory = Persona.entries.filter { it.category == category }

            PersonaSection(
                label = category.label,
                personas = personasInCategory,
                selectedPersona = selectedPersona,
                onPersonaSelected = onPersonaSelected
            )

            if (index < categories.lastIndex) {
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun PersonaSection(
    label: String,
    personas: List<Persona>,
    selectedPersona: Persona,
    onPersonaSelected: (Persona) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            label.uppercase(),
            color = focusedIndicatorColor.copy(alpha = 0.7f),
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.5.sp,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )
        personas.forEach { persona ->
            PersonaChip(
                persona = persona,
                isSelected = persona == selectedPersona,
                onSelected = { onPersonaSelected(persona) }
            )
        }
    }
}

@Composable
private fun PersonaChip(
    persona: Persona,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    val isOrtonimo = persona.category == PersonaCategory.ORTONIMO

    val bgColor = when {
        isSelected && isOrtonimo -> selectedPersonaChipColor
        isSelected -> componentColumnBackgroundColor
        else -> Color.Transparent
    }
    val borderColor = when {
        isSelected && isOrtonimo -> selectedPersonaChipBorderColor
        isSelected -> focusedIndicatorColor
        else -> focusedIndicatorColor.copy(alpha = 0.3f)
    }
    val textColor = when {
        isSelected && isOrtonimo -> selectedPersonaChipTextColor
        isSelected -> Color.White
        else -> Color.White.copy(alpha = 0.4f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(6.dp))
            .clickable(onClick = onSelected)
            .padding(horizontal = 10.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            persona.displayName,
            color = textColor,
            fontSize = 11.sp,
        )
    }
}

