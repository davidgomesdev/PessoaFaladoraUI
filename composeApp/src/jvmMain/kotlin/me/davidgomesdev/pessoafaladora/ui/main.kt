package me.davidgomesdev.pessoafaladora.ui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Pessoa Faladora",
        alwaysOnTop = false,
    ) {
        App()
    }
}
