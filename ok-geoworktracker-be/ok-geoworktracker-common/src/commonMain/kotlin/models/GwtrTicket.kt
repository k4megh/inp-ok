package ru.otus.otuskotlin.marketplace.common.models

data class GwtrTicket(
    var id: GwtrTicketId = GwtrTicketId.NONE,
    var title: String = "",
    var description: String = "",
    var ownerId: GwtrUserId = GwtrUserId.NONE,
    var ticketType: GwtrClaimStatus = GwtrClaimStatus.NONE,
    var visibility: GwtrVisibility = GwtrVisibility.NONE,
    var siteId: GwtrSiteId = GwtrSiteId.NONE,
    var lock: GwtrTicketLock = GwtrTicketLock.NONE,
    val permissionsClient: MutableSet<GwtrTicketPermissionClient> = mutableSetOf()
) {
    fun deepCopy(): GwtrTicket = copy(
        permissionsClient = permissionsClient.toMutableSet(),
    )

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = GwtrTicket()
    }

}
