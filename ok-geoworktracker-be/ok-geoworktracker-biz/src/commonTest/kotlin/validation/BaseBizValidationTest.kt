package ru.otus.otuskotlin.marketplace.biz.validation

import ru.otus.otuskotlin.marketplace.biz.GwtrTicketProcessor
import ru.otus.otuskotlin.marketplace.common.GwtrCorSettings
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.stubs.GwtrTicketStub

abstract class BaseBizValidationTest {
    protected abstract val command: GwtrCommand
    private val settings by lazy { GwtrCorSettings() }
    protected val processor by lazy { GwtrTicketProcessor(settings) }
}
