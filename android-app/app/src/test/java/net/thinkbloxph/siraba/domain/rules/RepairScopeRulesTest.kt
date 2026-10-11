package net.thinkbloxph.siraba.domain.rules

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import net.thinkbloxph.siraba.domain.model.RepairCategory

class RepairScopeRulesTest {
    @Test fun onlyThreeLowRiskCategoriesInitiallyEnabled() {
        val allowed = RepairCategory.entries.filter(RepairScopeRules::eligibleForInitialPilot)
        assertTrue(allowed.size == 3)
        assertTrue(RepairScopeRules.eligibleForInitialPilot(RepairCategory.MINOR_PLUMBING))
        assertFalse(RepairScopeRules.eligibleForInitialPilot(RepairCategory.ROOF_PATCH))
        assertFalse(RepairScopeRules.eligibleForInitialPilot(RepairCategory.ELECTRICAL))
    }
    @Test fun unverifiedWorkerCannotOfferToAccept() {
        assertFalse(JobAcceptanceRules.mayOfferAccept(false, true, true))
        assertTrue(JobAcceptanceRules.mayOfferAccept(true, true, true))
    }
}
