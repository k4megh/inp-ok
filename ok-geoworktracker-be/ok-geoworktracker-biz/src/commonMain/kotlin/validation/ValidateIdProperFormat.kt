package ru.otus.otuskotlin.marketplace.biz.validation

import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.helpers.errorValidation
import ru.otus.otuskotlin.marketplace.common.helpers.fail
import ru.otus.otuskotlin.marketplace.common.models.GwtrTicketId
import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker

fun ICorChainDsl<GwtrContext>.validateIdProperFormat(title: String) = worker {
    this.title = title

    // Может быть вынесен в GwtrAdId для реализации различных форматов
    val regExp = Regex("^[0-9a-zA-Z#:-]+$")
    on { ticketValidating.id != GwtrTicketId.NONE && !ticketValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = ticketValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}
