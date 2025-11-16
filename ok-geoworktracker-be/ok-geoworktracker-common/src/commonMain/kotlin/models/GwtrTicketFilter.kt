package ru.otus.otuskotlin.marketplace.common.models

data class GwtrTicketFilter(
    var searchString: String = "",
    var ownerId: GwtrUserId = GwtrUserId.NONE,
    var ClaimStatus: GwtrClaimStatus = GwtrClaimStatus.NONE,
)
