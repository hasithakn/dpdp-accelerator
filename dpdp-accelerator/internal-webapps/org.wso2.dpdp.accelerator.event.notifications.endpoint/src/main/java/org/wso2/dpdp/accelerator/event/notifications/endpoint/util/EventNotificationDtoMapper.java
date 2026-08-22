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

package org.wso2.dpdp.accelerator.event.notifications.endpoint.util;

import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.DeliveryConfigDTO;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.DeliveryMode;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.EventCreateDTO;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.EventDTO;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.FilterDTO;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.Initiator;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.PaginatedEventResult;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.PaginatedSubscriptionDeliveryResult;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.PaginatedSubscriptionResult;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.PaginatedTopicResult;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.PurposeFilterMode;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.SubscriptionDTO;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.SubscriptionDeliveryAttemptDTO;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.SubscriptionDeliveryDTO;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.SubscriptionEventHistoryDTO;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.SubscriptionStatus;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.TopicDTO;
import org.wso2.dpdp.accelerator.event.notifications.service.model.PaginatedResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Converts between the service layer's own DTOs and the OpenAPI-generated response/request types
 * in {@code endpoint.dto}. Both sides use identical field shapes by design - this class exists
 * only because the two packages declare same-named classes that Java can't otherwise
 * disambiguate, and because the generated enum constant names don't always match the service
 * enums' names (e.g. generated {@code ALL_EXCEPT} vs service {@code EXCEPT}), so conversion goes
 * through the shared JSON value string rather than {@code Enum.name()}.
 */
public final class EventNotificationDtoMapper {

    private EventNotificationDtoMapper() {

    }

    public static TopicDTO toDto(org.wso2.dpdp.accelerator.event.notifications.service.dto.TopicDTO topic) {

        if (topic == null) {
            return null;
        }
        return new TopicDTO()
                .topicId(topic.getTopicId())
                .name(topic.getName())
                .description(topic.getDescription())
                .status(topic.getStatus())
                .initiatedBy(topic.getInitiatedBy() == null ? null : Initiator.fromValue(topic.getInitiatedBy()));
    }

    public static org.wso2.dpdp.accelerator.event.notifications.service.dto.TopicDTO toServiceDto(TopicDTO topic) {

        if (topic == null) {
            return null;
        }
        org.wso2.dpdp.accelerator.event.notifications.service.dto.TopicDTO serviceDto =
                new org.wso2.dpdp.accelerator.event.notifications.service.dto.TopicDTO(
                        topic.getTopicId(), topic.getName(), topic.getDescription(), topic.getStatus());
        if (topic.getInitiatedBy() != null) {
            serviceDto.setInitiatedBy(topic.getInitiatedBy().toString());
        }
        return serviceDto;
    }

    public static SubscriptionDTO toDto(org.wso2.dpdp.accelerator.event.notifications.service.dto.SubscriptionDTO sub) {

        if (sub == null) {
            return null;
        }
        return new SubscriptionDTO()
                .subscriptionId(sub.getSubscriptionId())
                .orgId(sub.getOrgId())
                .groupId(sub.getGroupId())
                .topic(sub.getTopic())
                .filter(toDto(sub.getFilter()))
                .delivery(toDto(sub.getDelivery()))
                .status(sub.getStatus() == null ? null : SubscriptionStatus.fromValue(sub.getStatus().getValue()))
                .createdAt(sub.getCreatedAt())
                .updatedAt(sub.getUpdatedAt())
                .alreadyExists(sub.getAlreadyExists())
                .message(sub.getMessage());
    }

