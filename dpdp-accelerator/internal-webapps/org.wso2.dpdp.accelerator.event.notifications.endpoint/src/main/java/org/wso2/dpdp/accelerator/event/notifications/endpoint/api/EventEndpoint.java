/**
 * Copyright (c) 2026, WSO2 LLC. (https://www.wso2.com).
 * <p>
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.dpdp.accelerator.event.notifications.endpoint.api;

import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.EventCreateDTO;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.EventDTO;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.PaginatedEventResult;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.PaginatedSubscriptionDeliveryResult;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.SubscriptionEventHistoryDTO;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.handler.EventHandler;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.util.EventNotificationDtoMapper;
import org.wso2.dpdp.accelerator.event.notifications.common.constants.EventNotificationCommonConstants;
import org.wso2.dpdp.accelerator.common.util.DPDPTenantContext;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.function.Supplier;

/**
 * JAX-RS endpoint for event publication, delivery listing, and delivery audit history.
 */
@Path("/events")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EventEndpoint {

    private final EventHandler eventHandler;
    private final Supplier<String> organizationIdResolver;

    public EventEndpoint() {
        this.eventHandler = new EventHandler();
        this.organizationIdResolver = DPDPTenantContext::getOrganizationId;
    }

    public EventEndpoint(EventHandler eventHandler) {
        this(eventHandler, DPDPTenantContext::getOrganizationId);
    }

    EventEndpoint(EventHandler eventHandler, Supplier<String> organizationIdResolver) {
        this.eventHandler = eventHandler;
        this.organizationIdResolver = organizationIdResolver;
    }

    @POST
    public Response publishEvent(
            @HeaderParam(EventNotificationCommonConstants.GROUP_ID_HEADER) String groupId,
            EventCreateDTO request) {
        EventDTO dto = EventNotificationDtoMapper.toDto(
                eventHandler.publishEvent(organizationIdResolver.get(), groupId,
                        EventNotificationDtoMapper.toServiceDto(request)));
        return Response.status(Response.Status.CREATED).entity(dto).build();
    }

    // TODO: subscriptionId is accepted here but never actually used as a filter -
    // EventHandler.searchEvents's 4th positional parameter is groupId, not subscriptionId, so
    // this currently passes orgId through in its place. Needs review: either wire a real
    // subscription-based filter through to the DAO, or drop this query param.
    @GET
    public Response listEvents(
            @QueryParam("topic") String topic,
            @QueryParam("status") String status,
            @QueryParam("subscriptionId") String subscriptionId,
            @QueryParam("purposes") String purposes,
            @QueryParam("search") String search,
            @QueryParam("limit") @DefaultValue(EventNotificationCommonConstants.DEFAULT_LIMIT_STR) int limit,
            @QueryParam("offset") @DefaultValue(EventNotificationCommonConstants.DEFAULT_OFFSET_STR) int offset) {
        String orgId = organizationIdResolver.get();
        PaginatedEventResult result = EventNotificationDtoMapper.toEventResultDto(
                eventHandler.searchEvents(orgId, topic, status, orgId, purposes, search, limit, offset));
        return Response.ok(result).build();
    }

    @GET
    @Path("/{deliveryId}/history")
    public Response getDeliveryHistory(
            @PathParam("deliveryId") String deliveryId) {
        SubscriptionEventHistoryDTO dto = EventNotificationDtoMapper.toDto(
                eventHandler.getDeliveryHistory(organizationIdResolver.get(), deliveryId));
        return Response.ok(dto).build();
    }

    @GET
    @Path("/{eventId}")
    public Response getEvent(
            @PathParam("eventId") String eventId) {
        EventDTO dto = EventNotificationDtoMapper.toDto(
                eventHandler.getEventById(organizationIdResolver.get(), eventId));
        return Response.ok(dto).build();
    }

    @GET
    @Path("/{eventId}/deliveries")
    public Response getEventDeliveries(
            @PathParam("eventId") String eventId,
            @QueryParam("limit") @DefaultValue(EventNotificationCommonConstants.DEFAULT_LIMIT_STR) int limit,
            @QueryParam("offset") @DefaultValue(EventNotificationCommonConstants.DEFAULT_OFFSET_STR) int offset) {
        PaginatedSubscriptionDeliveryResult result = EventNotificationDtoMapper.toDeliveryResultDto(
                eventHandler.getEventDeliveries(organizationIdResolver.get(), eventId, limit, offset));
        return Response.ok(result).build();
    }
}
