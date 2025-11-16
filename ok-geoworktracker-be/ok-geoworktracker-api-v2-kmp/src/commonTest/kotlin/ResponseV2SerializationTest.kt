package ru.otus.otuskotlin.marketplace.api.v2

import kotlinx.serialization.encodeToString
import ru.otus.otuskotlin.marketplace.api.v2.models.TicketCreateResponse
import ru.otus.otuskotlin.marketplace.api.v2.models.TicketResponseObject
import ru.otus.otuskotlin.marketplace.api.v2.models.TicketVisibility
import ru.otus.otuskotlin.marketplace.api.v2.models.ClaimStatus
import ru.otus.otuskotlin.marketplace.api.v2.models.IResponse
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV2SerializationTest {
    private val response: IResponse = TicketCreateResponse(
        ticket = TicketResponseObject(
            title = "ticket title",
            description = "ticket description",
            ticketType = ClaimStatus.WAIT,
            visibility = TicketVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
//        val json = apiV2Mapper.encodeToString(TicketRequestSerializer1, request)
//        val json = apiV2Mapper.encodeToString(RequestSerializers.create, request)
        val json = apiV2Mapper.encodeToString(response)

        println(json)

        assertContains(json, Regex("\"title\":\\s*\"ticket title\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(response)
        val obj = apiV2Mapper.decodeFromString<IResponse>(json) as TicketCreateResponse

        assertEquals(response, obj)
    }
}
