package me.davidgomesdev.pessoafaladora.ui.service

actual fun getPessoaUrl(): String =
    js("window.PESSOA_URL") as? String ?: DEFAULT_PESSOA_URL
