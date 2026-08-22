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

import org.h2.tools.RunScript;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Opens a fresh in-memory H2 database per test class and applies the real
 * {@code dbscripts/dpdp-accelerator/event-notification/h2.sql} schema (copied into
 * {@code src/test/resources} as a fixture - the production copy under {@code carbon-home} is
 * the actual source of truth) so DAO tests exercise real SQL rather than mocks.
 */
final class DaoTestSchema {

    private DaoTestSchema() {

    }

    static Connection newConnection() throws SQLException {

        // A random DB name per call keeps test classes isolated from each other even when run
        // in the same JVM/fork.
        String url = "jdbc:h2:mem:dao-test-" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1";
        Connection conn = DriverManager.getConnection(url, "sa", "");
        try (InputStream in = DaoTestSchema.class.getResourceAsStream("/h2.sql")) {
            if (in == null) {
                throw new IllegalStateException("Test fixture /h2.sql not found on the classpath.");
            }
            try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                RunScript.execute(conn, reader);
            }
        } catch (Exception e) {
            throw new SQLException("Failed to apply test schema.", e);
        }
        return conn;
    }
}
