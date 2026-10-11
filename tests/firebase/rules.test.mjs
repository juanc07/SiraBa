import { before, after, test } from "node:test";
import { initializeTestEnvironment, assertFails } from "@firebase/rules-unit-testing";

let env;
const projectId = process.env.GCLOUD_PROJECT ?? "demo-siraba";

before(async () => {
  env = await initializeTestEnvironment({
    projectId,
    firestore: { host: "127.0.0.1", port: 8080 },
    storage: { host: "127.0.0.1", port: 9199 },
  });
});
after(async () => { if (env) await env.cleanup(); });

test("guest cannot read or write Firestore documents", async () => {
  const db = env.unauthenticatedContext().firestore();
  await assertFails(db.collection("users").doc("private").get());
  await assertFails(db.collection("serviceRequests").doc("test").set({status: "REQUESTED"}));
});

test("logged-in customer cannot forge provider verification or booking", async () => {
  const db = env.authenticatedContext("customer-123").firestore();
  await assertFails(db.collection("workerVerifications").doc("customer-123").set({ status: "VERIFIED" }));
  await assertFails(db.collection("bookings").doc("b1").set({ state: "COMPLETED" }));
  await assertFails(db.collection("billingPolicies").doc("active").set({ paymentsEnabled: true }));
  await assertFails(db.collection("bookings").doc("other-customer").get());
});

test("logged-in worker cannot read customer private documents or upload evidence", async () => {
  const worker = env.authenticatedContext("worker-123");
  await assertFails(worker.firestore().collection("users").doc("customer-123").get());
  await assertFails(worker.storage().ref("evidence/customer-123/id.png").putString("forbidden"));
});

test("guest cannot read evidence from Storage", async () => {
  await assertFails(env.unauthenticatedContext().storage().ref("evidence/customer-123/id.png").getDownloadURL());
});
