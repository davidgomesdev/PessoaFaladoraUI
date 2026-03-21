package me.davidgomesdev.pessoafaladora.ui.service

actual fun getPessoaHost(): String =
    js("window.PESSOA_HOST") as? String ?: (js("window.location.hostname") as String)

actual fun isDevMode(): Boolean =
    (js("window.IS_DEV_MODE") as? String) != "false"
