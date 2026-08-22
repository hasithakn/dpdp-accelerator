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

package org.wso2.dpdp.accelerator.event.notifications.dao.queries;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper builder for constructing dynamic topic search and count queries. Mirrors
 * {@link EventQueryBuilder}/{@link SubscriptionQueryBuilder} but is scoped to the TOPIC table.
 */
public class TopicQueryBuilder {

    private final String orgId;
    private String status;
    private String search;
    private String sort;

    public TopicQueryBuilder(String orgId) {
        this.orgId = orgId;
    }

    public TopicQueryBuilder setStatus(String status) {
        this.status = status;
        return this;
    }

    public TopicQueryBuilder setSearch(String search) {
        this.search = search;
        return this;
    }

    public TopicQueryBuilder setSort(String sort) {
        this.sort = sort;
        return this;
    }

    public String resolveSortColumn() {
        if ("-name".equalsIgnoreCase(sort)) {
            return "NAME DESC";
        } else if ("status".equalsIgnoreCase(sort)) {
            return "STATUS ASC";
        } else if ("-status".equalsIgnoreCase(sort)) {
            return "STATUS DESC";
        } else {
            return "NAME ASC";
        }
    }

    public QueryResult buildSelectQuery(String paginationClause) {
        StringBuilder sql = new StringBuilder(
                "SELECT TOPIC_ID, ORG_ID, NAME, DESCRIPTION, STATUS, INITIATED_BY FROM TOPIC WHERE ORG_ID = ?");
        List<Object> params = buildWhereClauseAndParams(sql);
        if (paginationClause != null && !paginationClause.trim().isEmpty()) {
            sql.append(paginationClause);
        }
        return new QueryResult(sql.toString(), params);
    }

    public QueryResult buildCountQuery() {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM TOPIC WHERE ORG_ID = ?");
        List<Object> params = buildWhereClauseAndParams(sql);
        return new QueryResult(sql.toString(), params);
    }

    private List<Object> buildWhereClauseAndParams(StringBuilder sql) {
        List<Object> params = new ArrayList<>();
        params.add(orgId);

        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND LOWER(STATUS) = LOWER(?)");
            params.add(status.trim());
        }

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (LOWER(TOPIC_ID) LIKE ? OR LOWER(NAME) LIKE ? OR LOWER(DESCRIPTION) LIKE ?)");
            String term = "%" + SubscriptionQueryBuilder.escapeLikePattern(search.trim()).toLowerCase() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
        }
        return params;
    }
}
