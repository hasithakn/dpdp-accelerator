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

package org.wso2.dpdp.accelerator.common.persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.dpdp.accelerator.common.config.DPDPConfigParser;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Resolves the single JNDI datasource shared by every dpdp-accelerator table, across all
 * features - not just event notifications. The datasource name is read once from
 * {@code dpdp-accelerator.xml}; mirrors WSO2 Open Banking's {@code JDBCPersistenceManager}.
 *
 * <p>A failed lookup is cached for {@link #JNDI_LOOKUP_COOLDOWN_MS} so a misconfigured or not-
 * yet-available datasource doesn't force a fresh JNDI round-trip on every single connection
 * request.</p>
 */
public final class JDBCPersistenceManager {

    private static final Log LOG = LogFactory.getLog(JDBCPersistenceManager.class);
    private static final long JNDI_LOOKUP_COOLDOWN_MS = 10000L;

    private static volatile DataSource dataSource;
    private static volatile long lastJndiLookupFailedTime = 0L;

    private JDBCPersistenceManager() {

    }

    public static Connection getConnection() throws SQLException {

        DataSource ds = getDataSource();
        if (ds == null) {
            throw new SQLException("The dpdp-accelerator datasource is unavailable.");
        }
        return ds.getConnection();
    }

    private static DataSource getDataSource() {

        if (dataSource == null) {
            long now = System.currentTimeMillis();
            if (now - lastJndiLookupFailedTime < JNDI_LOOKUP_COOLDOWN_MS) {
                return null;
            }
            synchronized (JDBCPersistenceManager.class) {
                if (dataSource == null) {
                    dataSource = lookupDataSource();
                    if (dataSource == null) {
                        lastJndiLookupFailedTime = System.currentTimeMillis();
                    }
                }
            }
        }
        return dataSource;
    }

    private static DataSource lookupDataSource() {

        String dataSourceName;
        try {
            dataSourceName = DPDPConfigParser.getInstance().getDatabaseDataSourceName();
            InitialContext ctx = new InitialContext();
            try {
                return (DataSource) ctx.lookup(dataSourceName);
            } catch (Exception directLookupFailure) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("JNDI lookup failed for [" + dataSourceName
                            + "], retrying under java:comp/env.", directLookupFailure);
                }
                return (DataSource) ctx.lookup("java:comp/env/" + dataSourceName);
            }
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Unable to resolve the dpdp-accelerator datasource.", e);
            }
            return null;
        }
    }
}
