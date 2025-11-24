package ru.otus.otuskotlin.marketplace.common.ws

interface IGwtrWsSessionRepo {
    fun add(session: IGwtrWsSession)
    fun clearAll()
    fun remove(session: IGwtrWsSession)
    suspend fun <K> sendAll(obj: K)

    companion object {
        val NONE = object : IGwtrWsSessionRepo {
            override fun add(session: IGwtrWsSession) {}
            override fun clearAll() {}
            override fun remove(session: IGwtrWsSession) {}
            override suspend fun <K> sendAll(obj: K) {}
        }
    }
}
