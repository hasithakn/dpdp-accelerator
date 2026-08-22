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

package org.wso2.dpdp.accelerator.common.util;

import org.testng.annotations.Test;

import java.net.http.HttpClient;

import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertThrows;

public class HTTPClientUtilsTest {

    @Test
    public void getHttpClientReturnsTheSameSingletonInstance() {

        HttpClient first = HTTPClientUtils.getHttpClient();
        HttpClient second = HTTPClientUtils.getHttpClient();

        assertSame(first, second);
    }

    @Test
    public void validateUrlRejectsEmptyUrl() {

        assertThrows(IllegalArgumentException.class, () -> HTTPClientUtils.validateUrl(""));
        assertThrows(IllegalArgumentException.class, () -> HTTPClientUtils.validateUrl(null));
    }

    @Test
    public void validateUrlRejectsNonHttpScheme() {

        assertThrows(IllegalArgumentException.class, () -> HTTPClientUtils.validateUrl("ftp://example.com"));
    }

    @Test
    public void validateUrlRejectsDisallowedPort() {

        assertThrows(IllegalArgumentException.class, () -> HTTPClientUtils.validateUrl("https://example.com:9999"));
    }

    @Test
    public void validateUrlRejectsLoopbackHost() {

        assertThrows(IllegalArgumentException.class, () -> HTTPClientUtils.validateUrl("http://127.0.0.1"));
    }

    @Test
    public void validateUrlRejectsSiteLocalHost() {

        assertThrows(IllegalArgumentException.class, () -> HTTPClientUtils.validateUrl("http://192.168.1.1"));
    }

    @Test
    public void validateUrlRejectsIPv6UniqueLocalHost() {

        // fc00::/7 - the modern IPv6 private-network range that isSiteLocalAddress() alone
        // does not cover (regression guard for the gap found in the pax-logging fix session).
        assertThrows(IllegalArgumentException.class, () -> HTTPClientUtils.validateUrl("http://[fd12:3456:789a::1]"));
    }

    @Test
    public void validateUrlAcceptsAPublicHttpsUrlOnAnAllowedPort() throws Exception {

        HTTPClientUtils.validateUrl("https://1.1.1.1:443/path");
    }
}
