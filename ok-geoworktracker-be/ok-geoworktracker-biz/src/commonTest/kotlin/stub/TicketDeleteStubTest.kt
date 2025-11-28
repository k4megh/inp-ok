package ru.otus.otuskotlin.marketplace.biz.stub

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.biz.GwtrTicketProcessor
import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.stubs.GwtrStubs
import ru.otus.otuskotlin.marketplace.stubs.GwtrTicketStub
import kotlin.test.Test
import kotlin.test.assertEquals

class TicketDeleteStubTest {

    private val processor = GwtrTicketProcessor()
    val id = GwtrTicketId("666")

    @Test
    fun delete() = runTest {

        val ctx = GwtrContext(
            command = GwtrCommand.DELETE,
            state = GwtrState.NONE,
            workMode = GwtrWorkMode.STUB,
            stubCase = GwtrStubs.SUCCESS,
            ticketRequest = GwtrTicket(
                id = id,
            ),
        )
        processor.exec(ctx)

        val stub = GwtrTicketStub.get()
        assertEquals(stub.id, ctx.ticketResponse.id)
        assertEquals(stub.title, ctx.ticketResponse.title)
        assertEquals(stub.description, ctx.ticketResponse.description)
        assertEquals(stub.ticketType, ctx.ticketResponse.ticketType)
        assertEquals(stub.visibility, ctx.ticketResponse.visibility)
    }

    @Test
    fun badId() = runTest {
        val ctx = GwtrContext(
            command = GwtrCommand.DELETE,
            state = GwtrState.NONE,
            workMode = GwtrWorkMode.STUB,
            stubCase = GwtrStubs.BAD_ID,
            ticketRequest = GwtrTicket(),
        )
        processor.exec(ctx)
        assertEquals(GwtrTicket(), ctx.ticketResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = GwtrContext(
            command = GwtrCommand.DELETE,
            state = GwtrState.NONE,
            workMode = GwtrWorkMode.STUB,
            stubCase = GwtrStubs.DB_ERROR,
            ticketRequest = GwtrTicket(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(GwtrTicket(), ctx.ticketResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = GwtrContext(
            command = GwtrCommand.DELETE,
            state = GwtrState.NONE,
            workMode = GwtrWorkMode.STUB,
            stubCase = GwtrStubs.BAD_TITLE,
            ticketRequest = GwtrTicket(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(GwtrTicket(), ctx.ticketResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
