package ru.otus.otuskotlin.marketplace.biz.general

import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.models.GwtrCommand
import ru.otus.otuskotlin.marketplace.common.models.GwtrState
import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.chain

fun ICorChainDsl<GwtrContext>.operation(
    title: String,
    command: GwtrCommand,
    block: ICorChainDsl<GwtrContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == GwtrState.RUNNING }
}
