package ru.otus.otuskotlin.marketplace.e2e.be.scenarios.v2

import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.e2e.be.base.client.Client
import ru.otus.otuskotlin.marketplace.e2e.be.scenarios.v2.base.sendAndReceive
import ru.otus.otuskotlin.marketplace.e2e.be.scenarios.v2.base.someCreateTicket
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioReadV2(
    private val client: Client,
    private val debug: TicketDebug? = null
) {
    @Test
    fun read() = runBlocking {
        val obj = someCreateTicket
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

        val rObj = TicketReadObject(
            id = cObj.id,
        )
        val resRead = client.sendAndReceive(
            "ticket/read",
            TicketReadRequest(
                debug = debug,
                ticket = rObj,
            )
        ) as TicketReadResponse

        assertEquals(ResponseResult.SUCCESS, resRead.result)

        val rrObj: TicketResponseObject = resRead.ticket ?: fail("No ticket in Read response")
        assertEquals(obj.title, rrObj.title)
        assertEquals(obj.description, rrObj.description)
        assertEquals(obj.visibility, rrObj.visibility)
        assertEquals(obj.ticketType, rrObj.ticketType)

        val resDelete = client.sendAndReceive(
            "ticket/delete", TicketDeleteRequest(
                debug = debug,
                ticket = TicketDeleteObject(cObj.id, cObj.lock),
            )
        ) as TicketDeleteResponse

        assertEquals(ResponseResult.SUCCESS, resDelete.result)

        val dObj: TicketResponseObject = resDelete.ticket ?: fail("No ticket in Delete response")
        assertEquals(obj.title, dObj.title)
        assertEquals(obj.description, dObj.description)
        assertEquals(obj.visibility, dObj.visibility)
        assertEquals(obj.ticketType, dObj.ticketType)
    }
}