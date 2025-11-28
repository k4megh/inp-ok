package ru.otus.otuskotlin.marketplace.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.biz.GwtrTicketProcessor
import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.stubs.GwtrTicketStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationTitleCorrect(command: GwtrCommand, processor: GwtrTicketProcessor) = runTest {
    val ctx = GwtrContext(
        command = command,
        state = GwtrState.NONE,
        workMode = GwtrWorkMode.TEST,
        ticketRequest = GwtrTicketStub.prepareResult {
            title = "abc"
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(GwtrState.FAILING, ctx.state)
    assertEquals("abc", ctx.ticketValidated.title)
}

fun validationTitleTrim(command: GwtrCommand, processor: GwtrTicketProcessor) = runTest {
    val ctx = GwtrContext(
        command = command,
        state = GwtrState.NONE,
        workMode = GwtrWorkMode.TEST,
        ticketRequest = GwtrTicketStub.prepareResult {
            title = " \n\t abc \t\n "
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(GwtrState.FAILING, ctx.state)
    assertEquals("abc", ctx.ticketValidated.title)
}

fun validationTitleEmpty(command: GwtrCommand, processor: GwtrTicketProcessor) = runTest {
    val ctx = GwtrContext(
        command = command,
        state = GwtrState.NONE,
        workMode = GwtrWorkMode.TEST,
        ticketRequest = GwtrTicketStub.prepareResult {
            title = ""
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(GwtrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}

fun validationTitleSymbols(command: GwtrCommand, processor: GwtrTicketProcessor) = runTest {
    val ctx = GwtrContext(
        command = command,
        state = GwtrState.NONE,
        workMode = GwtrWorkMode.TEST,
        ticketRequest = GwtrTicketStub.prepareResult {
            title = "!@#$%^&*(),.{}"
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(GwtrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}
