package ru.otus.otuskotlin.marketplace.app.common

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.api.v2.mappers.fromTransport
import ru.otus.otuskotlin.marketplace.api.v2.mappers.toTransportTicket
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.biz.GwtrTicketProcessor
import ru.otus.otuskotlin.marketplace.common.GwtrCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerV2Test {

    private val request = TicketCreateRequest(
        ticket = TicketCreateObject(
            title = "some ticket",
            description = "some description of some ticket",
            ticketType = ClaimStatus.WAIT,
            visibility = TicketVisibility.PUBLIC,
            siteId = "some product id",
        ),
        debug = TicketDebug(mode = TicketRequestDebugMode.STUB, stub = TicketRequestDebugStubs.SUCCESS)
    )

    private val appSettings: IGwtrAppSettings = object : IGwtrAppSettings {
        override val corSettings: GwtrCorSettings = GwtrCorSettings()
        override val processor: GwtrTicketProcessor = GwtrTicketProcessor(corSettings)
    }


    class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.createTicketKtor(appSettings: IGwtrAppSettings) {
        val resp = appSettings.controllerHelper(
            { fromTransport(receive<TicketCreateRequest>()) },
            { toTransportTicket() },
            ControllerV2Test::class,
            "controller-v2-test"
        )
        respond(resp)
    }

    @Test
    fun ktorHelperTest() = runTest {
        val testApp = TestApplicationCall(request).apply { createTicketKtor(appSettings) }
        val res = testApp.res as TicketCreateResponse
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}
