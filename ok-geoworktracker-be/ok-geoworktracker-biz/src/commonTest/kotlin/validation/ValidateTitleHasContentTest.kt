package ru.otus.otuskotlin.marketplace.biz.validation

import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.models.GwtrTicket
import ru.otus.otuskotlin.marketplace.common.models.GwtrTicketFilter
import ru.otus.otuskotlin.marketplace.common.models.GwtrState
import ru.otus.otuskotlin.marketplace.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateTitleHasContentTest {
    @Test
    fun emptyString() = runBizTest {
        val ctx = GwtrContext(state = GwtrState.RUNNING, ticketValidating = GwtrTicket(title = ""))
        chain.exec(ctx)
        assertEquals(GwtrState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun noContent() = runBizTest {
        val ctx = GwtrContext(state = GwtrState.RUNNING, ticketValidating = GwtrTicket(title = "12!@#$%^&*()_+-="))
        chain.exec(ctx)
        assertEquals(GwtrState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-title-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runBizTest {
        val ctx = GwtrContext(state = GwtrState.RUNNING, ticketFilterValidating = GwtrTicketFilter(searchString = "Ж"))
        chain.exec(ctx)
        assertEquals(GwtrState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateTitleHasContent("")
        }.build()
    }
}
