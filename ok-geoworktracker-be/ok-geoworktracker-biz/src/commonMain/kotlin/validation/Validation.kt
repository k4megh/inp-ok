package ru.otus.otuskotlin.marketplace.biz.validation

import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.models.GwtrState
import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.chain

fun ICorChainDsl<GwtrContext>.validation(block: ICorChainDsl<GwtrContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == GwtrState.RUNNING }
}
