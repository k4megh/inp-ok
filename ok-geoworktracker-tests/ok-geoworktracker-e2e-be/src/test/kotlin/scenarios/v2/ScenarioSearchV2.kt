package ru.otus.otuskotlin.marketplace.e2e.be.scenarios.v2

import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.e2e.be.base.client.Client
import ru.otus.otuskotlin.marketplace.e2e.be.scenarios.v2.base.sendAndReceive
import ru.otus.otuskotlin.marketplace.e2e.be.scenarios.v2.base.someCreateTicket
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioSearchV2(
    private val client: Client,
    private val debug: TicketDebug? = null
) {
    @Test
    fun search() = runBlocking {
        val objs = listOf(
            someCreateTicket,
            someCreateTicket.copy(title = "Selling Bolt"),
            someCreateTicket.copy(title = "Selling Nut"),
        ).map { obj ->
            val resCreate = client.sendAndReceive(
                "ticket/create", TicketCreateRequest(
                    debug = debug,
                    ticket = obj,
                )
            ) as TicketCreateResponse

            assertEquals(ResponseResult.SUCCESS, resCreate.result)

            val cObj: TicketResponseObject = resCreate.ticket ?: fail("No ticket in Create response")
            assertEquals(obj.title, cObj.title)
            assertEquals(obj.description, cObj.description)
            assertEquals(obj.visibility, cObj.visibility)
            assertEquals(obj.ticketType, cObj.ticketType)
            cObj
        }

        val sObj = TicketSearchFilter(searchString = "Selling")
        val resSearch = client.sendAndReceive(
            "ticket/search",
            TicketSearchRequest(
                debug = debug,
                ticketFilter = sObj,
            )
        ) as TicketSearchResponse

        assertEquals(ResponseResult.SUCCESS, resSearch.result)

        val rsObj: List<TicketResponseObject> = resSearch.tickets ?: fail("No tickets in Search response")
        val titles = rsObj.map { it.title }
        assertContains(titles, "Selling Bolt")
        assertContains(titles, "Selling Nut")

        objs.forEach { obj ->
            val resDelete = client.sendAndReceive(
                "ticket/delete", TicketDeleteRequest(
                    debug = debug,
                    ticket = TicketDeleteObject(obj.id, obj.lock),
                )
            ) as TicketDeleteResponse

            assertEquals(ResponseResult.SUCCESS, resDelete.result)
        }
    }
}