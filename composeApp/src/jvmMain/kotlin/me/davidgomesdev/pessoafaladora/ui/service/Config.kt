package me.davidgomesdev.pessoafaladora.ui.service

actual fun getPessoaUrl(): String =
    System.getenv("PESSOA_URL") ?: DEFAULT_PESSOA_URL
