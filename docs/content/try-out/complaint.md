---
title: Complaints
---

# Complaint Management

Use this walkthrough to submit, investigate, and resolve a complaint through the Consent Portal. Equivalent REST API examples show how to perform the same operations programmatically.

The example follows **Priya**, the Data Principal, and **Ravi**, the Complaint Officer, through submission, evidence review, communication, and resolution.

For the complete list of endpoints and schemas, see the [Complaint Server API](https://github.com/wso2/dpdp-accelerator/blob/main/dpdp-accelerator/internal-webapps/org.wso2.dpdp.accelerator.complaint.mgt.endpoint/src/main/resources/complaint-server-API.yaml) specification.

![Grievance flow from Data Principal submission through complaint-officer review, communication, and resolution](../../assets/images/diagrams/dpdp-grievance-flow.svg)

## Before you begin

- Ensure you can access the DPDP Accelerator and Consent Portal.
- Create test accounts for **Priya** (Data Principal) and **Ravi** (Complaint Officer) in the same tenant, with the appropriate roles assigned.

The walkthrough focuses on the portal UI. REST API examples are provided as alternatives for performing the same operations programmatically.

| User | Portal area | API namespace |
|---|---|---|
| **Priya** — Data Principal | **My Complaints** | `/api/dpdp/complaints/v1/me/complaints` |
| **Ravi** — Complaint Officer | **Administration → Complaints** | `/api/dpdp/complaints/v1/complaints` |

For API testing, use the appropriate access token:

- **Priya's token:** Requires `complaints:read:self` and `complaints:write:self` for self-service operations.
- **Ravi's token:** Requires `complaints:read:any` and `complaints:write:any` for officer operations. Alternatively, use an M2M client-credentials token authorized for these scopes when calling officer-level APIs programmatically.

Follow [Managing Access](../learn/managing-access.md) to configure API access and obtain the required tokens.

Set the following variables when running the API examples:

```bash
export BASE_URL="https://<your-is-host>"
export DATA_PRINCIPAL_TOKEN="<priya-access-token>"
export DPO_TOKEN="<ravi-access-token-or-authorized-m2m-token>"
```

Use `${DATA_PRINCIPAL_TOKEN}` for `/me/complaints` operations and `${DPO_TOKEN}` for officer-level operations. For local testing with a self-signed certificate, `curl` may require `-k`; do not use it in production.

## 1. Submit a complaint

### In the Consent Portal

1. Sign in as **Priya** and open **COMPLAINTS → My Complaints**.

   ![My Complaints view for Data Principal](../../assets/images/try-out/complaints/my-complaints-empty.png)

2. Select **+ Submit a complaint** and enter the details:
   - **Category:** For example, `Consent withdrawal issue`.
   - **Description:** Explain the issue without including unnecessary sensitive personal data. For example: *"I revoked my consent for marketing purposes, but I am still receiving promotional messages."*
   - **Attachments (optional):** Upload supporting evidence, such as a screenshot. Supported formats include PNG, JPG, PDF, and DOCX, up to 10 MB per file and five files per complaint.
3. Select **Submit complaint**.

   ![Submit a complaint dialog](../../assets/images/try-out/complaints/submit-complaint-modal.png)

4. Confirm that the complaint appears in **My Complaints** with a reference ID, status, and submission date.

   ![Complaint submitted confirmation and table entry](../../assets/images/try-out/complaints/complaint-submitted-success.png)

5. Open the complaint to review its status and statutory due date. The **Activity** tab shows the timeline and supports public follow-up messages; the **Attachments** tab lists submitted evidence.

   ![Data Principal complaint detail view](../../assets/images/try-out/complaints/complaint-details-principal-view.png)

### Using the REST API

The self-service API identifies the complainant from the bearer token, so do not include `userId` in the request.

```bash
curl -X POST \
  "${BASE_URL}/api/dpdp/complaints/v1/me/complaints" \
  -H "Authorization: Bearer ${DATA_PRINCIPAL_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "subjectCategory": "CONSENT_WITHDRAWN_DATA_STILL_USED",
    "description": "I revoked my consent for marketing purposes, but I am still receiving promotional messages."
  }'
```

A successful request returns the complaint reference, status, and statutory due date. Keep the returned complaint ID for the following steps.

To fetch the complaint later, replace `<complaint-id>` with the ID returned above:

```bash
curl \
  "${BASE_URL}/api/dpdp/complaints/v1/me/complaints/<complaint-id>" \
  -H "Authorization: Bearer ${DATA_PRINCIPAL_TOKEN}"
```

## 2. Investigate and communicate

### In the Consent Portal

1. Sign in as **Ravi** and open **ADMINISTRATION → Complaints**.
2. Find the complaint by its reference ID and open it.

   ![Complaint Officer queue under Administration > Complaints](../../assets/images/try-out/complaints/officer-complaints-queue.png)

3. Review the complaint details, due date, and evidence in the **Attachments** tab.

   ![Complaint Officer case view with Reply and Internal Note options](../../assets/images/try-out/complaints/officer-complaint-handling.png)

4. Use **REPLY** to send a message visible to Priya, such as *"We are reviewing your request."* Use **INTERNAL NOTE** for investigation notes that should only be visible to officers.
5. Sign back in as Priya and verify that the public reply appears in the activity timeline and the internal note is not visible.

### Using the REST API

`${DPO_TOKEN}` here can be Ravi's own signed-in access token, or a client-credentials token from an M2M application authorized for `complaints:read:any`/`complaints:write:any` — both carry the same officer-level scopes.

Replace `<complaint-id>` with the ID returned when the complaint was created. Fetch its full detail, including Priya's description and any attachments:

```bash
curl \
  "${BASE_URL}/api/dpdp/complaints/v1/complaints/<complaint-id>" \
  -H "Authorization: Bearer ${DPO_TOKEN}"
```

Ravi can then post a public reply and move the complaint to `IN_PROGRESS` in one request:

```bash
curl -X POST \
  "${BASE_URL}/api/dpdp/complaints/v1/complaints/<complaint-id>/comments" \
  -H "Authorization: Bearer ${DPO_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "We are reviewing your request.",
    "isPublic": true,
    "toStatus": "IN_PROGRESS"
  }'
```

To add an internal note instead, set `"isPublic": false`. Internal notes are visible to officers only.

## 3. Resolve the complaint

### In the Consent Portal

1. As Ravi, open the complaint and use the status menu beside the comment composer.
2. Select **RESOLVED**, enter a final message describing the outcome, and submit it.
3. Confirm that the complaint shows **Resolved**.

   ![Final resolved view in Complaint Management](../../assets/images/try-out/complaints/complaint-final-resolved-view.png)

4. Sign back in as Priya and verify that the resolution message and status appear in **My Complaints**.

### Using the REST API

Use Ravi's token to post the final public message and set the status to `RESOLVED`:

```bash
curl -X POST \
  "${BASE_URL}/api/dpdp/complaints/v1/complaints/<complaint-id>/comments" \
  -H "Authorization: Bearer ${DPO_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "The marketing preferences have been updated, and promotional messages have stopped.",
    "isPublic": true,
    "toStatus": "RESOLVED"
  }'
```

## Next Steps

- [Learn: Complaints](../learn/complaint.md) — understand the grievance-handling concepts behind this walkthrough.
- [Try Out: Event Notifications](event.md) — explore event delivery and consent lifecycle notifications.
- [Developer Guide: Grievances](../grievances-guide.md) — see the API reference, attachment limits, and email template configuration.
