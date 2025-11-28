package ru.otus.otuskotlin.marketplace.biz.validation

import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.helpers.errorValidation
import ru.otus.otuskotlin.marketplace.common.helpers.fail
import ru.otus.otuskotlin.marketplace.common.models.GwtrTicketLock
import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker

fun ICorChainDsl<GwtrContext>.validateLockProperFormat(title: String) = worker {
    this.title = title

    // Может быть вынесен в GwtrTicketId для реализации различных форматов
    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { ticketValidating.lock != GwtrTicketLock.NONE && !ticketValidating.lock.asString().matches(regExp) }
    handle {
        val encodedId = ticketValidating.lock.asString()
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = "value $encodedId must contain only"
            )
        )
    }
}
