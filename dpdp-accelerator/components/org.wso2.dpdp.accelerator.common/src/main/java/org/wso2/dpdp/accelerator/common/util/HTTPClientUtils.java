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

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Shared HTTP client utility providing SSRF-guarded outbound connections.
 *
 * <p>Named to match the WSO2 Open Banking accelerator's {@code HTTPClientUtils} precedent, but
 * intentionally not a port of it: OB's version wraps Apache {@code CloseableHttpClient} with
 * TLS/keystore/hostname-verifier configuration for calling a fixed set of pre-registered banking
 * integration endpoints - it has no target-URL validation at all, since those endpoints are
 * operator-configured, not attacker-influenced. This accelerator's outbound calls (webhook
 * deliveries, subscription verification) go to a URL a Data Fiduciary API caller supplies at
 * request time, so the risk profile is different: the client itself can stay a plain
 * {@link HttpClient} with default trust, but every target URL must be validated against SSRF
 * before use.</p>
 */
public final class HTTPClientUtils {

    private static volatile HttpClient httpClient;
    private static final Set<Integer> ALLOWED_PORTS = new HashSet<>(Arrays.asList(-1, 80, 443, 8443));

    private HTTPClientUtils() {
    }

    public static HttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (HTTPClientUtils.class) {
                if (httpClient == null) {
                    httpClient = HttpClient.newBuilder()
                            .connectTimeout(Duration.ofSeconds(10))
                            .followRedirects(HttpClient.Redirect.NEVER)
                            .build();
                }
            }
        }
        return httpClient;
    }

    /**
     * Validates that a target URL scheme, port, and IP host do not violate SSRF constraints.
     */
    public static void validateUrl(String urlString) throws IllegalArgumentException, UnknownHostException {
        if (urlString == null || urlString.trim().isEmpty()) {
            throw new IllegalArgumentException("URL string cannot be empty.");
        }

        URI uri = URI.create(urlString.trim());
        String scheme = uri.getScheme();
        if (scheme == null || (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme))) {
            throw new IllegalArgumentException("Only http and https URL schemes are permitted.");
        }

        int port = uri.getPort();
        if (!ALLOWED_PORTS.contains(port)) {
            throw new IllegalArgumentException("Destination port [" + port + "] is not in the allowed list (80, 443, 8443).");
        }

        String host = uri.getHost();
        if (host == null || host.trim().isEmpty()) {
            throw new IllegalArgumentException("Target URL host cannot be empty.");
        }

        InetAddress[] addresses = InetAddress.getAllByName(host.trim());
        if (addresses == null || addresses.length == 0) {
            throw new UnknownHostException("Unable to resolve host: " + host);
        }

        for (InetAddress addr : addresses) {
            if (addr.isLoopbackAddress() || addr.isSiteLocalAddress()
                    || addr.isLinkLocalAddress() || addr.isAnyLocalAddress()
                    || addr.isMulticastAddress() || isIPv6UniqueLocalAddress(addr)) {
                throw new IllegalArgumentException("Target IP [" + addr.getHostAddress() + "] is in a restricted range.");
            }
        }
    }

    /**
     * {@link InetAddress#isSiteLocalAddress()} only recognizes the deprecated IPv6 site-local
     * range ({@code fec0::/10}), not the modern Unique Local Address range actually used by
     * private IPv6 networks today ({@code fc00::/7}, RFC 4193) - check that separately.
     */
    private static boolean isIPv6UniqueLocalAddress(InetAddress addr) {
        if (!(addr instanceof Inet6Address)) {
            return false;
        }
        byte firstByte = addr.getAddress()[0];
        return (firstByte & 0xFE) == 0xFC;
    }
}
