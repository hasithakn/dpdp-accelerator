/*
 * Copyright (c) 2026, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.dpdp.accelerator.event.notifications.dao.impl;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.dpdp.accelerator.event.notifications.common.enums.DeliveryStatus;
import org.wso2.dpdp.accelerator.event.notifications.dao.model.WebhookDelivery;
import org.wso2.dpdp.accelerator.event.notifications.dao.model.WebhookDeliveryAudit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.UUID;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Minimal real-SQL coverage for {@link DeliveryDAOImpl} against an in-memory H2 schema.
 */
public class DeliveryDAOImplTest {

    private Connection conn;
    private final DeliveryDAOImpl dao = new DeliveryDAOImpl();

    @BeforeMethod
    public void setUp() throws Exception {

        conn = DaoTestSchema.newConnection();
        insertTopic(conn, "topic-1", "org-1");
        insertEvent(conn, "event-1", "org-1", "topic-1");
    }

    @AfterMethod
    public void tearDown() throws Exception {

        if (conn != null) {
            conn.close();
        }
    }

    @Test
    public void addWebhookDeliveryPersistsAllFields() throws Exception {

        Timestamp now = new Timestamp(System.currentTimeMillis());
        WebhookDelivery delivery = new WebhookDelivery("deliv-1", "sub-1", "event-1",
                DeliveryStatus.PENDING.getValue(), 0, null, now, now, null);

        boolean added = dao.addWebhookDelivery(conn, delivery);

        assertTrue(added);
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT SUBSCRIPTION_ID, STATUS, ATTEMPT_COUNT FROM WEBHOOK_DELIVERY WHERE DELIVERY_ID = ?")) {
            ps.setString(1, "deliv-1");
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(rs.getString("SUBSCRIPTION_ID"), "sub-1");
                assertEquals(rs.getString("STATUS"), DeliveryStatus.PENDING.getValue());
                assertEquals(rs.getInt("ATTEMPT_COUNT"), 0);
            }
        }
    }

    @Test
    public void updateWebhookDeliveryStatusOnlyAffectsInFlightRows() throws Exception {

        insertWebhookDelivery(conn, "deliv-1", DeliveryStatus.IN_FLIGHT.getValue(), 0);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        WebhookDelivery updated = new WebhookDelivery("deliv-1", "sub-1", "event-1",
                DeliveryStatus.DELIVERED.getValue(), 1, null, now, now, now);

        boolean result = dao.updateWebhookDeliveryStatus(conn, updated);

        assertTrue(result);
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT STATUS, ATTEMPT_COUNT FROM WEBHOOK_DELIVERY WHERE DELIVERY_ID = ?")) {
            ps.setString(1, "deliv-1");
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(rs.getString("STATUS"), DeliveryStatus.DELIVERED.getValue());
                assertEquals(rs.getInt("ATTEMPT_COUNT"), 1);
            }
        }
    }

    @Test
    public void updateWebhookDeliveryStatusIsANoOpWhenRowIsNotInFlight() throws Exception {

        insertWebhookDelivery(conn, "deliv-1", DeliveryStatus.PENDING.getValue(), 0);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        WebhookDelivery updated = new WebhookDelivery("deliv-1", "sub-1", "event-1",
                DeliveryStatus.DELIVERED.getValue(), 1, null, now, now, now);

        boolean result = dao.updateWebhookDeliveryStatus(conn, updated);

        assertFalse(result);
    }

    @Test
    public void releaseWebhookDeliveryFlipsInFlightBackToPending() throws Exception {

        insertWebhookDelivery(conn, "deliv-1", DeliveryStatus.IN_FLIGHT.getValue(), 1);
        Timestamp nextRetryAt = new Timestamp(System.currentTimeMillis() + 5000L);

        boolean released = dao.releaseWebhookDelivery(conn, "deliv-1", 2, nextRetryAt);

        assertTrue(released);
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT STATUS, ATTEMPT_COUNT FROM WEBHOOK_DELIVERY WHERE DELIVERY_ID = ?")) {
            ps.setString(1, "deliv-1");
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(rs.getString("STATUS"), DeliveryStatus.PENDING.getValue());
                assertEquals(rs.getInt("ATTEMPT_COUNT"), 2);
            }
        }
    }

    @Test
    public void addWebhookDeliveryAuditPersistsAllFields() throws Exception {

        insertWebhookDelivery(conn, "deliv-1", DeliveryStatus.PENDING.getValue(), 0);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        WebhookDeliveryAudit audit = new WebhookDeliveryAudit(UUID.randomUUID().toString(), "event-1", "deliv-1",
                "org-1", "500", now, now);

        boolean added = dao.addWebhookDeliveryAudit(conn, audit);

        assertTrue(added);
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT RESPONSE_CODE FROM WEBHOOK_DELIVERY_AUDIT WHERE DELIVERY_ID = ?")) {
            ps.setString(1, "deliv-1");
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(rs.getString("RESPONSE_CODE"), "500");
            }
        }
    }

    private static void insertTopic(Connection conn, String topicId, String orgId) throws Exception {

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO TOPIC (TOPIC_ID, ORG_ID, NAME, DESCRIPTION, STATUS, INITIATED_BY) "
                        + "VALUES (?, ?, 'billing', 'test topic', 'active', 'USER')")) {
            ps.setString(1, topicId);
            ps.setString(2, orgId);
            ps.executeUpdate();
        }
    }

    private static void insertEvent(Connection conn, String eventId, String orgId, String topicId) throws Exception {

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO EVENT (EVENT_ID, ORG_ID, GROUP_ID, TOPIC_ID, PAYLOAD) VALUES (?, ?, 'group-1', ?, '{}')")) {
            ps.setString(1, eventId);
            ps.setString(2, orgId);
            ps.setString(3, topicId);
            ps.executeUpdate();
        }
    }

    private static void insertWebhookDelivery(Connection conn, String deliveryId, String status, int attemptCount)
            throws Exception {

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO WEBHOOK_DELIVERY (DELIVERY_ID, SUBSCRIPTION_ID, EVENT_ID, STATUS, ATTEMPT_COUNT) "
                        + "VALUES (?, 'sub-1', 'event-1', ?, ?)")) {
            ps.setString(1, deliveryId);
            ps.setString(2, status);
            ps.setInt(3, attemptCount);
            ps.executeUpdate();
        }
    }
}
