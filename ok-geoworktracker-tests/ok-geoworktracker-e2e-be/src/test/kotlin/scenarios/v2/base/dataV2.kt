package ru.otus.otuskotlin.marketplace.e2e.be.scenarios.v2.base

import ru.otus.otuskotlin.marketplace.api.v2.models.*


val debug = TicketDebug(mode = TicketRequestDebugMode.STUB, stub = TicketRequestDebugStubs.SUCCESS)

val someCreateTicket = TicketCreateObject(
    title = "Требуется болт",
    description = "Требуется болт 100x5 с шестигранной шляпкой",
    ticketType = ClaimStatus.WAIT,
    visibility = TicketVisibility.PUBLIC
)