    public static org.wso2.dpdp.accelerator.event.notifications.service.dto.SubscriptionDTO toServiceDto(
            SubscriptionDTO sub) {

        if (sub == null) {
            return null;
        }
        org.wso2.dpdp.accelerator.event.notifications.service.dto.SubscriptionDTO serviceDto =
                new org.wso2.dpdp.accelerator.event.notifications.service.dto.SubscriptionDTO();
        serviceDto.setSubscriptionId(sub.getSubscriptionId());
        serviceDto.setOrgId(sub.getOrgId());
        serviceDto.setGroupId(sub.getGroupId());
        serviceDto.setTopic(sub.getTopic());
        serviceDto.setFilter(toServiceDto(sub.getFilter()));
        serviceDto.setDelivery(toServiceDto(sub.getDelivery()));
        if (sub.getStatus() != null) {
            serviceDto.setStatus(org.wso2.dpdp.accelerator.event.notifications.common.enums.SubscriptionStatus
                    .fromValue(sub.getStatus().toString()));
        }
        serviceDto.setCreatedAt(sub.getCreatedAt());
        serviceDto.setUpdatedAt(sub.getUpdatedAt());
        serviceDto.setAlreadyExists(sub.getAlreadyExists());
        serviceDto.setMessage(sub.getMessage());
        return serviceDto;
    }

    public static FilterDTO toDto(org.wso2.dpdp.accelerator.event.notifications.service.dto.FilterDTO filter) {

        if (filter == null) {
            return null;
        }
        return new FilterDTO()
                .type(filter.getType() == null ? null : PurposeFilterMode.fromValue(filter.getType().getValue()))
                .purposes(filter.getPurposes());
    }

    public static org.wso2.dpdp.accelerator.event.notifications.service.dto.FilterDTO toServiceDto(FilterDTO filter) {

        if (filter == null) {
            return null;
        }
        org.wso2.dpdp.accelerator.event.notifications.common.enums.PurposeFilterMode type = filter.getType() == null
                ? null
                : org.wso2.dpdp.accelerator.event.notifications.common.enums.PurposeFilterMode
                        .fromValue(filter.getType().toString());
        return new org.wso2.dpdp.accelerator.event.notifications.service.dto.FilterDTO(type, filter.getPurposes());
    }

    public static DeliveryConfigDTO toDto(
            org.wso2.dpdp.accelerator.event.notifications.service.dto.DeliveryConfigDTO delivery) {

        if (delivery == null) {
            return null;
        }
        return new DeliveryConfigDTO()
                .mode(delivery.getMode() == null ? null : DeliveryMode.fromValue(delivery.getMode().getValue()))
                .callbackUrl(delivery.getCallbackUrl())
                .sharedSecret(delivery.getSharedSecret());
    }

    public static org.wso2.dpdp.accelerator.event.notifications.service.dto.DeliveryConfigDTO toServiceDto(
            DeliveryConfigDTO delivery) {

        if (delivery == null) {
            return null;
        }
        org.wso2.dpdp.accelerator.event.notifications.common.enums.DeliveryMode mode = delivery.getMode() == null
                ? null
                : org.wso2.dpdp.accelerator.event.notifications.common.enums.DeliveryMode
                        .fromValue(delivery.getMode().toString());
        return new org.wso2.dpdp.accelerator.event.notifications.service.dto.DeliveryConfigDTO(
                mode, delivery.getCallbackUrl(), delivery.getSharedSecret());
    }

    @SuppressWarnings("unchecked")
    public static org.wso2.dpdp.accelerator.event.notifications.service.dto.EventCreateDTO toServiceDto(
            EventCreateDTO event) {

        if (event == null) {
            return null;
        }
        return new org.wso2.dpdp.accelerator.event.notifications.service.dto.EventCreateDTO(
                event.getTopicName(), event.getPurposes(), (java.util.Map<String, Object>) event.getPayload());
    }

    public static EventDTO toDto(org.wso2.dpdp.accelerator.event.notifications.service.dto.EventDTO event) {

        if (event == null) {
            return null;
        }
        return new EventDTO()
                .eventId(event.getEventId())
                .orgId(event.getOrgId())
                .groupId(event.getGroupId())
                .topicId(event.getTopicId())
                .payload(event.getPayload())
                .purposes(event.getPurposes())
                .occurredAt(event.getOccurredAt() == null ? null : event.getOccurredAt().getTime())
                .createdAt(event.getCreatedAt() == null ? null : event.getCreatedAt().getTime())
                .topic(event.getTopic())
                .deliveriesCount(event.getDeliveriesCount());
    }

