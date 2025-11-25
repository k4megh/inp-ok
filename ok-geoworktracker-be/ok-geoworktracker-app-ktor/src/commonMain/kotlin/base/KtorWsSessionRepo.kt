package ru.otus.otuskotlin.marketplace.app.ktor.base

import ru.otus.otuskotlin.marketplace.common.ws.IGwtrWsSession
import ru.otus.otuskotlin.marketplace.common.ws.IGwtrWsSessionRepo

class KtorWsSessionRepo: IGwtrWsSessionRepo {
    private val sessions: MutableSet<IGwtrWsSession> = mutableSetOf()
    override fun add(session: IGwtrWsSession) {
        sessions.add(session)
    }

    override fun clearAll() {
        sessions.clear()
    }

    override fun remove(session: IGwtrWsSession) {
        sessions.remove(session)
    }

    override suspend fun <T> sendAll(obj: T) {
        sessions.forEach { it.send(obj) }
    }
}
