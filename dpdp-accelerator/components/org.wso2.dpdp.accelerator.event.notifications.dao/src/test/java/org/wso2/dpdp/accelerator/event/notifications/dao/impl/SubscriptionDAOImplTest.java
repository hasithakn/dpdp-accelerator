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
import org.wso2.dpdp.accelerator.event.notifications.common.enums.DeliveryMode;
import org.wso2.dpdp.accelerator.event.notifications.common.enums.PurposeFilterMode;
import org.wso2.dpdp.accelerator.event.notifications.common.enums.SubscriptionStatus;
import org.wso2.dpdp.accelerator.event.notifications.dao.model.Subscription;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Minimal real-SQL coverage for {@link SubscriptionDAOImpl} against an in-memory H2 schema.
 */
public class SubscriptionDAOImplTest {

    private Connection conn;
    private final SubscriptionDAOImpl dao = new SubscriptionDAOImpl();

    @BeforeMethod
    public void setUp() throws Exception {

        conn = DaoTestSchema.newConnection();
        insertTopic(conn, "topic-1", "org-1", "billing");
    }

    @AfterMethod
    public void tearDown() throws Exception {

        if (conn != null) {
            conn.close();
        }
    }

    @Test
    public void getSubscriptionsByOrgAndTopicReturnsActiveSubscriptionsWithPurposes() throws Exception {

        insertSubscription(conn, "sub-1", "org-1", "group-1", "topic-1", SubscriptionStatus.ACTIVE.getValue());
        insertSubscriptionPurposes(conn, "sub-1", "marketing", "billing");

        List<Subscription> result = dao.getSubscriptionsByOrgAndTopic(conn, "org-1", "topic-1",
                SubscriptionStatus.ACTIVE.getValue());

        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getSubscriptionId(), "sub-1");
        assertTrue(result.get(0).getPurposes().containsAll(Arrays.asList("marketing", "billing")));
    }

    @Test
    public void getSubscriptionsByOrgAndTopicExcludesOtherStatuses() throws Exception {

        insertSubscription(conn, "sub-1", "org-1", "group-1", "topic-1", SubscriptionStatus.DELETED.getValue());

        List<Subscription> result = dao.getSubscriptionsByOrgAndTopic(conn, "org-1", "topic-1",
                SubscriptionStatus.ACTIVE.getValue());

        assertTrue(result.isEmpty());
    }

    @Test
    public void getLiveSubscriptionsByOrgAndTopicIncludesPendingAndStale() throws Exception {

        insertSubscription(conn, "sub-1", "org-1", "group-1", "topic-1", SubscriptionStatus.PENDING.getValue());
        insertSubscription(conn, "sub-2", "org-1", "group-1", "topic-1", SubscriptionStatus.STALE.getValue());
        insertSubscription(conn, "sub-3", "org-1", "group-1", "topic-1", SubscriptionStatus.DELETED.getValue());

        List<Subscription> result = dao.getLiveSubscriptionsByOrgAndTopic(conn, "org-1", "topic-1");

        assertEquals(result.size(), 2);
    }

    @Test
    public void getPurposesBySubscriptionIdsGroupsPurposesPerSubscription() throws Exception {

        insertSubscription(conn, "sub-1", "org-1", "group-1", "topic-1", SubscriptionStatus.ACTIVE.getValue());
        insertSubscription(conn, "sub-2", "org-1", "group-1", "topic-1", SubscriptionStatus.ACTIVE.getValue());
        insertSubscriptionPurposes(conn, "sub-1", "marketing");
        insertSubscriptionPurposes(conn, "sub-2", "billing", "analytics");

        Map<String, List<String>> result = dao.getPurposesBySubscriptionIds(conn, Arrays.asList("sub-1", "sub-2"));

        assertEquals(result.get("sub-1"), Arrays.asList("marketing"));
        assertTrue(result.get("sub-2").containsAll(Arrays.asList("billing", "analytics")));
    }

    private static void insertTopic(Connection conn, String topicId, String orgId, String name) throws Exception {

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO TOPIC (TOPIC_ID, ORG_ID, NAME, DESCRIPTION, STATUS, INITIATED_BY) "
                        + "VALUES (?, ?, ?, ?, 'active', 'USER')")) {
            ps.setString(1, topicId);
            ps.setString(2, orgId);
            ps.setString(3, name);
            ps.setString(4, "test topic");
            ps.executeUpdate();
        }
    }

    private static void insertSubscription(Connection conn, String subscriptionId, String orgId, String groupId,
            String topicId, String status) throws Exception {

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO SUBSCRIPTION (SUBSCRIPTION_ID, ORG_ID, GROUP_ID, TOPIC_ID, STATUS, "
                        + "PURPOSE_FILTER_MODE, DELIVERY_MODE, CALLBACK_URL, SHARED_SECRET) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, subscriptionId);
            ps.setString(2, orgId);
            ps.setString(3, groupId);
            ps.setString(4, topicId);
            ps.setString(5, status);
            ps.setString(6, PurposeFilterMode.ALL.getValue());
            ps.setString(7, DeliveryMode.WEBHOOK.getValue());
            ps.setString(8, "https://example.com/hook");
            ps.setString(9, "secret");
            ps.executeUpdate();
        }
    }

    private static void insertSubscriptionPurposes(Connection conn, String subscriptionId, String... purposes)
            throws Exception {

        for (String purpose : purposes) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO SUBSCRIPTION_PURPOSE (SUBSCRIPTION_ID, PURPOSE_NAME) VALUES (?, ?)")) {
                ps.setString(1, subscriptionId);
                ps.setString(2, purpose);
                ps.executeUpdate();
            }
        }
    }
}
