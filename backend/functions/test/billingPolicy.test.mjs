import test from "node:test";
import assert from "node:assert/strict";
import {
  FREE_BILLING_SNAPSHOT,
  calculateBookingFeeMinor,
  rejectMoneyMutation,
} from "../src/billingPolicy.ts";

test("free launch cannot charge for any valid labor amount", () => {
  for (const labor of [0, 1, 30000, 50000, 100000, Number.MAX_SAFE_INTEGER]) {
    assert.equal(calculateBookingFeeMinor(labor), 0);
  }
});
test("reject invalid monetary values", () => {
  for (const bad of [-1, 0.1, Infinity, NaN, Number.MAX_SAFE_INTEGER + 1]) {
    assert.throws(() => calculateBookingFeeMinor(bad), RangeError);
  }
});
test("billing policy is immutable, disabled and zero fee", () => {
  assert.equal(FREE_BILLING_SNAPSHOT.paymentsEnabled, false);
  assert.equal(FREE_BILLING_SNAPSHOT.paymentMode, "DISABLED");
  assert.equal(FREE_BILLING_SNAPSHOT.bookingFeeRateBps, 500);
  assert.equal(FREE_BILLING_SNAPSHOT.feeAmountMinor, 0);
  assert.equal(FREE_BILLING_SNAPSHOT.settlement, "NOT_APPLICABLE");
  assert.equal(Object.isFrozen(FREE_BILLING_SNAPSHOT), true);
});
test("attempts to perform money mutation fail closed", () => {
  assert.throws(rejectMoneyMutation, /PAID_FEATURE_DISABLED/);
});
