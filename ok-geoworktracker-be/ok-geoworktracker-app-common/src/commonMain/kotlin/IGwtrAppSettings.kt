package ru.otus.otuskotlin.marketplace.app.common

import ru.otus.otuskotlin.marketplace.biz.GwtrTicketProcessor
import ru.otus.otuskotlin.marketplace.common.GwtrCorSettings

interface IGwtrAppSettings {
    val processor: GwtrTicketProcessor
    val corSettings: GwtrCorSettings
}
