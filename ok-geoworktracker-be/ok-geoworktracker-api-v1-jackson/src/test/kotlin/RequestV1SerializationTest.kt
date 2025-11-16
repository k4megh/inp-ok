package ru.otus.otuskotlin.marketplace.api.v1

import ru.otus.otuskotlin.marketplace.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {
    private val request = TicketCreateRequest(
        debug = TicketDebug(
            mode = TicketRequestDebugMode.STUB,
            stub = TicketRequestDebugStubs.BAD_TITLE
        ),
        ticket = TicketCreateObject(
            title = "ticket title",
            description = "ticket description",
            ticketType = ClaimStatus.WAIT,
            visibility = TicketVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"title\":\\s*\"ticket title\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as TicketCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"ticket": null}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, TicketCreateRequest::class.java)

        assertEquals(null, obj.ticket)
    }
}
