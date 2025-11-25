package ru.otus.otuskotlin.marketplace.app.ktor

import ru.otus.otuskotlin.marketplace.app.common.IGwtrAppSettings
import ru.otus.otuskotlin.marketplace.biz.GwtrTicketProcessor
import ru.otus.otuskotlin.marketplace.common.GwtrCorSettings

data class GwtrAppSettings(
    val appUrls: List<String> = emptyList(),
    override val corSettings: GwtrCorSettings = GwtrCorSettings(),
    override val processor: GwtrTicketProcessor = GwtrTicketProcessor(corSettings),
) : IGwtrAppSettings
