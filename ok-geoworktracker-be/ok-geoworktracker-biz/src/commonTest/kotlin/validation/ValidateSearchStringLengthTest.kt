package ru.otus.otuskotlin.marketplace.biz.validation

import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.models.GwtrTicketFilter
import ru.otus.otuskotlin.marketplace.common.models.GwtrState
import ru.otus.otuskotlin.marketplace.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateSearchStringLengthTest {
    @Test
    fun emptyString() = runBizTest {
        val ctx = GwtrContext(state = GwtrState.RUNNING, ticketFilterValidating = GwtrTicketFilter(searchString = ""))
        chain.exec(ctx)
        assertEquals(GwtrState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun blankString() = runBizTest {
        val ctx = GwtrContext(state = GwtrState.RUNNING, ticketFilterValidating = GwtrTicketFilter(searchString = "  "))
        chain.exec(ctx)
        assertEquals(GwtrState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun shortString() = runBizTest {
        val ctx = GwtrContext(state = GwtrState.RUNNING, ticketFilterValidating = GwtrTicketFilter(searchString = "12"))
        chain.exec(ctx)
        assertEquals(GwtrState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runBizTest {
        val ctx = GwtrContext(state = GwtrState.RUNNING, ticketFilterValidating = GwtrTicketFilter(searchString = "123"))
        chain.exec(ctx)
        assertEquals(GwtrState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runBizTest {
        val ctx = GwtrContext(state = GwtrState.RUNNING, ticketFilterValidating = GwtrTicketFilter(searchString = "12".repeat(51)))
        chain.exec(ctx)
        assertEquals(GwtrState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooLong", ctx.errors.first().code)
    }

    companion object {
        val chain = rootChain {
            validateSearchStringLength("")
        }.build()
    }
}
