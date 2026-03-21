package me.davidgomesdev.pessoafaladora.ui.service

actual fun getPessoaHost(): String =
    js("window.PESSOA_HOST") as? String ?: (js("window.location.hostname") as String)

actual fun isDevMode(): Boolean =
    (js("window.IS_DEV_MODE") as? String) != "false"

actual fun isMobileDevice(): Boolean {
    val userAgent = js("navigator.userAgent") as String
    return userAgent.contains(Regex("Mobile|Android|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini"))
}

