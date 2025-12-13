package ru.otus.otuskotlin.marketplace.common

import ru.otus.otuskotlin.marketplace.common.repo.IRepoTicket
import ru.otus.otuskotlin.marketplace.common.ws.IGwtrWsSessionRepo
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider
import ru.otus.otuskotlin.marketplace.states.common.GwtrStatesCorSettings

data class GwtrCorSettings(
    val loggerProvider: MpLoggerProvider = MpLoggerProvider(),
    val wsSessions: IGwtrWsSessionRepo = IGwtrWsSessionRepo.NONE,
    val repoStub: IRepoTicket = IRepoTicket.NONE,
    val repoTest: IRepoTicket = IRepoTicket.NONE,
    val repoProd: IRepoTicket = IRepoTicket.NONE,
    val stateSettings: GwtrStatesCorSettings = GwtrStatesCorSettings(),
) {
    companion object {
        val NONE = GwtrCorSettings()
    }
}
