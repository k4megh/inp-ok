package ru.otus.otuskotlin.marketplace.app.ktor.v2

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.app.ktor.GwtrAppSettings
import kotlin.reflect.KClass

val clCreate: KClass<*> = ApplicationCall::createTicket::class
suspend fun ApplicationCall.createTicket(appSettings: GwtrAppSettings) =
    processV2<TicketCreateRequest, TicketCreateResponse>(appSettings, clCreate,"create")

val clRead: KClass<*> = ApplicationCall::readTicket::class
suspend fun ApplicationCall.readTicket(appSettings: GwtrAppSettings) =
    processV2<TicketReadRequest, TicketReadResponse>(appSettings, clRead, "read")

val clUpdate: KClass<*> = ApplicationCall::updateTicket::class
suspend fun ApplicationCall.updateTicket(appSettings: GwtrAppSettings) =
    processV2<TicketUpdateRequest, TicketUpdateResponse>(appSettings, clUpdate, "update")

val clDelete: KClass<*> = ApplicationCall::deleteTicket::class
suspend fun ApplicationCall.deleteTicket(appSettings: GwtrAppSettings) =
    processV2<TicketDeleteRequest, TicketDeleteResponse>(appSettings, clDelete, "delete")

val clSearch: KClass<*> = ApplicationCall::searchTicket::class
suspend fun ApplicationCall.searchTicket(appSettings: GwtrAppSettings) =
    processV2<TicketSearchRequest, TicketSearchResponse>(appSettings, clSearch, "search")

