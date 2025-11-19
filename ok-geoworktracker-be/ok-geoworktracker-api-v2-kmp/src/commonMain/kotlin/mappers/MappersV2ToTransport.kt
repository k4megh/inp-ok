package ru.otus.otuskotlin.marketplace.api.v2.mappers

import ru.otus.otuskotlin.marketplace.common.exceptions.UnknownGwtrCommand
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.models.*

fun GwtrContext.toTransportTicket(): IResponse = when (val cmd = command) {
    GwtrCommand.CREATE -> toTransportCreate()
    GwtrCommand.READ -> toTransportRead()
    GwtrCommand.UPDATE -> toTransportUpdate()
    GwtrCommand.DELETE -> toTransportDelete()
    GwtrCommand.SEARCH -> toTransportSearch()
    GwtrCommand.NONE -> throw UnknownGwtrCommand(cmd)
}

fun GwtrContext.toTransportCreate() = TicketCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    ticket = ticketResponse.toTransportTicket()
)

fun GwtrContext.toTransportRead() = TicketReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    ticket = ticketResponse.toTransportTicket()
)

fun GwtrContext.toTransportUpdate() = TicketUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    ticket = ticketResponse.toTransportTicket()
)

fun GwtrContext.toTransportDelete() = TicketDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    ticket = ticketResponse.toTransportTicket()
)

fun GwtrContext.toTransportSearch() = TicketSearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    tickets = ticketsResponse.toTransportTicket()
)


fun List<GwtrTicket>.toTransportTicket(): List<TicketResponseObject>? = this
    .map { it.toTransportTicket() }
    .toList()
    .takeIf { it.isNotEmpty() }

internal fun GwtrTicket.toTransportTicket(): TicketResponseObject = TicketResponseObject(
    id = id.toTransportTicket(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != GwtrUserId.NONE }?.asString(),
    ticketType = ticketType.toTransportTicket(),
    visibility = visibility.toTransportTicket(),
    permissions = permissionsClient.toTransportTicket(),
    siteId = siteId.takeIf { it != GwtrSiteId.NONE }?.asString(),
    lock = lock.takeIf { it != GwtrTicketLock.NONE }?.asString()
)

internal fun GwtrTicketId.toTransportTicket() = takeIf { it != GwtrTicketId.NONE }?.asString()

private fun Set<GwtrTicketPermissionClient>.toTransportTicket(): Set<TicketPermissions>? = this
    .map { it.toTransportTicket() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun GwtrTicketPermissionClient.toTransportTicket() = when (this) {
    GwtrTicketPermissionClient.READ -> TicketPermissions.READ
    GwtrTicketPermissionClient.UPDATE -> TicketPermissions.UPDATE
    GwtrTicketPermissionClient.MAKE_VISIBLE_OWNER -> TicketPermissions.MAKE_VISIBLE_OWN
    GwtrTicketPermissionClient.MAKE_VISIBLE_GROUP -> TicketPermissions.MAKE_VISIBLE_GROUP
    GwtrTicketPermissionClient.MAKE_VISIBLE_PUBLIC -> TicketPermissions.MAKE_VISIBLE_PUBLIC
    GwtrTicketPermissionClient.DELETE -> TicketPermissions.DELETE
}

internal fun GwtrVisibility.toTransportTicket(): TicketVisibility? = when (this) {
    GwtrVisibility.VISIBLE_PUBLIC -> TicketVisibility.PUBLIC
    GwtrVisibility.VISIBLE_TO_GROUP -> TicketVisibility.REGISTERED_ONLY
    GwtrVisibility.VISIBLE_TO_OWNER -> TicketVisibility.OWNER_ONLY
    GwtrVisibility.NONE -> null
}

internal fun GwtrClaimStatus.toTransportTicket(): ClaimStatus? = when (this) {
    GwtrClaimStatus.WAIT -> ClaimStatus.WAIT
    GwtrClaimStatus.INWORK -> ClaimStatus.INWORK
    GwtrClaimStatus.EXPIRED -> ClaimStatus.EXPIRED
    GwtrClaimStatus.DONE -> ClaimStatus.DONE
    GwtrClaimStatus.RETURNED -> ClaimStatus.RETURNED
     GwtrClaimStatus.NONE -> null
}

private fun List<GwtrError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportTicket() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun GwtrError.toTransportTicket() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun GwtrState.toResult(): ResponseResult? = when (this) {
    GwtrState.RUNNING, GwtrState.FINISHING -> ResponseResult.SUCCESS
    GwtrState.FAILING -> ResponseResult.ERROR
    GwtrState.NONE -> null
}
