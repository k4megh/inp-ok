package ru.otus.otuskotlin.marketplace.common.ws

interface IGwtrWsSession {
    suspend fun <T> send(obj: T)
    companion object {
        val NONE = object : IGwtrWsSession {
            override suspend fun <T> send(obj: T) {

            }
        }
    }
}
