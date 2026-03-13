package me.davidgomesdev.pessoafaladora.ui.service

actual fun getPessoaHost(): String =
    js("window.PESSOA_HOST") as? String ?: (js("window.location.hostname") as String)
