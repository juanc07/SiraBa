package net.thinkbloxph.siraba.domain.rules

import net.thinkbloxph.siraba.domain.model.RepairCategory

/** Client-side UI eligibility hint, NOT server authority. Server must validate again. */
object RepairScopeRules {
    fun eligibleForInitialPilot(category: RepairCategory): Boolean = when (category) {
        RepairCategory.MINOR_PLUMBING,
        RepairCategory.CLOG_REMOVAL,
        RepairCategory.LOW_RISK_FIXTURES -> true
        else -> false
    }
}
