package ru.otus.otuskotlin.marketplace.mappers.v1

import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.models.GwtrWorkMode
import ru.otus.otuskotlin.marketplace.common.stubs.GwtrStubs
import ru.otus.otuskotlin.marketplace.mappers.v1.exceptions.UnknownRequestClass

fun GwtrContext.fromTransport(request: IRequest) = when (request) {
    is TicketCreateRequest -> fromTransport(request)
    is TicketReadRequest -> fromTransport(request)
    is TicketUpdateRequest -> fromTransport(request)
    is TicketDeleteRequest -> fromTransport(request)
    is TicketSearchRequest -> fromTransport(request)
     else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toTicketId() = this?.let { GwtrTicketId(it) } ?: GwtrTicketId.NONE
private fun String?.toTicketWithId() = GwtrTicket(id = this.toTicketId())
private fun String?.toTicketLock() = this?.let { GwtrTicketLock(it) } ?: GwtrTicketLock.NONE

private fun TicketDebug?.transportToWorkMode(): GwtrWorkMode = when (this?.mode) {
    TicketRequestDebugMode.PROD -> GwtrWorkMode.PROD
    TicketRequestDebugMode.TEST -> GwtrWorkMode.TEST
    TicketRequestDebugMode.STUB -> GwtrWorkMode.STUB
    null -> GwtrWorkMode.PROD
}

private fun TicketDebug?.transportToStubCase(): GwtrStubs = when (this?.stub) {
    TicketRequestDebugStubs.SUCCESS -> GwtrStubs.SUCCESS
    TicketRequestDebugStubs.NOT_FOUND -> GwtrStubs.NOT_FOUND
    TicketRequestDebugStubs.BAD_ID -> GwtrStubs.BAD_ID
    TicketRequestDebugStubs.BAD_TITLE -> GwtrStubs.BAD_TITLE
    TicketRequestDebugStubs.BAD_DESCRIPTION -> GwtrStubs.BAD_DESCRIPTION
    TicketRequestDebugStubs.BAD_VISIBILITY -> GwtrStubs.BAD_VISIBILITY
    TicketRequestDebugStubs.CANNOT_DELETE -> GwtrStubs.CANNOT_DELETE
    TicketRequestDebugStubs.BAD_SEARCH_STRING -> GwtrStubs.BAD_SEARCH_STRING
    null -> GwtrStubs.NONE
}

fun GwtrContext.fromTransport(request: TicketCreateRequest) {
    command = GwtrCommand.CREATE
    ticketRequest = request.ticket?.toInternal() ?: GwtrTicket()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun GwtrContext.fromTransport(request: TicketReadRequest) {
    command = GwtrCommand.READ
    ticketRequest = request.ticket.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun TicketReadObject?.toInternal(): GwtrTicket = if (this != null) {
    GwtrTicket(id = id.toTicketId())
} else {
    GwtrTicket()
}


fun GwtrContext.fromTransport(request: TicketUpdateRequest) {
    command = GwtrCommand.UPDATE
    ticketRequest = request.ticket?.toInternal() ?: GwtrTicket()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun GwtrContext.fromTransport(request: TicketDeleteRequest) {
    command = GwtrCommand.DELETE
    ticketRequest = request.ticket.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun TicketDeleteObject?.toInternal(): GwtrTicket = if (this != null) {
    GwtrTicket(
        id = id.toTicketId(),
        lock = lock.toTicketLock(),
    )
} else {
    GwtrTicket()
}

fun GwtrContext.fromTransport(request: TicketSearchRequest) {
    command = GwtrCommand.SEARCH
    ticketFilterRequest = request.ticketFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}


private fun TicketSearchFilter?.toInternal(): GwtrTicketFilter = GwtrTicketFilter(
    searchString = this?.searchString ?: ""
)

private fun TicketCreateObject.toInternal(): GwtrTicket = GwtrTicket(
    title = this.title ?: "",
    description = this.description ?: "",
    ticketType = this.ticketType.fromTransport(),
    visibility = this.visibility.fromTransport(),
)

private fun TicketUpdateObject.toInternal(): GwtrTicket = GwtrTicket(
    id = this.id.toTicketId(),
    title = this.title ?: "",
    description = this.description ?: "",
    ticketType = this.ticketType.fromTransport(),
    visibility = this.visibility.fromTransport(),
    lock = lock.toTicketLock(),
)

private fun TicketVisibility?.fromTransport(): GwtrVisibility = when (this) {
    TicketVisibility.PUBLIC -> GwtrVisibility.VISIBLE_PUBLIC
    TicketVisibility.OWNER_ONLY -> GwtrVisibility.VISIBLE_TO_OWNER
    TicketVisibility.REGISTERED_ONLY -> GwtrVisibility.VISIBLE_TO_GROUP
    null -> GwtrVisibility.NONE
}

private fun ClaimStatus?.fromTransport(): GwtrClaimStatus = when (this) {
    ClaimStatus.WAIT -> GwtrClaimStatus.WAIT
    ClaimStatus.INWORK -> GwtrClaimStatus.INWORK
    ClaimStatus.EXPIRED -> GwtrClaimStatus.EXPIRED
    ClaimStatus.DONE -> GwtrClaimStatus.DONE
    ClaimStatus.RETURNED -> GwtrClaimStatus.RETURNED
    null -> GwtrClaimStatus.NONE
}

