package ru.otus.otuskotlin.marketplace.biz.stub

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.biz.GwtrTicketProcessor
import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.stubs.GwtrStubs
import ru.otus.otuskotlin.marketplace.stubs.GwtrTicketStub
import kotlin.test.Test
import kotlin.test.assertEquals

class TicketReadStubTest {

    private val processor = GwtrTicketProcessor()
    val id = GwtrTicketId("666")

    @Test
    fun read() = runTest {

        val ctx = GwtrContext(
            command = GwtrCommand.READ,
            state = GwtrState.NONE,
            workMode = GwtrWorkMode.STUB,
            stubCase = GwtrStubs.SUCCESS,
            ticketRequest = GwtrTicket(
                id = id,
            ),
        )
        processor.exec(ctx)
        with (GwtrTicketStub.get()) {
            assertEquals(id, ctx.ticketResponse.id)
            assertEquals(title, ctx.ticketResponse.title)
            assertEquals(description, ctx.ticketResponse.description)
            assertEquals(ticketType, ctx.ticketResponse.ticketType)
            assertEquals(visibility, ctx.ticketResponse.visibility)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = GwtrContext(
            command = GwtrCommand.READ,
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
            command = GwtrCommand.READ,
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
    fun bticketNoCase() = runTest {
        val ctx = GwtrContext(
            command = GwtrCommand.READ,
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
