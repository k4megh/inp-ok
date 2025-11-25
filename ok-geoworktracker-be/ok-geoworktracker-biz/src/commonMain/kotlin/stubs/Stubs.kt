package ru.otus.otuskotlin.marketplace.biz.stubs

import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.models.GwtrState
import ru.otus.otuskotlin.marketplace.common.models.GwtrWorkMode
import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.chain

fun ICorChainDsl<GwtrContext>.stubs(title: String, block: ICorChainDsl<GwtrContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == GwtrWorkMode.STUB && state == GwtrState.RUNNING }
}