    public static SubscriptionDeliveryDTO toDto(
            org.wso2.dpdp.accelerator.event.notifications.service.dto.SubscriptionDeliveryDTO delivery) {

        if (delivery == null) {
            return null;
        }
        return new SubscriptionDeliveryDTO()
                .deliveryId(delivery.getDeliveryId())
                .eventId(delivery.getEventId())
                .subscriptionId(delivery.getSubscriptionId())
                .groupId(delivery.getGroupId())
                .topic(delivery.getTopic())
                .currentStatus(delivery.getCurrentStatus())
                .deliveryMode(delivery.getDeliveryMode())
                .occurredAt(delivery.getOccurredAt());
    }

    public static SubscriptionDeliveryAttemptDTO toDto(
            org.wso2.dpdp.accelerator.event.notifications.service.dto.SubscriptionDeliveryAttemptDTO attempt) {

        if (attempt == null) {
            return null;
        }
        return new SubscriptionDeliveryAttemptDTO()
                .attempt(attempt.getAttempt())
                .status(attempt.getStatus())
                .timestamp(attempt.getTimestamp())
                .httpStatus(attempt.getHttpStatus())
                .error(attempt.getError());
    }

    public static SubscriptionEventHistoryDTO toDto(
            org.wso2.dpdp.accelerator.event.notifications.service.dto.SubscriptionEventHistoryDTO history) {

        if (history == null) {
            return null;
        }
        List<SubscriptionDeliveryAttemptDTO> attempts = history.getHistory() == null
                ? null
                : history.getHistory().stream().map(EventNotificationDtoMapper::toDto).collect(Collectors.toList());
        return new SubscriptionEventHistoryDTO()
                .deliveryId(history.getDeliveryId())
                .eventId(history.getEventId())
                .topic(history.getTopic())
                .deliveryMode(history.getDeliveryMode())
                .currentStatus(history.getCurrentStatus())
                .occurredAt(history.getOccurredAt())
                .nextRetryAt(history.getNextRetryAt())
                .completionStatus(history.getCompletionStatus())
                .completionEvidence(history.getCompletionEvidence())
                .history(attempts);
    }

    public static PaginatedTopicResult toDto(
            PaginatedResult<org.wso2.dpdp.accelerator.event.notifications.service.dto.TopicDTO> result) {

        return new PaginatedTopicResult()
                .items(result.getItems().stream().map(EventNotificationDtoMapper::toDto).collect(Collectors.toList()))
                .total(result.getTotal());
    }

    public static PaginatedSubscriptionResult toSubscriptionResultDto(
            PaginatedResult<org.wso2.dpdp.accelerator.event.notifications.service.dto.SubscriptionDTO> result) {

        return new PaginatedSubscriptionResult()
                .items(result.getItems().stream().map(EventNotificationDtoMapper::toDto).collect(Collectors.toList()))
                .total(result.getTotal());
    }

    public static PaginatedEventResult toEventResultDto(
            PaginatedResult<org.wso2.dpdp.accelerator.event.notifications.service.dto.EventDTO> result) {

        return new PaginatedEventResult()
                .items(result.getItems().stream().map(EventNotificationDtoMapper::toDto).collect(Collectors.toList()))
                .total(result.getTotal());
    }

    public static PaginatedSubscriptionDeliveryResult toDeliveryResultDto(
            PaginatedResult<org.wso2.dpdp.accelerator.event.notifications.service.dto.SubscriptionDeliveryDTO> result) {

        return new PaginatedSubscriptionDeliveryResult()
                .items(result.getItems().stream().map(EventNotificationDtoMapper::toDto).collect(Collectors.toList()))
                .total(result.getTotal());
    }
}
