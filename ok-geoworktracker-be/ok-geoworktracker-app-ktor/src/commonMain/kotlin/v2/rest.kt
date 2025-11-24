package ru.otus.otuskotlin.marketplace.app.ktor.v2

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.marketplace.app.ktor.GwtrAppSettings

fun Route.v2Ticket(appSettings: GwtrAppSettings) {
    route("ticket") {
        post("create") {
            call.createTicket(appSettings)
        }
        post("read") {
            call.readTicket(appSettings)
        }
        post("update") {
            call.updateTicket(appSettings)
        }
        post("delete") {
            call.deleteTicket(appSettings)
        }
        post("search") {
            call.searchTicket(appSettings)
        }
    }
}
