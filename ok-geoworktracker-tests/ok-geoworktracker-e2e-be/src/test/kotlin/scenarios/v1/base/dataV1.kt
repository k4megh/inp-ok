package ru.otus.otuskotlin.marketplace.e2e.be.scenarios.v1.base

import ru.otus.otuskotlin.marketplace.api.v1.models.*

val someCreateTicket = TicketCreateObject(
    title = "Требуется болт",
    description = "Требуется болт 100x5 с шестигранной шляпкой",
    ticketType = ClaimStatus.WAIT,
    visibility = TicketVisibility.PUBLIC
)
