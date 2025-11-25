package ru.otus.otuskotlin.marketplace.biz.stubs

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.helpers.fail
import ru.otus.otuskotlin.marketplace.common.models.GwtrError
import ru.otus.otuskotlin.marketplace.common.models.GwtrState
import ru.otus.otuskotlin.marketplace.common.stubs.GwtrStubs

fun ICorChainDsl<GwtrContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для идентификатора объявления
    """.trimIndent()
    on { stubCase == GwtrStubs.BAD_ID && state == GwtrState.RUNNING }
    handle {
        fail(
            GwtrError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
    }
}
