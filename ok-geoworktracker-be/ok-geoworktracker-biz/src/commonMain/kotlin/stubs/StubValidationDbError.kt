package ru.otus.otuskotlin.marketplace.biz.stubs

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.helpers.fail
import ru.otus.otuskotlin.marketplace.common.models.GwtrError
import ru.otus.otuskotlin.marketplace.common.models.GwtrState
import ru.otus.otuskotlin.marketplace.common.stubs.GwtrStubs

fun ICorChainDsl<GwtrContext>.stubDbError(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки базы данных
    """.trimIndent()
    on { stubCase == GwtrStubs.DB_ERROR && state == GwtrState.RUNNING }
    handle {
        fail(
            GwtrError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}
