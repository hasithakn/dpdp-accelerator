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
import org.wso2.dpdp.accelerator.event.notifications.common.enums.TopicStatus;
import org.wso2.dpdp.accelerator.event.notifications.dao.model.Topic;

import java.sql.Connection;
import java.util.Optional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Minimal real-SQL coverage for {@link TopicDAOImpl} against an in-memory H2 schema. Not
 * exhaustive - just enough to exercise the connection-scoped read path for real; more cases
 * can be added incrementally.
 */
public class TopicDAOImplTest {

    private Connection conn;
    private final TopicDAOImpl dao = new TopicDAOImpl();

    @BeforeMethod
    public void setUp() throws Exception {

        conn = DaoTestSchema.newConnection();
    }

    @AfterMethod
    public void tearDown() throws Exception {

        if (conn != null) {
            conn.close();
        }
    }

    @Test
    public void getTopicByOrgAndNameFindsAnActiveTopic() throws Exception {

        insertTopic(conn, "topic-1", "org-1", "billing", TopicStatus.ACTIVE.getValue());

        Optional<Topic> found = dao.getTopicByOrgAndName(conn, "org-1", "billing");

        assertTrue(found.isPresent());
        assertEquals(found.get().getTopicId(), "topic-1");
        assertEquals(found.get().getOrgId(), "org-1");
        assertEquals(found.get().getStatus(), TopicStatus.ACTIVE.getValue());
    }

    @Test
    public void getTopicByOrgAndNameIsCaseInsensitiveOnName() throws Exception {

        insertTopic(conn, "topic-1", "org-1", "Billing", TopicStatus.ACTIVE.getValue());

        Optional<Topic> found = dao.getTopicByOrgAndName(conn, "org-1", "billing");

        assertTrue(found.isPresent());
    }

    @Test
    public void getTopicByOrgAndNameIgnoresDeregisteredTopics() throws Exception {

        insertTopic(conn, "topic-1", "org-1", "billing", TopicStatus.DEREGISTERED.getValue());

        Optional<Topic> found = dao.getTopicByOrgAndName(conn, "org-1", "billing");

        assertFalse(found.isPresent());
    }

    @Test
    public void getTopicByOrgAndNameReturnsEmptyForUnknownTopic() throws Exception {

        Optional<Topic> found = dao.getTopicByOrgAndName(conn, "org-1", "does-not-exist");

        assertFalse(found.isPresent());
    }

    private static void insertTopic(Connection conn, String topicId, String orgId, String name, String status)
            throws Exception {

        try (java.sql.PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO TOPIC (TOPIC_ID, ORG_ID, NAME, DESCRIPTION, STATUS, INITIATED_BY) "
                        + "VALUES (?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, topicId);
            ps.setString(2, orgId);
            ps.setString(3, name);
            ps.setString(4, "test topic");
            ps.setString(5, status);
            ps.setString(6, "USER");
            ps.executeUpdate();
        }
    }
}
