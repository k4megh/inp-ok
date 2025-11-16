package ru.otus.otuskotlin.marketplace.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.stubs.GwtrStubs

data class GwtrContext(
    var command: GwtrCommand = GwtrCommand.NONE,
    var state: GwtrState = GwtrState.NONE,
    val errors: MutableList<GwtrError> = mutableListOf(),

    var workMode: GwtrWorkMode = GwtrWorkMode.PROD,
    var stubCase: GwtrStubs = GwtrStubs.NONE,

    var requestId: GwtrRequestId = GwtrRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var ticketRequest: GwtrTicket = GwtrTicket(),
    var ticketFilterRequest: GwtrTicketFilter = GwtrTicketFilter(),

    var ticketResponse: GwtrTicket = GwtrTicket(),
    var ticketsResponse: MutableList<GwtrTicket> = mutableListOf(),

    )
