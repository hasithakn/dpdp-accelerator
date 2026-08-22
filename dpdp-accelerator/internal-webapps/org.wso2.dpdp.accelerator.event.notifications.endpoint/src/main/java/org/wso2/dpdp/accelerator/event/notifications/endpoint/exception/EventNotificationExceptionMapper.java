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

package org.wso2.dpdp.accelerator.event.notifications.endpoint.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.error.EventNotificationEndpointErrorCodes;
import org.wso2.dpdp.accelerator.event.notifications.service.exception.EventNotificationException;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

@Provider
public class EventNotificationExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Log LOG = LogFactory.getLog(EventNotificationExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {

        if (exception instanceof EventNotificationException) {
            return handleEventNotificationException((EventNotificationException) exception);
        }

        Throwable rootCause = unwrap(exception);

        if (rootCause instanceof EventNotificationException) {
            return handleEventNotificationException((EventNotificationException) rootCause);
        }

        if (rootCause instanceof WebApplicationException) {
            return handleWebApplicationException((WebApplicationException) rootCause);
        }

        if (rootCause instanceof ConstraintViolationException) {
            return handleConstraintViolation((ConstraintViolationException) rootCause);
        }

        if (rootCause instanceof JsonProcessingException) {
            return handleJsonProcessingException((JsonProcessingException) rootCause);
        }

        if (rootCause instanceof IllegalArgumentException) {
            LOG.debug("Invalid request argument.", rootCause);
            return buildResponse(Response.Status.BAD_REQUEST.getStatusCode(),
                    EventNotificationEndpointErrorCodes.ERROR_CODE_INVALID_PARAMETER, "Invalid request parameter",
                    null);
        }

        LOG.error("Unhandled exception in Event Notification endpoint", exception);
        return buildResponse(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                EventNotificationEndpointErrorCodes.ERROR_CODE_INTERNAL_ERROR, "Internal server error",
                "An unexpected error occurred.");
    }

    private Response handleEventNotificationException(EventNotificationException ex) {
        if (ex.getStatusCode() >= 500) {
            LOG.error("Service error [" + sanitize(ex.getCode()) + "]: " + sanitize(ex.getMessage()), ex);
        } else {
            LOG.debug("Service error [" + sanitize(ex.getCode()) + "]: " + sanitize(ex.getMessage()), ex);
        }
        return buildResponse(ex.getStatusCode(), ex.getCode(), ex.getMessage(), ex.getDescription());
    }

    private Response handleWebApplicationException(WebApplicationException wae) {
        int status = wae.getResponse().getStatus();
        LOG.debug("JAX-RS exception [" + status + "]: " + sanitize(wae.getMessage()), wae);
        return buildResponse(status, EventNotificationEndpointErrorCodes.ERROR_CODE_FRAMEWORK_ERROR,
                wae.getMessage() != null ? wae.getMessage() : Response.Status.fromStatusCode(status).getReasonPhrase(),
                null);
    }

    private Response handleConstraintViolation(ConstraintViolationException cve) {
        String detail = cve.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + " " + v.getMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse(cve.getMessage());

        LOG.debug("Validation failure: " + sanitize(detail), cve);
        return buildResponse(Response.Status.BAD_REQUEST.getStatusCode(),
                EventNotificationEndpointErrorCodes.ERROR_CODE_VALIDATION_FAILED, "Request failed validation",
                detail);
    }

    private Response handleJsonProcessingException(JsonProcessingException jpe) {
        String detail = jpe.getOriginalMessage();
        if (jpe instanceof UnrecognizedPropertyException) {
            UnrecognizedPropertyException upe = (UnrecognizedPropertyException) jpe;
            detail = "Unrecognized field '" + upe.getPropertyName() + "' in request payload.";
        }

        LOG.debug("Malformed request payload: " + sanitize(detail), jpe);
        return buildResponse(Response.Status.BAD_REQUEST.getStatusCode(),
                EventNotificationEndpointErrorCodes.ERROR_CODE_MALFORMED_PAYLOAD, "Malformed request payload",
                detail);
    }

    private Response buildResponse(int status, String code, String message, String description) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", code);
        body.put("message", message);
        if (description != null) {
            body.put("description", description);
        }

        return Response.status(status > 0 ? status : 500)
                .type(MediaType.APPLICATION_JSON)
                .entity(body)
                .build();
    }

    private Throwable unwrap(Throwable exception) {
        Throwable current = exception;
        Set<Throwable> visited = Collections.newSetFromMap(new IdentityHashMap<>());

        while (current != null) {
            if (!visited.add(current)) {
                break; // Cycle detected
            }
            if (current instanceof EventNotificationException) {
                return current;
            }
            if (current.getCause() == null || current.getCause() == current) {
                break;
            }
            current = current.getCause();
        }
        return current != null ? current : exception;
    }

    private static String sanitize(String value) {

        return value == null ? null : value.replaceAll("[\r\n]", "");
    }
}
