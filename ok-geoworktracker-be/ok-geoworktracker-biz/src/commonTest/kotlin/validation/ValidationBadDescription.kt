package ru.otus.otuskotlin.marketplace.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.biz.GwtrTicketProcessor
import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.stubs.GwtrTicketStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationDescriptionCorrect(command: GwtrCommand, processor: GwtrTicketProcessor) = runTest {
    val ctx = GwtrContext(
        command = command,
        state = GwtrState.NONE,
        workMode = GwtrWorkMode.TEST,
        ticketRequest = GwtrTicketStub.get(),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(GwtrState.FAILING, ctx.state)
    assertContains(ctx.ticketValidated.description, "болт")
}

fun validationDescriptionTrim(command: GwtrCommand, processor: GwtrTicketProcessor) = runTest {
    val ctx = GwtrContext(
        command = command,
        state = GwtrState.NONE,
        workMode = GwtrWorkMode.TEST,
        ticketRequest = GwtrTicketStub.prepareResult {
            description = " \n\tabc \n\t"
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(GwtrState.FAILING, ctx.state)
    assertEquals("abc", ctx.ticketValidated.description)
}

fun validationDescriptionEmpty(command: GwtrCommand, processor: GwtrTicketProcessor) = runTest {
    val ctx = GwtrContext(
        command = command,
        state = GwtrState.NONE,
        workMode = GwtrWorkMode.TEST,
        ticketRequest = GwtrTicketStub.prepareResult {
            description = ""
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(GwtrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}

fun validationDescriptionSymbols(command: GwtrCommand, processor: GwtrTicketProcessor) = runTest {
    val ctx = GwtrContext(
        command = command,
        state = GwtrState.NONE,
        workMode = GwtrWorkMode.TEST,
        ticketRequest = GwtrTicketStub.prepareResult {
            description = "!@#$%^&*(),.{}"
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(GwtrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}
