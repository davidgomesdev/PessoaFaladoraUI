package me.davidgomesdev.pessoafaladora.ui.service

actual fun getPessoaHost(): String =
    System.getenv("PESSOA_HOST") ?: DEFAULT_PESSOA_HOST

actual fun isDevMode(): Boolean =
    System.getenv("IS_DEV_MODE") != "false"

actual fun isMobileDevice(): Boolean = false

