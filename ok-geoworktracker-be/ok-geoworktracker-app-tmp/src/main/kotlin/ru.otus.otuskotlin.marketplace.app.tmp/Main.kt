package ru.otus.otuskotlin.marketplace.app.tmp

import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.marketplace.api.log1.mapper.toLog
import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.logging.common.IMpLogWrapper
import ru.otus.otuskotlin.marketplace.logging.common.LogLevel
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider
import ru.otus.otuskotlin.marketplace.logging.jvm.mpLoggerLogback
import kotlin.reflect.KClass


private val clazz: KClass<*> = ::main::class
suspend fun main() {
    val provider = MpLoggerProvider { mpLoggerLogback(it) }
    val logger: IMpLogWrapper = provider.logger(clazz.simpleName ?: "(unknown)")
    while (true) {
        val ctx = GwtrContext(
            command = GwtrCommand.CREATE,
            state = GwtrState.RUNNING,
            workMode = GwtrWorkMode.STUB,
            timeStart = Clock.System.now(),
            requestId = GwtrRequestId("tmp-request"),
            ticketRequest = GwtrTicket(
                title = "tmp title",
                description = "tmp desc",
                ticketType = GwtrClaimStatus.WAIT,
                visibility = GwtrVisibility.VISIBLE_PUBLIC,
            ),
            ticketResponse = GwtrTicket(
                title = "tmp title",
                description = "tmp desc",
                ticketType = GwtrClaimStatus.WAIT,
                visibility = GwtrVisibility.VISIBLE_PUBLIC,
                ownerId = GwtrUserId("tmp-user-id"),
                lock = GwtrTicketLock("tmp-lock"),
                permissionsClient = mutableSetOf(GwtrTicketPermissionClient.READ, GwtrTicketPermissionClient.UPDATE),
            ),
            errors = mutableListOf(
                GwtrError(
                    code = "tmp-error",
                    group = "tmp",
                    field = "none",
                    message = "tmp error message",
                    level = LogLevel.INFO,
                    exception = Exception("some exception"),
                ),
            )
        )
        logger.info(
            msg = "tmp log string",
            data = ctx.toLog("tmp-app-logg"),
        )
        delay(500)
    }
}