import org.junit.Test
import ru.otus.otuskotlin.marketplace.api.v1.models.TicketCreateRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.TicketCreateResponse
import ru.otus.otuskotlin.marketplace.api.v1.models.TicketDebug
import ru.otus.otuskotlin.marketplace.api.v1.models.TicketRequestDebugMode
import ru.otus.otuskotlin.marketplace.api.v1.models.TicketRequestDebugStubs
import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.models.GwtrTicketId
import ru.otus.otuskotlin.marketplace.common.models.GwtrTicketLock
import ru.otus.otuskotlin.marketplace.common.models.GwtrCommand
import ru.otus.otuskotlin.marketplace.common.models.GwtrError
import ru.otus.otuskotlin.marketplace.common.models.GwtrRequestId
import ru.otus.otuskotlin.marketplace.common.models.GwtrState
import ru.otus.otuskotlin.marketplace.common.models.GwtrUserId
import ru.otus.otuskotlin.marketplace.common.models.GwtrWorkMode
import ru.otus.otuskotlin.marketplace.common.stubs.GwtrStubs
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportTicket
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportCreateTicket
import ru.otus.otuskotlin.marketplace.stubs.GwtrTicketStub
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = TicketCreateRequest(
            debug = TicketDebug(
                mode = TicketRequestDebugMode.STUB,
                stub = TicketRequestDebugStubs.SUCCESS,
            ),
            ticket = GwtrTicketStub.get().toTransportCreateTicket()
        )
        val expected = GwtrTicketStub.prepareResult {
            id = GwtrTicketId.NONE
            ownerId = GwtrUserId.NONE
            lock = GwtrTicketLock.NONE
            permissionsClient.clear()
        }

        val context = GwtrContext()
        context.fromTransport(req)

        assertEquals(GwtrStubs.SUCCESS, context.stubCase)
        assertEquals(GwtrWorkMode.STUB, context.workMode)
        assertEquals(expected, context.ticketRequest)
    }

    @Test
    fun toTransport() {
        val context = GwtrContext(
            requestId = GwtrRequestId("1234"),
            command = GwtrCommand.CREATE,
            ticketResponse = GwtrTicketStub.get(),
            errors = mutableListOf(
                GwtrError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = GwtrState.RUNNING,
        )

        val req = context.toTransportTicket() as TicketCreateResponse

        assertEquals(req.ticket, GwtrTicketStub.get().toTransportTicket())
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}
