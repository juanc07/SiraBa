import { HttpsError, onCall } from "firebase-functions/v2/https";
import { logger } from "firebase-functions";
import { FREE_BILLING_SNAPSHOT } from "./billingPolicy.js";

/**
 * First authenticated, App-Check-protected read-only backend command.
 * Does not write user documents, create a booking or accept client fee fields.
 * No public privileged mutations are deployed in Phase 1.
 */
export const getFoundationStatus = onCall(
  {
    region: "asia-southeast1",
    enforceAppCheck: true,
    memory: "256MiB",
    maxInstances: 2,
    timeoutSeconds: 20,
  },
  async (request) => {
    if (!request.auth?.uid) {
      throw new HttpsError("unauthenticated", "Sign in is required");
    }
    if (request.data !== null && request.data !== undefined &&
        (typeof request.data !== "object" || Array.isArray(request.data) ||
         Object.keys(request.data as object).length !== 0)) {
      throw new HttpsError("invalid-argument", "This endpoint accepts no input fields");
    }
    logger.info("Foundation status requested", { environment: process.env.GCLOUD_PROJECT ?? "unknown" });
    return {
      serverTime: new Date().toISOString(),
      authenticated: true,
      bookingEnabled: false,
      billing: FREE_BILLING_SNAPSHOT,
    };
  },
);
