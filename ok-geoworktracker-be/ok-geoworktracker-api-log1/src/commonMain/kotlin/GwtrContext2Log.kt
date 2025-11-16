package ru.otus.otuskotlin.marketplace.api.log1.mapper

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.marketplace.api.log1.models.*
import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.models.*

fun GwtrContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "ok-geoworktracker",
    ticket = toGwtrLog(),
    errors = errors.map { it.toLog() },
)

private fun GwtrContext.toGwtrLog(): GwtrLogModel? {
    val ticketNone = GwtrTicket()
    return GwtrLogModel(
        requestId = requestId.takeIf { it != GwtrRequestId.NONE }?.asString(),
        requestTicket = ticketRequest.takeIf { it != ticketNone }?.toLog(),
        responseTicket = ticketResponse.takeIf { it != ticketNone }?.toLog(),
        responseTickets = ticketsResponse.takeIf { it.isNotEmpty() }?.filter { it != ticketNone }?.map { it.toLog() },
        requestFilter = ticketFilterRequest.takeIf { it != GwtrTicketFilter() }?.toLog(),
    ).takeIf { it != GwtrLogModel() }
}

private fun GwtrTicketFilter.toLog() = TicketFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != GwtrUserId.NONE }?.asString(),
    claimStatus = ClaimStatus.takeIf { it != GwtrClaimStatus.NONE }?.name,
)

private fun GwtrError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun GwtrTicket.toLog() = TicketLog(
    id = id.takeIf { it != GwtrTicketId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ticketType = ticketType.takeIf { it != GwtrClaimStatus.NONE }?.name,
    visibility = visibility.takeIf { it != GwtrVisibility.NONE }?.name,
    ownerId = ownerId.takeIf { it != GwtrUserId.NONE }?.asString(),
    siteId = siteId.takeIf { it != GwtrSiteId.NONE }?.asString(),
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)
