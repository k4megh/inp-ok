package ru.otus.otuskotlin.marketplace.api.v2.mappers

import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.stubs.GwtrStubs
import ru.otus.otuskotlin.marketplace.stubs.GwtrTicketStub
import kotlin.test.assertEquals
import kotlin.test.Test

class MapperUpdateTest {
    @Test
    fun fromTransport() {
        val req = TicketUpdateRequest(
            debug = TicketDebug(
                mode = TicketRequestDebugMode.STUB,
                stub = TicketRequestDebugStubs.SUCCESS,
            ),
            ticket = GwtrTicketStub.get().toTransportUpdateTicket(),
        )
        val expected = GwtrTicketStub.prepareResult {
            ownerId = GwtrUserId.NONE
            permissionsClient.clear()
        }

        val context = GwtrContext()
        context.fromTransport(req)

        assertEquals(GwtrStubs.SUCCESS, context.stubCase)
        assertEquals(GwtrWorkMode.STUB, context.workMode)
        assertEquals(expected, context.ticketRequest)
    }

    @Test
    fun toTransport() {
        val context = GwtrContext(
            requestId = GwtrRequestId("1234"),
            command = GwtrCommand.UPDATE,
            ticketResponse = GwtrTicketStub.get(),
            errors = mutableListOf(
                GwtrError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = GwtrState.RUNNING,
        )

        val req = context.toTransportTicket() as TicketUpdateResponse

        assertEquals(GwtrTicketStub.get().toTransportTicket(), req.ticket)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}
