package ru.otus.otuskotlin.marketplace.common.models

import ru.otus.otuskotlin.marketplace.logging.common.LogLevel


data class GwtrError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: LogLevel = LogLevel.ERROR,
    val exception: Throwable? = null,
)
