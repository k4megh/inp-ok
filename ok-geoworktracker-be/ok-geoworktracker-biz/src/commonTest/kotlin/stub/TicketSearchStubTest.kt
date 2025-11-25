package ru.otus.otuskotlin.marketplace.biz.stub

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.biz.GwtrTicketProcessor
import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.stubs.GwtrStubs
import ru.otus.otuskotlin.marketplace.stubs.GwtrTicketStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class TicketSearchStubTest {

    private val processor = GwtrTicketProcessor()
    val filter = GwtrTicketFilter(searchString = "bolt")

    @Test
    fun read() = runTest {

        val ctx = GwtrContext(
            command = GwtrCommand.SEARCH,
            state = GwtrState.NONE,
            workMode = GwtrWorkMode.STUB,
            stubCase = GwtrStubs.SUCCESS,
            ticketFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.ticketsResponse.size > 1)
        val first = ctx.ticketsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.title.contains(filter.searchString))
        assertTrue(first.description.contains(filter.searchString))
        with (GwtrTicketStub.get()) {
            assertEquals(ticketType, first.ticketType)
            assertEquals(visibility, first.visibility)
        }
    }

    @Test
    fun bticketId() = runTest {
        val ctx = GwtrContext(
            command = GwtrCommand.SEARCH,
            state = GwtrState.NONE,
            workMode = GwtrWorkMode.STUB,
            stubCase = GwtrStubs.BAD_ID,
            ticketFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(GwtrTicket(), ctx.ticketResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = GwtrContext(
            command = GwtrCommand.SEARCH,
            state = GwtrState.NONE,
            workMode = GwtrWorkMode.STUB,
            stubCase = GwtrStubs.DB_ERROR,
            ticketFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(GwtrTicket(), ctx.ticketResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = GwtrContext(
            command = GwtrCommand.SEARCH,
            state = GwtrState.NONE,
            workMode = GwtrWorkMode.STUB,
            stubCase = GwtrStubs.BAD_TITLE,
            ticketFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(GwtrTicket(), ctx.ticketResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
