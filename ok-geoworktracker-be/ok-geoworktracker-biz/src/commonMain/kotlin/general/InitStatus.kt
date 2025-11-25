package ru.otus.otuskotlin.marketplace.biz.general

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.models.GwtrState

fun ICorChainDsl<GwtrContext>.initStatus(title: String) = worker() {
    this.title = title
    this.description = """
        Этот обработчик устанавливает стартовый статус обработки. Запускается только в случае не заданного статуса.
    """.trimIndent()
    on { state == GwtrState.NONE }
    handle { state = GwtrState.RUNNING }
}
