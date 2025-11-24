package ru.otus.otuskotlin.marketplace.app.ktor.stub

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.app.ktor.GwtrAppSettings
import ru.otus.otuskotlin.marketplace.app.ktor.moduleJvm
import ru.otus.otuskotlin.marketplace.common.GwtrCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class V1TicketStubApiTest {
    @Test
    fun create() = v1TestApplication(
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
    fun read() = v1TestApplication(
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
    fun update() = v1TestApplication(
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
    fun delete() = v1TestApplication(
        func = "delete",
        request = TicketDeleteRequest(
            ticket = TicketDeleteObject(
                id = "666",
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
    fun search() = v1TestApplication(
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

    private fun v1TestApplication(
        func: String,
        request: IRequest,
        function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { moduleJvm(GwtrAppSettings(corSettings = GwtrCorSettings())) }
        val client = createClient {
            install(ContentNegotiation) {
                jackson {
                    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                    enable(SerializationFeature.INDENT_OUTPUT)
                    writerWithDefaultPrettyPrinter()
                }
            }
        }
        val response = client.post("/v1/ticket/$func") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        function(response)
    }
}
