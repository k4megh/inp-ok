package ru.otus.otuskotlin.marketplace.api.v2.mappers

import ru.otus.otuskotlin.marketplace.api.v2.models.TicketCreateObject
import ru.otus.otuskotlin.marketplace.api.v2.models.TicketDeleteObject
import ru.otus.otuskotlin.marketplace.api.v2.models.TicketReadObject
import ru.otus.otuskotlin.marketplace.api.v2.models.TicketUpdateObject
import ru.otus.otuskotlin.marketplace.common.models.GwtrTicket
import ru.otus.otuskotlin.marketplace.common.models.GwtrTicketLock

fun GwtrTicket.toTransportCreateTicket() = TicketCreateObject(
    title = title,
    description = description,
    ticketType = ticketType.toTransportTicket(),
    visibility = visibility.toTransportTicket(),
    )

fun GwtrTicket.toTransportReadTicket() = TicketReadObject(
    id = id.toTransportTicket()
)

fun GwtrTicket.toTransportUpdateTicket() = TicketUpdateObject(
    id = id.toTransportTicket(),
    title = title,
    description = description,
    ticketType = ticketType.toTransportTicket(),
    visibility = visibility.toTransportTicket(),
    lock = lock.toTransportTicket(),
)

internal fun GwtrTicketLock.toTransportTicket() = takeIf { it != GwtrTicketLock.NONE }?.asString()

fun GwtrTicket.toTransportDeleteTicket() = TicketDeleteObject(
    id = id.toTransportTicket(),
    lock = lock.toTransportTicket(),
)