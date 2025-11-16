package ru.otus.otuskotlin.marketplace.e2e.be.scenarios.v1

import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.e2e.be.base.client.Client
import ru.otus.otuskotlin.marketplace.e2e.be.scenarios.v1.base.sendAndReceive
import ru.otus.otuskotlin.marketplace.e2e.be.scenarios.v1.base.someCreateTicket
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioUpdateV1(
    private val client: Client,
    private val debug: TicketDebug? = null
) {
    @Test
    fun update() = runBlocking {
        val obj = someCreateTicket
        val resCreate = client.sendAndReceive(
            "ticket/create", TicketCreateRequest(
                requestType = "create",
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

        val uObj = TicketUpdateObject(
            id = cObj.id,
            lock = cObj.lock,
            title = "Selling Nut",
            description = cObj.description,
            ticketType = cObj.ticketType,
            visibility = cObj.visibility,
        )
        val resUpdate = client.sendAndReceive(
            "ticket/update",
            TicketUpdateRequest(
                requestType = "update",
                debug = debug,
                ticket = uObj,
            )
        ) as TicketUpdateResponse

        assertEquals(ResponseResult.SUCCESS, resUpdate.result)

        val ruObj: TicketResponseObject = resUpdate.ticket ?: fail("No ticket in Update response")
        assertEquals(uObj.title, ruObj.title)
        assertEquals(uObj.description, ruObj.description)
        assertEquals(uObj.visibility, ruObj.visibility)
        assertEquals(uObj.ticketType, ruObj.ticketType)

        val resDelete = client.sendAndReceive(
            "ticket/delete", TicketDeleteRequest(
                requestType = "delete",
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