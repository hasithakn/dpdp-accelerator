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

package org.wso2.dpdp.accelerator.common.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.Collections;

public class DPDPConfigurationServiceImpl implements DPDPConfigurationService {

    private static final Log LOG = LogFactory.getLog(DPDPConfigurationServiceImpl.class);
    private final DPDPConfigParser configParser;

    public DPDPConfigurationServiceImpl() {
        this(true);
    }

    public DPDPConfigurationServiceImpl(boolean loadConfiguration) {
        DPDPConfigParser parser;
        if (!loadConfiguration) {
            parser = null;
        } else {
            try {
                parser = DPDPConfigParser.getInstance();
            } catch (RuntimeException e) {
                LOG.debug("DPDP accelerator configuration is unavailable.", e);
                parser = null;
            }
        }
        this.configParser = parser;
    }

    @Override
    public Map<String, Object> getConfigurations() {

        return configParser == null ? Collections.emptyMap() : configParser.getConfiguration();
    }

    @Override
    public boolean isConsentPortalProvisioningEnabled() {

        return configParser == null || configParser.isConsentPortalProvisioningEnabled();
    }

    @Override
    public String getConsentPortalClientId() {

        return configParser == null ? "DPDP_CONSENT_PORTAL" : configParser.getConsentPortalClientId();
    }

    @Override
    public int getEventNotificationThreadPoolSize() {

        return configParser == null ? 4 : configParser.getEventNotificationThreadPoolSize();
    }

    @Override
    public long getEventNotificationBaseBackoffSeconds() {

        return configParser == null ? 5L : configParser.getEventNotificationBaseBackoffSeconds();
    }

    @Override
    public int getEventNotificationMaxRetries() {

        return configParser == null ? 5 : configParser.getEventNotificationMaxRetries();
    }

    @Override
    public boolean isEventNotificationHttpCallbackUrlAllowed() {

        return configParser == null || configParser.isEventNotificationHttpCallbackUrlAllowed();
    }

    @Override
    public int getEventNotificationDeliveryWorkerBatchSize() {

        return configParser == null ? 50 : configParser.getEventNotificationDeliveryWorkerBatchSize();
    }

    @Override
    public int getEventNotificationDeliveryWorkerPollSeconds() {

        return configParser == null ? 5 : configParser.getEventNotificationDeliveryWorkerPollSeconds();
    }

    @Override
    public int getEventNotificationStuckInFlightThresholdSeconds() {

        return configParser == null ? 10 : configParser.getEventNotificationStuckInFlightThresholdSeconds();
    }

    @Override
    public int getEventNotificationMaxVerificationResponseBodyBytes() {

        return configParser == null ? 4096 : configParser.getEventNotificationMaxVerificationResponseBodyBytes();
    }

    @Override
    public int getEventNotificationPendingSubscriptionRecoveryThresholdSeconds() {

        return configParser == null ? 60
                : configParser.getEventNotificationPendingSubscriptionRecoveryThresholdSeconds();
    }
}
