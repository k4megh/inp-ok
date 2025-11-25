package ru.otus.otuskotlin.marketplace.app.ktor.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.app.ktor.GwtrAppSettings
import ru.otus.otuskotlin.marketplace.app.ktor.base.KtorWsSessionRepo
//import ru.otus.otuskotlin.marketplace.biz.GwtrTicketProcessor
import ru.otus.otuskotlin.marketplace.common.GwtrCorSettings

fun Application.initAppSettings(): GwtrAppSettings {
    val corSettings = GwtrCorSettings(
        loggerProvider = getLoggerProviderConf(),
        wsSessions = KtorWsSessionRepo(),
    )
    return GwtrAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        //processor = GwtrTicketProcessor(corSettings),
    )
}
