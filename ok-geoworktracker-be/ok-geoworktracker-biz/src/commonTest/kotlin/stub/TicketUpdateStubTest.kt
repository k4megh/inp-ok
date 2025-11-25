package ru.otus.otuskotlin.marketplace.biz.stub

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.biz.GwtrTicketProcessor
import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.stubs.GwtrStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class TicketUpdateStubTest {

    private val processor = GwtrTicketProcessor()
    val id = GwtrTicketId("777")
    val title = "title 666"
    val description = "desc 666"
    val claimStatus = GwtrClaimStatus.WAIT
    val visibility = GwtrVisibility.VISIBLE_PUBLIC

    @Test
    fun create() = runTest {

        val ctx = GwtrContext(
            command = GwtrCommand.UPDATE,
            state = GwtrState.NONE,
            workMode = GwtrWorkMode.STUB,
            stubCase = GwtrStubs.SUCCESS,
            ticketRequest = GwtrTicket(
                id = id,
                title = title,
                description = description,
                ticketType = claimStatus,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(id, ctx.ticketResponse.id)
        assertEquals(title, ctx.ticketResponse.title)
        assertEquals(description, ctx.ticketResponse.description)
        assertEquals(claimStatus, ctx.ticketResponse.ticketType)
        assertEquals(visibility, ctx.ticketResponse.visibility)
    }

    @Test
    fun badId() = runTest {
        val ctx = GwtrContext(
            command = GwtrCommand.UPDATE,
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
    fun badTitle() = runTest {
        val ctx = GwtrContext(
            command = GwtrCommand.UPDATE,
            state = GwtrState.NONE,
            workMode = GwtrWorkMode.STUB,
            stubCase = GwtrStubs.BAD_TITLE,
            ticketRequest = GwtrTicket(
                id = id,
                title = "",
                description = description,
                ticketType = claimStatus,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(GwtrTicket(), ctx.ticketResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
    @Test
    fun badDescription() = runTest {
        val ctx = GwtrContext(
            command = GwtrCommand.UPDATE,
            state = GwtrState.NONE,
            workMode = GwtrWorkMode.STUB,
            stubCase = GwtrStubs.BAD_DESCRIPTION,
            ticketRequest = GwtrTicket(
                id = id,
                title = title,
                description = "",
                ticketType = claimStatus,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(GwtrTicket(), ctx.ticketResponse)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = GwtrContext(
            command = GwtrCommand.UPDATE,
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
            command = GwtrCommand.UPDATE,
            state = GwtrState.NONE,
            workMode = GwtrWorkMode.STUB,
            stubCase = GwtrStubs.BAD_SEARCH_STRING,
            ticketRequest = GwtrTicket(
                id = id,
                title = title,
                description = description,
                ticketType = claimStatus,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(GwtrTicket(), ctx.ticketResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
