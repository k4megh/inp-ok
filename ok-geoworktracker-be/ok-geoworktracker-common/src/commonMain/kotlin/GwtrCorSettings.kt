package ru.otus.otuskotlin.marketplace.common

//import ru.otus.otuskotlin.marketplace.common.repo.IRepoTicket
import ru.otus.otuskotlin.marketplace.common.ws.IGwtrWsSessionRepo
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider

data class GwtrCorSettings(
    val loggerProvider: MpLoggerProvider = MpLoggerProvider(),
    val wsSessions: IGwtrWsSessionRepo = IGwtrWsSessionRepo.NONE,
//    val repoStub: IRepoTicket = IRepoTicket.NONE,
//    val repoTest: IRepoTicket = IRepoTicket.NONE,
//    val repoProd: IRepoTicket = IRepoTicket.NONE,
) {
    companion object {
        val NONE = GwtrCorSettings()
    }
}
