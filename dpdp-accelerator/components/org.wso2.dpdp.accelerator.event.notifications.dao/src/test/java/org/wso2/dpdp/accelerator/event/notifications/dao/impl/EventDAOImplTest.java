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
import org.wso2.dpdp.accelerator.event.notifications.dao.model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Minimal real-SQL coverage for {@link EventDAOImpl} against an in-memory H2 schema. Verifies
 * the write path (the only path this DAO exposes a {@code Connection} overload for) by reading
 * the rows back with plain JDBC, since the read methods only have the no-{@code Connection}
 * overloads that need a live JNDI datasource.
 */
public class EventDAOImplTest {

    private Connection conn;
    private final EventDAOImpl dao = new EventDAOImpl();

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
    public void addEventPersistsAllFields() throws Exception {

        Event event = new Event("event-1", "org-1", "group-1", "topic-1", "{\"amount\":100}",
                new Timestamp(System.currentTimeMillis()));

        boolean added = dao.addEvent(conn, event);

        assertTrue(added);
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT ORG_ID, GROUP_ID, TOPIC_ID, PAYLOAD FROM EVENT WHERE EVENT_ID = ?")) {
            ps.setString(1, "event-1");
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(rs.getString("ORG_ID"), "org-1");
                assertEquals(rs.getString("GROUP_ID"), "group-1");
                assertEquals(rs.getString("TOPIC_ID"), "topic-1");
                assertEquals(rs.getString("PAYLOAD"), "{\"amount\":100}");
            }
        }
    }

    @Test
    public void addEventPurposesPersistsEveryPurpose() throws Exception {

        Event event = new Event("event-1", "org-1", "group-1", "topic-1", "{}",
                new Timestamp(System.currentTimeMillis()));
        dao.addEvent(conn, event);

        dao.addEventPurposes(conn, "event-1", Arrays.asList("marketing", "billing"));

        Set<String> purposes = new HashSet<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT PURPOSE_NAME FROM EVENT_PURPOSE WHERE EVENT_ID = ?")) {
            ps.setString(1, "event-1");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    purposes.add(rs.getString("PURPOSE_NAME"));
                }
            }
        }
        assertEquals(purposes, new HashSet<>(Arrays.asList("marketing", "billing")));
    }

    @Test
    public void addEventPurposesIsANoOpForAnEmptyList() throws Exception {

        Event event = new Event("event-1", "org-1", "group-1", "topic-1", "{}",
                new Timestamp(System.currentTimeMillis()));
        dao.addEvent(conn, event);

        dao.addEventPurposes(conn, "event-1", java.util.Collections.emptyList());

        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM EVENT_PURPOSE WHERE EVENT_ID = ?")) {
            ps.setString(1, "event-1");
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(rs.getInt(1), 0);
            }
        }
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
}
