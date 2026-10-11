/**
 * Phase 1 deny-all money contract. Intentionally NO remotely configurable
 * paymentsEnabled switch or payment SDK: nobody can activate charges via APK,
 * forged request.data, or edited Firestore document.
 * Future 5% (500 bps) is a documented proposal, not operational charging.
 */
export type PaymentMode = "DISABLED";
export type BillingSnapshot = Readonly<{
  paymentMode: PaymentMode;
  paymentsEnabled: false;
  bookingFeeRateBps: 500;
  feeAmountMinor: 0;
  settlement: "NOT_APPLICABLE";
  policyVersion: "free-launch-v1";
}>;

export const FREE_BILLING_SNAPSHOT: BillingSnapshot = Object.freeze({
  paymentMode: "DISABLED",
  paymentsEnabled: false,
  bookingFeeRateBps: 500,
  feeAmountMinor: 0,
  settlement: "NOT_APPLICABLE",
  policyVersion: "free-launch-v1",
});

export function calculateBookingFeeMinor(approvedLaborMinor: number): 0 {
  if (!Number.isSafeInteger(approvedLaborMinor) || approvedLaborMinor < 0) {
    throw new RangeError("Approved labor must be a non-negative integer number of centavos");
  }
  return 0;
}

export function rejectMoneyMutation(): never {
  throw new Error("PAID_FEATURE_DISABLED");
}
