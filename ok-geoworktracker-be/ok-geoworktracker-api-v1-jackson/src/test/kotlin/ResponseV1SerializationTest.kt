package ru.otus.otuskotlin.marketplace.api.v1

import ru.otus.otuskotlin.marketplace.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {
    private val response = TicketCreateResponse(
        ticket = TicketResponseObject(
            title = "ticket title",
            description = "ticket description",
            ticketType = ClaimStatus.WAIT,
            visibility = TicketVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"title\":\\s*\"ticket title\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as TicketCreateResponse

        assertEquals(response, obj)
    }
}
