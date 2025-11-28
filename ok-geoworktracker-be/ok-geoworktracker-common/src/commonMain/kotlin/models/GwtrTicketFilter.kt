package ru.otus.otuskotlin.marketplace.common.models

data class GwtrTicketFilter(
    var searchString: String = "",
    var ownerId: GwtrUserId = GwtrUserId.NONE,
    var claimStatus: GwtrClaimStatus = GwtrClaimStatus.NONE,
){
    fun deepCopy(): GwtrTicketFilter = copy()

    companion object {
        private val NONE = GwtrTicketFilter()
    }
}
