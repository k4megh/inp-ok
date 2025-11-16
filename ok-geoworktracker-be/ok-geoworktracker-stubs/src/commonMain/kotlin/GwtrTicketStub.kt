package ru.otus.otuskotlin.marketplace.stubs

import ru.otus.otuskotlin.marketplace.common.models.GwtrTicket
import ru.otus.otuskotlin.marketplace.common.models.GwtrTicketId
import ru.otus.otuskotlin.marketplace.common.models.GwtrClaimStatus
import ru.otus.otuskotlin.marketplace.stubs.GwtrTicketStubBolts.TICKET_WAIT_BOLT1
import ru.otus.otuskotlin.marketplace.stubs.GwtrTicketStubBolts.TICKET_INWORK_BOLT1

object GwtrTicketStub {
    fun get(): GwtrTicket = TICKET_WAIT_BOLT1.copy()

    fun prepareResult(block: GwtrTicket.() -> Unit): GwtrTicket = get().apply(block)

    fun prepareSearchList(filter: String, type: GwtrClaimStatus) = listOf(
        gwtrTicketWait("d-666-01", filter, type),
        gwtrTicketWait("d-666-02", filter, type),
        gwtrTicketWait("d-666-03", filter, type),
        gwtrTicketWait("d-666-04", filter, type),
        gwtrTicketWait("d-666-05", filter, type),
        gwtrTicketWait("d-666-06", filter, type),
    )


    private fun gwtrTicketWait(id: String, filter: String, type: GwtrClaimStatus) =
        gwtrTicket(TICKET_WAIT_BOLT1, id = id, filter = filter, type = type)

    private fun gwtrTicketInwork(id: String, filter: String, type: GwtrClaimStatus) =
        gwtrTicket(TICKET_INWORK_BOLT1, id = id, filter = filter, type = type)

    private fun gwtrTicket(base: GwtrTicket, id: String, filter: String, type: GwtrClaimStatus) = base.copy(
        id = GwtrTicketId(id),
        title = "$filter $id",
        description = "desc $filter $id",
        ticketType = type,
    )

}
