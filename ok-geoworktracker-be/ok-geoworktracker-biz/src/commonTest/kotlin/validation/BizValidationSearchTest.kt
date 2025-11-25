package ru.otus.otuskotlin.marketplace.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.models.GwtrTicketFilter
import ru.otus.otuskotlin.marketplace.common.models.GwtrCommand
import ru.otus.otuskotlin.marketplace.common.models.GwtrState
import ru.otus.otuskotlin.marketplace.common.models.GwtrWorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizValidationSearchTest: BaseBizValidationTest() {
    override val command = GwtrCommand.SEARCH

    @Test
    fun correctEmpty() = runTest {
        val ctx = GwtrContext(
            command = command,
            state = GwtrState.NONE,
            workMode = GwtrWorkMode.TEST,
            ticketFilterRequest = GwtrTicketFilter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(GwtrState.FAILING, ctx.state)
    }
}
