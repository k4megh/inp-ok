package validation

import ru.otus.otuskotlin.marketplace.biz.GwtrTicketProcessor
import ru.otus.otuskotlin.marketplace.biz.validation.runBizTest
import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.stubs.GwtrTicketStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationLockCorrect(command: GwtrCommand, processor: GwtrTicketProcessor) = runBizTest {
    val ctx = GwtrContext(
        command = command,
        state = GwtrState.NONE,
        workMode = GwtrWorkMode.TEST,
        ticketRequest = GwtrTicketStub.get(),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(GwtrState.FAILING, ctx.state)
}

fun validationLockTrim(command: GwtrCommand, processor: GwtrTicketProcessor) = runBizTest {
    val ctx = GwtrContext(
        command = command,
        state = GwtrState.NONE,
        workMode = GwtrWorkMode.TEST,
        ticketRequest = GwtrTicketStub.prepareResult {
            lock = GwtrTicketLock(" \n\t 123-234-abc-ABC \n\t ")
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(GwtrState.FAILING, ctx.state)
}

fun validationLockEmpty(command: GwtrCommand, processor: GwtrTicketProcessor) = runBizTest {
    val ctx = GwtrContext(
        command = command,
        state = GwtrState.NONE,
        workMode = GwtrWorkMode.TEST,
        ticketRequest = GwtrTicketStub.prepareResult {
            lock = GwtrTicketLock("")
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(GwtrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationLockFormat(command: GwtrCommand, processor: GwtrTicketProcessor) = runBizTest {
    val ctx = GwtrContext(
        command = command,
        state = GwtrState.NONE,
        workMode = GwtrWorkMode.TEST,
        ticketRequest = GwtrTicketStub.prepareResult {
            lock = GwtrTicketLock("!@#\$%^&*(),.{}")
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(GwtrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}
