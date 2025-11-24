package ru.otus.otuskotlin.marketplace.app.ktor.stub

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.app.ktor.GwtrAppSettings
import ru.otus.otuskotlin.marketplace.app.ktor.module
import ru.otus.otuskotlin.marketplace.common.GwtrCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class V2TicketStubApiTest {

    @Test
    fun create() = v2TestApplication(
        func = "create",
        request = TicketCreateRequest(
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
        ),
    ) { response ->
        val responseObj = response.body<TicketCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("666", responseObj.ticket?.id)
    }

    @Test
    fun read() = v2TestApplication(
        func = "read",
        request = TicketReadRequest(
            ticket = TicketReadObject("666"),
            debug = TicketDebug(
                mode = TicketRequestDebugMode.STUB,
                stub = TicketRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<TicketReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals("666", responseObj.ticket?.id)
    }

    @Test
    fun update() = v2TestApplication(
        func = "update",
        request = TicketUpdateRequest(
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
        ),
    ) { response ->
        val responseObj = response.body<TicketUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("666", responseObj.ticket?.id)
    }

    @Test
    fun delete() = v2TestApplication(
        func = "delete",
        request = TicketDeleteRequest(
            ticket = TicketDeleteObject(
                id = "666",
                lock = "123"
            ),
            debug = TicketDebug(
                mode = TicketRequestDebugMode.STUB,
                stub = TicketRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<TicketDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals("666", responseObj.ticket?.id)
    }

    @Test
    fun search() = v2TestApplication(
        func = "search",
        request = TicketSearchRequest(
            ticketFilter = TicketSearchFilter(),
            debug = TicketDebug(
                mode = TicketRequestDebugMode.STUB,
                stub = TicketRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<TicketSearchResponse>()
        assertEquals(200, response.status.value)
        assertEquals("d-666-01", responseObj.tickets?.first()?.id)
    }


    private inline fun <reified T: IRequest> v2TestApplication(
        func: String,
        request: T,
        crossinline function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { module(GwtrAppSettings(corSettings = GwtrCorSettings())) }
        val client = createClient {
            install(ContentNegotiation) {
                json(apiV2Mapper)
            }
        }
        val response = client.post("/v2/ticket/$func") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        function(response)
    }
}
