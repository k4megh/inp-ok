package ru.otus.otuskotlin.marketplace.stubs

import ru.otus.otuskotlin.marketplace.common.models.*

object GwtrTicketStubBolts {
    val TICKET_WAIT_BOLT1: GwtrTicket
        get() = GwtrTicket(
            id = GwtrTicketId("666"),
            title = "Требуется болт",
            description = "Требуется болт 100x5 с шестигранной шляпкой",
            ownerId = GwtrUserId("user-1"),
            ticketType = GwtrClaimStatus.WAIT,
            visibility = GwtrVisibility.VISIBLE_PUBLIC,
            lock = GwtrTicketLock("123"),
            permissionsClient = mutableSetOf(
                GwtrTicketPermissionClient.READ,
                GwtrTicketPermissionClient.UPDATE,
                GwtrTicketPermissionClient.DELETE,
                GwtrTicketPermissionClient.MAKE_VISIBLE_PUBLIC,
                GwtrTicketPermissionClient.MAKE_VISIBLE_GROUP,
                GwtrTicketPermissionClient.MAKE_VISIBLE_OWNER,
            )
        )
    val TICKET_INWORK_BOLT1 = TICKET_WAIT_BOLT1.copy(ticketType = GwtrClaimStatus.WAIT)
}
