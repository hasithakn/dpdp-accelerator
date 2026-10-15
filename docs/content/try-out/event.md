# Event Notifications

Event Notifications tell connected systems when something changes, such as a
consent being revoked or a user account being deleted. Subscribers receive
events through a webhook listener or by polling for them.

This page contains two hands-on flows. You'll configure topics and subscriptions,
trigger or publish events, and inspect delivery results in the Consent Portal.
The receiving application runs separately from the portal.

## Choose a flow

- **Recommended first flow — [Notify a processor when a consent is revoked](#notify-a-processor-when-a-consent-is-revoked):** Configure a webhook, revoke a consent, and inspect the delivery. An optional final step shows how a processor reports completion.
- **More technical flow — [Publish your own event and poll for it](#publish-your-own-event-and-poll-for-it):** Register a custom topic, publish an event through the REST API, then retrieve and acknowledge it using polling.

The key concepts used in the exercises are:

- **Topic:** The type of change, such as `consent.revoke`.
- **Subscription:** The topics and delivery method configured for a receiver, with optional filters.
- **Event and delivery history:** Records of published events and their delivery attempts, available in the portal.

## Before you begin

- Ensure you can access the DPDP Accelerator and Consent Portal.
- Ensure test accounts exist in the same tenant for **Anika**, the administrator (`dpdp-consent-admin`), and **Priya**, the Data Principal (`dpdp-consent-user`).
- The main steps use the portal UI where indicated. REST API examples are alternatives or are required where the portal does not perform that operation. Each API step identifies the token and scopes it needs. Follow [Managing Access](../learn/managing-access.md) if you need to configure API access.

## Notify a processor when a consent is revoked

In this flow, **CloudEngage**, CarePulse's email marketing processor, runs a
webhook listener subscribed to `consent.revoke`. You'll start the listener,
subscribe it, revoke a consent as Priya, and inspect the resulting event
delivery. This is the recommended flow to try first.

**Portal:** As **Anika**, CarePulse's administrator, you register the
subscription under **Event Notifications → Subscriptions** and check the
result under **Events**. As the Data Principal, you revoke the consent from
**My Consents**. The listener runs outside the portal, standing in for
CloudEngage's own listener service.

You'll need two users in the tenant: Anika with `dpdp-consent-admin`, and a
Data Principal with `dpdp-consent-user`. We'll call the Data Principal Priya,
with the username `priya@example.com`. They're the same people you met in the
[Learn → Event Notifications](../learn/event.md) stories. Wherever you see
`priya@example.com`, use your own user's username.

### Step 1: Start the webhook listener

A webhook listener receives the subscription verification and each event
delivery from Identity Server. It must be reachable at a public URL, not
`localhost`. Its API contract is in
[`webhook-listener.openapi.yaml`](pathname:///examples/webhook-listener.openapi.yaml).
For this try-out, use one of the sample listeners below.

Each sample listener is a single file, with nothing to install. It answers the
verification challenge, checks every delivery's signature with the shared
secret, and prints whatever it receives.

**Node.js** ([`webhook-listener.mjs`](pathname:///examples/webhook-listener.mjs)) needs Node.js 18 or later:

```bash
node --version

curl -O https://raw.githubusercontent.com/wso2/dpdp-accelerator/main/docs/static/examples/webhook-listener.mjs

export SHARED_SECRET="carepulse-sample-secret-9d3e7b12"
node webhook-listener.mjs
```

**Python** ([`webhook-listener.py`](pathname:///examples/webhook-listener.py)) needs Python 3.9 or later:

```bash
python3 --version

curl -O https://raw.githubusercontent.com/wso2/dpdp-accelerator/main/docs/static/examples/webhook-listener.py

export SHARED_SECRET="carepulse-sample-secret-9d3e7b12"
python3 webhook-listener.py
```

Once it's running, the listener prints its local address and confirms that
HMAC signature checks are enabled:

```text
=============================================================
  DPDP Reference Webhook Listener (Node.js)
  Listening on: http://127.0.0.1:8443/dpdp/events
  HMAC Verification: ENABLED (shared secret configured)
  Press Ctrl+C to stop.
=============================================================
```

The address shown in the terminal (usually `http://127.0.0.1:8443/dpdp/events`)
is local to your machine. Identity Server must be able to reach the listener,
so use a publicly reachable callback URL in the subscription. For local testing,
the [ngrok option below](#exposing-a-listener-locally) can provide one; do not
use a test tunnel for a production receiver.

Make sure to use a strong shared secret in a production environment.

:::info Understand the two signature checks

Event contents are signed, not encrypted. HTTPS protects the contents in transit.
Two separate signatures have different purposes:

- **Event JWT signature:** Identity Server signs the event JWT with its tenant
  private key. A receiver should verify the JWT signature using the matching
  public key published by Identity Server through its JWKS endpoint. This
  verifies the event's authenticity and integrity.
- **Delivery HMAC (`event-signature`):** Webhook deliveries also carry an HMAC
  of the request body, calculated using the subscription's shared secret. The
  sample listener checks this HMAC. It helps verify that the delivery matches
  the secret configured for the subscription.

The shared secret is also used for the signed event's `payloadHash` and to sign
poll requests and completion reports. These checks do not encrypt event data or
replace validating the event JWT signature. Avoid putting unnecessary personal
data in payloads; use references, such as a consent ID, wherever possible.

:::

#### Exposing a listener locally

No public server to run the listener on? Try one of these:

- **An ngrok tunnel.** Set up [ngrok](https://ngrok.com/docs/start), run
  `ngrok http 8443` in a second terminal, then add `/dpdp/events` to the
  `https://` forwarding address it prints. That's your callback URL for
  Step 3. Your test events travel through ngrok, so stick to sample data, and
  don't use a tunnel for a real receiver.
- **Your machine's network IP**, if Identity Server can't reach the internet.
  Start the listener with `HOST` set to that IP, and use
  `http://<ip>:8443/dpdp/events` as the callback URL. Identity Server turns
  away private network addresses by default, so also set
  `allow_private_network_callback_targets = true` under
  `[dpdp_accelerator.event_notifications.webhook]` in `deployment.toml` and
  restart it. Keep that setting `false` in production.

For the listeners' other options, see
[Run a sample reference listener](../event-notification-guide.md#run-the-sample-listener).

### Step 2: Create a consent to revoke

The Consent Portal does not create consent records. In a real deployment, a
connected application creates them through the Consent Management API. For this
exercise, you'll create a test consent with one API request, then revoke it in
the portal as Priya.

1. **Get a purpose ID and an element ID.** Sign in to the portal as the
   administrator, go to **Definitions → Purposes**, and open the purpose you
   want to use. This flow uses `marketing-email`. No purpose yet? Create one
   with an element first, as described in
   [Define a purpose and its data element](consent.md#define-a-purpose-and-its-data-element).
   Copy the **Purpose ID** from the purpose page, then open one of its
   elements and copy its ID as well.

   ![Purpose details page with the Purpose ID and its Contact email element](../../assets/images/try-out/event/00-purpose-id.png)

2. **Get an access token with the `internal_consent_mgt_consent_create`
   scope.** Creating a consent needs it, and the accelerator provisions the
   **DPDP Consent API Invoker** application for exactly this.
   [Consent API Invoker provisioning](../install-and-setup/configuring-the-accelerator.md#consent-api-invoker-provisioning)
   explains how to get its credentials. Get the token from the same tenant
   the consent belongs to, since one tenant won't accept another tenant's
   token. Then set it, along with your server, for the next step:

   ```bash
   export BASE_URL="https://localhost:9443"
   export TOKEN="<access-token>"
   ```

3. **Create an active consent for Priya.** `subjectId` is the Data
   Principal's username, `priya@example.com`, and it has to belong to a user
   in the same tenant. On the highlighted line, swap in the
   **`<purpose-id>`** and **`<element-id>`** you copied in step 1:

   ```bash {10}
   curl -sk -X POST "${BASE_URL}/api/identity/consent-mgt/v2.0/consents" \
     -H "Authorization: Bearer ${TOKEN}" \
     -H "Content-Type: application/json" \
     -d '{
       "subjectId": "priya@example.com",
       "serviceId": "carepulse-marketing",
       "language": "en",
       "state": "ACTIVE",
       "purposes": [
         { "id": "<purpose-id>", "elements": [ { "id": "<element-id>" } ] }
       ]
     }'
   ```

   The response includes the new consent's `id`. Keep it for reference; use the
actual ID from your response rather than the example IDs shown later.

`-k` skips certificate checks. Use it only against a local server with a
self-signed certificate, never in production.

### Step 3: Subscribe the listener to consent revocations

1. Sign in to the portal as Anika, the administrator.
2. Open **Event Notifications → Subscriptions** and select **Register
   Subscription**.
3. Enter a **Subscription Name**, such as `CloudEngage - consent
   revocations`.
4. Set **Topic Category** to **Consent Topics**, and select `consent.revoke`
   under **Topics**.
5. Set **Consent Purpose Filter Mode** to **Specific Purposes**, and select
   `marketing-email` under **Consent Purposes**. The listener then hears only
   about revocations of consents that include this purpose. To hear about
   every revocation, choose **All Purposes** instead.
6. Set **Delivery Mode** to **Webhook**, enter your callback URL from Step 1
   as the **Webhook Callback URL** (for example,
   `https://<your-ngrok-address>/dpdp/events`), and enter
   `carepulse-sample-secret-9d3e7b12` as the **Shared Secret**.
7. Select **Register Subscription**.

![Register Subscription dialog filled in for consent.revoke with the marketing-email purpose](../../assets/images/try-out/event/01-register-subscription.png)

Right away, Identity Server sends the listener a verification challenge. As
soon as the listener answers it, the subscription turns **Active**:

![Subscriptions list with the new consent.revoke subscription in Active status](../../assets/images/try-out/event/02-subscription-active.png)

Over in the listener's terminal, you'll see the challenge it answered:

```text
[INFO] ================ [SUBSCRIPTION VERIFICATION] ================
[INFO] Verification Payload:
{
  "type": "subscription.verification",
  "subscriptionId": "e4e10144-e76a-4d4b-9e80-70747df4f650",
  "topics": [
    "consent.revoke"
  ],
  "challenge": "ec09e7a8-fb5b-4eec-9670-c3c1fbe529b9"
}
[INFO] Verified subscription 'e4e10144-e76a-4d4b-9e80-70747df4f650' for topics: ["consent.revoke"]
[INFO] <-- Returned HTTP 200 OK with challenge: 'ec09e7a8-fb5b-4eec-9670-c3c1fbe529b9'
```

**Optional: Register the subscription using the REST API**

If you'd rather register the subscription through the API, here's the
equivalent request. It uses the administrator's access token, which carries
`notifications:subscriptions:write`:

```bash
export ADMIN_TOKEN="<admin-access-token>"

curl -X POST \
  "${BASE_URL}/api/dpdp/event-notifications/v1/subscriptions" \
  -H "Authorization: Bearer ${ADMIN_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "topics": ["consent.revoke"],
    "filter": {
      "type": "specific",
      "purposes": ["marketing-email"]
    },
    "delivery": {
      "mode": "webhook",
      "callbackUrl": "https://<your-ngrok-address>/dpdp/events",
      "sharedSecret": "<shared-secret>"
    }
  }'
```

### Step 4: Revoke the consent

1. Sign in to the portal as Priya (`priya@example.com`).
2. Open **My Consents** and select the consent you created. It should show
   **Active**.

   ![Consent details page for an active marketing-email consent with the Revoke button](../../assets/images/try-out/event/03-consent-active.png)

3. Select **Revoke**, then **Revoke Consent** to confirm.

   ![Confirm Revocation dialog](../../assets/images/try-out/event/04-confirm-revocation.png)

The consent now shows **Revoked**. The portal performs this action for you.

**Optional: Revoke the consent using the REST API**

If you make the request yourself, use Priya's access token and the actual
consent ID. Do not use the administrator's or Consent API Invoker's token for
this self-service operation.

```bash
export PRIYA_TOKEN="<priya-access-token>"

curl -X POST \
  "${BASE_URL}/api/users/v1/me/consents/<consent-id>/revoke" \
  -H "Authorization: Bearer ${PRIYA_TOKEN}"
```

### Step 5: Confirm that the processor was notified

Within a few seconds, the event should reach the listener. The sample checks
the delivery HMAC and prints the decoded event, including the consent ID Priya
just revoked:

```text
[INFO] ==================== [EVENT DELIVERY] ====================
[INFO] Delivery-Id:     58328a80-4e74-44c8-80cc-927414e4159a
[INFO] Event-Signature: sha256=e8c0443706bca2452875971e20bdc909867a12298bbd99907d36edcbf4092245
[INFO] HMAC-SHA256 signature verified successfully.
[INFO] Decoded Event Payload:
{
  "iss": "https://localhost:9443/oauth2/token",
  "sub": "carbon.super",
  "aud": "dpdp-event-notifications",
  ...
  "payload": {
    "deliveryId": "58328a80-4e74-44c8-80cc-927414e4159a",
    "eventId": "131b9ab4-c6f2-4652-ae70-023dafc35c8f",
    "subscriptionId": "e4e10144-e76a-4d4b-9e80-70747df4f650",
    "orgId": "carbon.super",
    "groupId": "carbon.super",
    "topic": "consent.revoke",
    "eventPayload": {
      "consentId": "c540bcee-c9d8-4919-8fdc-e2b9723971f5",
      "previousStatus": "ACTIVE"
    }
  }
}
[INFO] <-- Returned HTTP 202 Accepted
```

You can find the same event in the portal too. Sign in as the administrator,
open **Event Notifications → Events**, and search for the `eventId` from the
listener output, or just look for the newest `consent.revoke` event:

![Events list showing the consent.revoke event for the marketing-email purpose with one subscriber](../../assets/images/try-out/event/06-events-list.png)

Open it to see the payload and the delivery to your subscription, marked
**Delivered**:

![Event details page with the consent.revoke payload and a Delivered webhook delivery](../../assets/images/try-out/event/07-event-delivered.png)

Notice that the event carries only the consent ID and its previous status,
none of Priya's personal data. A real processor would use `consentId` to find
the data it holds under that consent and stop using it.

Expected result: revoking the consent publishes a `consent.revoke` event, and
CloudEngage's listener receives it because the subscription matches both the
topic and the `marketing-email` purpose. **Delivered** means the listener
accepted the notification; CloudEngage is still responsible for acting on it.
See [receiver responsibilities](../event-notification-guide.md#acceptance-retries-and-processing-responsibilities)
for more information.

### Step 6: Report completion

**Optional follow-up:** You can stop after Step 5 if you only want to verify
webhook delivery. Continue here to demonstrate how a processor reports that it
has acted on an event.

A `2xx` response confirms receipt, not completion. After CloudEngage has updated
Priya's marketing preferences, it can submit a signed completion report so the
accelerator records that confirmation alongside the delivery.

This needs the `notifications:event-deliveries:complete` scope, which no
default role grants. Grant it to a dedicated role or application for
CloudEngage — see
[Event Notification roles and scopes](../role-guide.md#event-notification-roles-and-scopes).

Use the actual `deliveryId` and `groupId` shown in your Step 5 event output;
the values in the sample output are illustrative. The request needs a token
with `notifications:event-deliveries:complete` and the same shared secret used
when you registered the subscription.

```bash
export CLOUDENGAGE_TOKEN="<cloudengage-access-token>"
export GROUP_ID="<group-id-from-step-5-output>"
export SHARED_SECRET="carepulse-sample-secret-9d3e7b12"
DELIVERY_ID="<delivery-id-from-step-5>"
COMPLETION_BODY='{"completionStatus":"completed","completionEvidence":"https://cloudengage.example/evidence/receipt.pdf"}'
COMPLETION_SIGNATURE="sha256=$(printf 'v1\ncompletion\n%s\n%s' "${DELIVERY_ID}" "${COMPLETION_BODY}" \
  | openssl dgst -sha256 -hmac "${SHARED_SECRET}" -hex | awk '{print $2}')"

curl -X POST \
  "${BASE_URL}/api/dpdp/event-notifications/v1/deliveries/${DELIVERY_ID}/completion" \
  -H "Authorization: Bearer ${CLOUDENGAGE_TOKEN}" \
  -H "group-id: ${GROUP_ID}" \
  -H "event-signature: ${COMPLETION_SIGNATURE}" \
  -H "Content-Type: application/json" \
  -d "${COMPLETION_BODY}"
```

A successful call returns `204 No Content`. Reopen the event in **Event
Notifications → Events** and the delivery still shows **Delivered**, now with
**Completion Evidence** recorded alongside it:

![Delivery attempt history showing the Delivered status with Completion Evidence recorded below it](../../assets/images/learn/event-delivery-completion-evidence.png)

Submitting a second completion for the same delivery returns `409 Conflict`
(`EN-4090`). For the full request/response schema, see
[Submit webhook delivery completion](../event-notification-guide.md#submit-webhook-delivery-completion)
in the Event Notification Guide.

## Publish your own event and poll for it

This is the more technical flow. The accelerator publishes consent and user
lifecycle events itself, while connected applications can publish events about
their own business changes on custom topics.

Say CarePulse's order system publishes an event whenever a customer updates
their delivery preferences. MedExpress, the delivery processor, sits behind a
firewall and can't expose a webhook. Instead, it polls for new events and
acknowledges each one once it has processed it.

You'll register a custom topic, subscribe a polling receiver, publish an
event, retrieve it through the polling API, and acknowledge it after processing.

**Portal:** As the portal administrator, you register the topic and
subscription and check the result. Publishing and polling are API calls that
the integrating applications make, not portal actions.

Portal sign-in is used to register the topic and subscription. The REST API
steps need two tokens. [Event Notification roles and scopes](../role-guide.md#event-notification-roles-and-scopes)
lists the roles that grant them:

| Token | Scope | Used by |
| --- | --- | --- |
| Publisher | `notifications:events:write` | The application that publishes the event. Users with `dpdp-consent-admin` already have this scope. |
| Receiver | `notifications:events:poll` | The polling receiver. No default role has this scope, so grant it to a dedicated receiver role or application. |

```bash
export BASE_URL="https://localhost:9443"
export TENANT_DOMAIN="example.com"
export API_BASE="${BASE_URL}/api/dpdp/event-notifications/v1"
export PUBLISHER_TOKEN="<publisher-access-token>"
export RECEIVER_TOKEN="<receiver-access-token>"
```

Change `TENANT_DOMAIN` to your own tenant's domain, and get both tokens from
that same tenant.

### Step 1: Register a custom topic

1. Sign in to the portal as Anika, the administrator.
2. Open **Event Notifications → Topics** and select **Register Topic**.
3. Enter `delivery.preferences.update` as the **Topic Name**, add a
   **Description**, and select **Register Topic**.

![Register Topic dialog for delivery.preferences.update](../../assets/images/try-out/event/10-register-topic.png)

### Step 2: Subscribe a poll receiver

1. Open **Event Notifications → Subscriptions** and select **Register
   Subscription**.
2. Enter a **Subscription Name**, such as `MedExpress - delivery preferences`.
3. Set **Topic Category** to **Custom Topics**, and select
   `delivery.preferences.update` under **Topics**.
4. Keep **Consent Purpose Filter Mode** as **All Purposes**.
5. Set **Delivery Mode** to **Poll**.
6. Enter `medexpress-sample-secret-4f8a2c91` as the **Shared Secret**. The
   receiver signs its poll requests with it. We use a sample value so the
   commands below work as they are. For a real receiver, select the generate
   icon at the end of the field instead, and keep the secret somewhere safe.
7. Select **Register Subscription**.

![Register Subscription dialog for a poll subscription with the sample shared secret](../../assets/images/try-out/event/11-register-poll-subscription.png)

Poll subscriptions don't need verification, so this one is **Active**
straight away. Copy its **Subscription ID** with the copy icon in the list:

![Subscriptions list with the active poll subscription](../../assets/images/try-out/event/12-poll-subscription-active.png)

```bash
export SUBSCRIPTION_ID="<subscription-id>"
export SHARED_SECRET="medexpress-sample-secret-4f8a2c91"
```

### Step 3: Publish an event

Now play the order system and publish the change with the publisher token.
The `group-id` header has to match the subscription's group, which is the
tenant domain:

```bash
curl -sk -X POST "${API_BASE}/events" \
  -H "Authorization: Bearer ${PUBLISHER_TOKEN}" \
  -H "group-id: ${TENANT_DOMAIN}" \
  -H "Content-Type: application/json" \
  -d '{
    "topic": "delivery.preferences.update",
    "payload": {
      "customerReference": "cust-0001",
      "change": "DELIVERY_WINDOW_UPDATED"
    }
  }'
```

You'll get back the stored event and its `eventId`:

```json
{
  "eventId": "f0b66a0d-edcb-4d94-94ee-70ce33b34f20",
  "orgId": "example.com",
  "groupId": "example.com",
  "topicId": "3064830b-7249-4fa6-9b83-59d7215af1c2",
  "payload": "{\"customerReference\":\"cust-0001\",\"change\":\"DELIVERY_WINDOW_UPDATED\"}",
  "occurredAt": 1791199075832,
  "createdAt": 1791199075832
}
```

Try to keep personal data out of the payload. Send a reference the receiver
can look up instead, the way `customerReference` does here.

### Step 4: Poll for the event

Next, switch to MedExpress and poll with the receiver token. The first poll
can have an empty body:

```bash
POLL_BODY=''
POLL_SIGNATURE="sha256=$(printf %s "${POLL_BODY}" | openssl dgst -sha256 -hmac "${SHARED_SECRET}" -hex | awk '{print $2}')"

curl -sk -X POST "${API_BASE}/events/poll" \
  -H "Authorization: Bearer ${RECEIVER_TOKEN}" \
  -H "Content-Type: application/json" \
  -H "group-id: ${TENANT_DOMAIN}" \
  -H "subscription-id: ${SUBSCRIPTION_ID}" \
  -H "event-signature: ${POLL_SIGNATURE}" \
  -d "${POLL_BODY}"
```

`event-signature` is an HMAC-SHA256 of the exact request body, made with the
subscription's shared secret. For an empty body, it's calculated over zero
bytes. The server only checks it when
`request_hmac_validation_enabled = true` under
`[dpdp_accelerator.event_notifications.polling]`, and that's off by default.
Sign every request anyway, so your receiver keeps working when someone turns
the check on.

The response holds pending deliveries in the `sets` object. Each **key** is
a delivery ID; its **value** is a signed event (a compact JWS) carrying the
same contents as a webhook delivery. Save the delivery ID key because you'll
need it in Step 5 to acknowledge the event:

```json
{
  "moreAvailable": false,
  "sets": {
    "07f813ee-391a-4cd2-aa70-2ebe420fc0d0": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIs..."
  }
}
```

Before you trust an event, verify its signature against Identity Server's
JWKS endpoint. Once it checks out, you'll find your event in the `payload`
claim, under `eventPayload`:

```json
"eventPayload": {
  "change": "DELIVERY_WINDOW_UPDATED",
  "customerReference": "cust-0001"
}
```

### Step 5: Acknowledge the event

Once MedExpress has processed the event, it sends the delivery ID back in the
next poll's `ack` array, signing the new body the same way:

```bash
DELIVERY_ID="<delivery-id-from-step-4>"
POLL_BODY="{\"ack\": [\"${DELIVERY_ID}\"], \"maxEvents\": 20}"
POLL_SIGNATURE="sha256=$(printf %s "${POLL_BODY}" | openssl dgst -sha256 -hmac "${SHARED_SECRET}" -hex | awk '{print $2}')"

curl -sk -X POST "${API_BASE}/events/poll" \
  -H "Authorization: Bearer ${RECEIVER_TOKEN}" \
  -H "Content-Type: application/json" \
  -H "group-id: ${TENANT_DOMAIN}" \
  -H "subscription-id: ${SUBSCRIPTION_ID}" \
  -H "event-signature: ${POLL_SIGNATURE}" \
  -d "${POLL_BODY}"
```

An acknowledged event isn't returned again, so with nothing else pending,
the response comes back empty:

```json
{"moreAvailable":false,"sets":{}}
```

If the receiver couldn't process an event, report it in `setErrs` instead of
`ack`. A delivery ID goes in one or the other, never both.

### Step 6: Check the result in the portal

Back in the portal as Anika, the administrator, open **Event Notifications →
Events** and find the `delivery.preferences.update` event. Its delivery to the
poll subscription now shows **Acknowledged**:

![Event details page with the custom event payload and an Acknowledged poll delivery](../../assets/images/try-out/event/13-poll-event-acknowledged.png)

When you're done, tidy up by deleting the subscription and deregistering the
topic in the portal.

Expected result: the published event is queued for the poll subscription, the
receiver picks it up on its first poll, and once it's acknowledged it no
longer shows up in later polls. For the full request options, errors, and HMAC
settings, see
[Register a poll subscription](../event-notification-guide.md#register-a-poll-subscription)
and [Poll event deliveries](../event-notification-guide.md#poll-event-deliveries).

## Next Steps

- [Learn: Event Notifications](../learn/event.md) — revisit the concepts behind these flows.
- [Try Out: Consent](consent.md) — create, review, and revoke a consent to trigger a lifecycle event.
- [Event Notification Guide](../event-notification-guide.md) — the reference for topics, subscriptions, receivers, signatures, and troubleshooting.
