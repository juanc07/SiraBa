package net.thinkbloxph.siraba.domain.rules

/** Presentation hint only; actual job acceptance requires a server transaction. */
object JobAcceptanceRules {
    fun mayOfferAccept(verified: Boolean, eligibleCategory: Boolean, available: Boolean): Boolean =
        verified && eligibleCategory && available
}
