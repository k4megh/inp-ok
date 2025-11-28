package ru.otus.otuskotlin.marketplace.biz.stubs

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.GwtrCorSettings
import ru.otus.otuskotlin.marketplace.common.models.GwtrState
import ru.otus.otuskotlin.marketplace.common.stubs.GwtrStubs
import ru.otus.otuskotlin.marketplace.logging.common.LogLevel
import ru.otus.otuskotlin.marketplace.stubs.GwtrTicketStub

fun ICorChainDsl<GwtrContext>.stubDeleteSuccess(title: String, corSettings: GwtrCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для удаления объявления
    """.trimIndent()
    on { stubCase == GwtrStubs.SUCCESS && state == GwtrState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubOffersSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = GwtrState.FINISHING
            val stub = GwtrTicketStub.prepareResult {
                ticketRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            }
            ticketResponse = stub
        }
    }
}
