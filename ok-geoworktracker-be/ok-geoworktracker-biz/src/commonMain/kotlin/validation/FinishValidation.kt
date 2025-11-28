package ru.otus.otuskotlin.marketplace.biz.validation

import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.models.GwtrState
import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker

fun ICorChainDsl<GwtrContext>.finishTicketValidation(title: String) = worker {
    this.title = title
    on { state == GwtrState.RUNNING }
    handle {
        ticketValidated = ticketValidating
    }
}

fun ICorChainDsl<GwtrContext>.finishTicketFilterValidation(title: String) = worker {
    this.title = title
    on { state == GwtrState.RUNNING }
    handle {
        ticketFilterValidated = ticketFilterValidating
    }
}
