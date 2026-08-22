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

package org.wso2.dpdp.accelerator.event.notifications.endpoint.error;

/**
 * Error codes owned by this webapp's {@code ExceptionMapper}, for failures that occur in the
 * JAX-RS/transport layer itself (malformed payload, bad query param, framework exceptions)
 * before a request ever reaches the service layer. Uses its own {@code EN-} prefix, distinct
 * from the service layer's {@code CS-*} codes in {@code EventNotificationServiceConstants} -
 * those are returned as-is via {@link
 * org.wso2.dpdp.accelerator.event.notifications.service.exception.EventNotificationException}
 * and never re-coded here.
 */
public final class EventNotificationEndpointErrorCodes {

    public static final String ERROR_CODE_MALFORMED_PAYLOAD = "EN-00001";
    public static final String ERROR_CODE_INVALID_PARAMETER = "EN-00002";
    public static final String ERROR_CODE_VALIDATION_FAILED = "EN-00003";
    public static final String ERROR_CODE_FRAMEWORK_ERROR = "EN-00004";
    public static final String ERROR_CODE_INTERNAL_ERROR = "EN-00005";

    private EventNotificationEndpointErrorCodes() {

    }
}
