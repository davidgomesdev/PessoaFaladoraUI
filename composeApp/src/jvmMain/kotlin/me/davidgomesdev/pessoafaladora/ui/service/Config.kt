package me.davidgomesdev.pessoafaladora.ui.service

actual fun getPessoaHost(): String =
    System.getenv("PESSOA_HOST") ?: DEFAULT_PESSOA_HOST
