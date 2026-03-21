package me.davidgomesdev.pessoafaladora.ui.model

import me.davidgomesdev.pessoafaladora.ui.dto.ChatEvent

data class Source(
    val id: String,
    val title: String,
    val author: String,
    val category: String,
    val score: Int,
) {
    companion object {
        fun from(event: ChatEvent.Sources.Source) =
            Source(
                event.id,
                event.title,
                event.author,
                event.category,
                event.score,
            )
    }
}

