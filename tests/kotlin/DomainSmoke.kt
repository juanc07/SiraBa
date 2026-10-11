import net.thinkbloxph.siraba.domain.model.RepairCategory
import net.thinkbloxph.siraba.domain.rules.RepairScopeRules
import net.thinkbloxph.siraba.domain.rules.JobAcceptanceRules

fun main() {
    check(RepairCategory.entries.count { RepairScopeRules.eligibleForInitialPilot(it) } == 3)
    check(RepairScopeRules.eligibleForInitialPilot(RepairCategory.MINOR_PLUMBING))
    check(!RepairScopeRules.eligibleForInitialPilot(RepairCategory.ROOF_PATCH))
    check(!RepairScopeRules.eligibleForInitialPilot(RepairCategory.ELECTRICAL))
    check(!JobAcceptanceRules.mayOfferAccept(verified = false, eligibleCategory = true, available = true))
    check(JobAcceptanceRules.mayOfferAccept(verified = true, eligibleCategory = true, available = true))
    println("PASS: pure Kotlin repair-scope and worker-eligibility smoke checks (6 assertions)")
}
