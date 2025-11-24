package ru.otus.otuskotlin.marketplace.app.common

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.marketplace.api.log1.mapper.toLog
import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.helpers.asGwtrError
import ru.otus.otuskotlin.marketplace.common.models.GwtrCommand
import ru.otus.otuskotlin.marketplace.common.models.GwtrState
import kotlin.reflect.KClass

suspend inline fun <T> IGwtrAppSettings.controllerHelper(
    crossinline getRequest: suspend GwtrContext.() -> Unit,
    crossinline toResponse: suspend GwtrContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
     val ctx = GwtrContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId),
            e = e,
        )
        ctx.state = GwtrState.FAILING
        ctx.errors.add(e.asGwtrError())
        processor.exec(ctx)
        if (ctx.command == GwtrCommand.NONE) {
            ctx.command = GwtrCommand.READ
        }
        ctx.toResponse()
    }
}
