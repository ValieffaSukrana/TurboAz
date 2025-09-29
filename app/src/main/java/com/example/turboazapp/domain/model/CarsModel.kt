package com.example.turboazapp.domain.model

data class Make(
    val make_id: String = "",
    val make_display: String = ""
)

data class MakesResponse(
    val Makes: List<Make> = emptyList()
)
