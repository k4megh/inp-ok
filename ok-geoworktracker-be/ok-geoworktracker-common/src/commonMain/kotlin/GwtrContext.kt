package ru.otus.otuskotlin.marketplace.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.repo.IRepoTicket
import ru.otus.otuskotlin.marketplace.common.stubs.GwtrStubs
import ru.otus.otuskotlin.marketplace.common.ws.IGwtrWsSession


data class GwtrContext(
    var command: GwtrCommand = GwtrCommand.NONE,
    var state: GwtrState = GwtrState.NONE,
    val errors: MutableList<GwtrError> = mutableListOf(),

    var corSettings: GwtrCorSettings = GwtrCorSettings(),
    var workMode: GwtrWorkMode = GwtrWorkMode.PROD,
    var stubCase: GwtrStubs = GwtrStubs.NONE,
    var wsSession: IGwtrWsSession = IGwtrWsSession.NONE,

    var requestId: GwtrRequestId = GwtrRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var ticketRequest: GwtrTicket = GwtrTicket(),
    var ticketFilterRequest: GwtrTicketFilter = GwtrTicketFilter(),
 
    var ticketValidating: GwtrTicket = GwtrTicket(),
    var ticketFilterValidating: GwtrTicketFilter = GwtrTicketFilter(),

    var ticketValidated: GwtrTicket = GwtrTicket(),
    var ticketFilterValidated: GwtrTicketFilter = GwtrTicketFilter(),
 
    var ticketRepo: IRepoTicket = IRepoTicket.NONE,
    var ticketRepoRead: GwtrTicket = GwtrTicket(), // То, что прочитали из репозитория
    var ticketRepoPrepare: GwtrTicket = GwtrTicket(), // То, что готовим для сохранения в БД
    var ticketRepoDone: GwtrTicket = GwtrTicket(),  // Результат, полученный из БД
    var ticketsRepoDone: MutableList<GwtrTicket> = mutableListOf(),

    var ticketResponse: GwtrTicket = GwtrTicket(),
    var ticketsResponse: MutableList<GwtrTicket> = mutableListOf(),
 
    )
