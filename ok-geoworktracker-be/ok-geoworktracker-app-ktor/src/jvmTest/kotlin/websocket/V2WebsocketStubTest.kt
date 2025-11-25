package ru.otus.otuskotlin.marketplace.app.ktor.websocket

import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.*
import io.ktor.server.testing.*
import kotlinx.coroutines.withTimeout
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.app.ktor.GwtrAppSettings
import ru.otus.otuskotlin.marketplace.app.ktor.module
import ru.otus.otuskotlin.marketplace.common.GwtrCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class V2WebsocketStubTest {

    @Test
    fun createStub() {
        val request = TicketCreateRequest(
            ticket = TicketCreateObject(
                title = "Болт",
                description = "КРУТЕЙШИЙ",
                ticketType = ClaimStatus.WAIT,
                visibility = TicketVisibility.PUBLIC,
            ),
            debug = TicketDebug(
                mode = TicketRequestDebugMode.STUB,
                stub = TicketRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun readStub() {
        val request = TicketReadRequest(
            ticket = TicketReadObject("666"),
            debug = TicketDebug(
                mode = TicketRequestDebugMode.STUB,
                stub = TicketRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun updateStub() {
        val request = TicketUpdateRequest(
            ticket = TicketUpdateObject(
                id = "666",
                title = "Болт",
                description = "КРУТЕЙШИЙ",
                ticketType = ClaimStatus.WAIT,
                visibility = TicketVisibility.PUBLIC,
            ),
            debug = TicketDebug(
                mode = TicketRequestDebugMode.STUB,
                stub = TicketRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun deleteStub() {
        val request = TicketDeleteRequest(
            ticket = TicketDeleteObject(
                id = "666",
            ),
            debug = TicketDebug(
                mode = TicketRequestDebugMode.STUB,
                stub = TicketRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun searchStub() {
        val request = TicketSearchRequest(
            ticketFilter = TicketSearchFilter(),
            debug = TicketDebug(
                mode = TicketRequestDebugMode.STUB,
                stub = TicketRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }


    private inline fun <reified T> testMethod(
        request: IRequest,
        crossinline assertBlock: (T) -> Unit
    ) = testApplication {
        application { module(GwtrAppSettings(corSettings = GwtrCorSettings())) }
        val client = createClient {
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(apiV2Mapper)
            }
        }

        client.webSocket("/v2/ws") {
            withTimeout(3000) {
                val response = receiveDeserialized<IResponse>() as T
                assertIs<TicketInitResponse>(response)
            }
            sendSerialized(request)
            withTimeout(3000) {
                val response = receiveDeserialized<IResponse>() as T
                assertBlock(response)
            }
        }
    }
}
