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

package org.wso2.dpdp.accelerator.event.notifications.common.util;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class HmacSignerTest {

    @Test
    public void signIsDeterministicAndLowercaseHex() {

        String signature = HmacSigner.sign("secret", "payload");

        assertNotNull(signature);
        assertEquals(signature, HmacSigner.sign("secret", "payload"));
        assertEquals(signature, signature.toLowerCase());
        assertEquals(signature.length(), 64);
    }

    @Test
    public void differentSecretsProduceDifferentSignatures() {

        assertNotEquals(HmacSigner.sign("secret-a", "payload"), HmacSigner.sign("secret-b", "payload"));
    }

    @Test
    public void nullOrEmptySecretReturnsNull() {

        assertNull(HmacSigner.sign(null, "payload"));
        assertNull(HmacSigner.sign("", "payload"));
    }

    @Test
    public void nullPayloadReturnsNull() {

        assertNull(HmacSigner.sign("secret", null));
    }
}
