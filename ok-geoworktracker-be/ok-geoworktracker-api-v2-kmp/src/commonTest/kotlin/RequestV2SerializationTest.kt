package ru.otus.otuskotlin.marketplace.api.v2

import kotlinx.serialization.encodeToString
import ru.otus.otuskotlin.marketplace.api.v2.models.TicketCreateObject
import ru.otus.otuskotlin.marketplace.api.v2.models.TicketCreateRequest
import ru.otus.otuskotlin.marketplace.api.v2.models.TicketDebug
import ru.otus.otuskotlin.marketplace.api.v2.models.TicketRequestDebugMode
import ru.otus.otuskotlin.marketplace.api.v2.models.TicketRequestDebugStubs
import ru.otus.otuskotlin.marketplace.api.v2.models.TicketVisibility
import ru.otus.otuskotlin.marketplace.api.v2.models.ClaimStatus
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV2SerializationTest {
    private val request: IRequest = TicketCreateRequest(
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
        val json = apiV2Mapper.encodeToString(IRequest.serializer(), request)

        println(json)

        assertContains(json, Regex("\"title\":\\s*\"ticket title\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(request)
        val obj = apiV2Mapper.decodeFromString<IRequest>(json) as TicketCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"ticket": null}
        """.trimIndent()
        val obj = apiV2Mapper.decodeFromString<TicketCreateRequest>(jsonString)

        assertEquals(null, obj.ticket)
    }
}
