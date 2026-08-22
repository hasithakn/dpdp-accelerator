package org.wso2.dpdp.accelerator.event.notifications.endpoint.dto;

import java.util.ArrayList;
import java.util.List;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.SubscriptionDeliveryAttemptDTO;

import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)

public class SubscriptionEventHistoryDTO  {
  
  @ApiModelProperty(value = "")
  private String deliveryId;

  @ApiModelProperty(value = "")
  private String eventId;

  @ApiModelProperty(value = "")
  private String topic;

  @ApiModelProperty(value = "")
  private String deliveryMode;

  @ApiModelProperty(value = "")
  private String currentStatus;

  @ApiModelProperty(value = "Epoch milliseconds.")
 /**
   * Epoch milliseconds.
  **/
  private Long occurredAt;

  @ApiModelProperty(value = "Epoch milliseconds. Absent once there are no more retries scheduled.")
 /**
   * Epoch milliseconds. Absent once there are no more retries scheduled.
  **/
  private Long nextRetryAt;

  @ApiModelProperty(value = "")
  private String completionStatus;

  @ApiModelProperty(value = "")
  private String completionEvidence;

  @ApiModelProperty(value = "")
  private List<SubscriptionDeliveryAttemptDTO> history;
 /**
   * Get deliveryId
   * @return deliveryId
  **/
  @JsonProperty("deliveryId")
  public String getDeliveryId() {
    return deliveryId;
  }

  public void setDeliveryId(String deliveryId) {
    this.deliveryId = deliveryId;
  }

  public SubscriptionEventHistoryDTO deliveryId(String deliveryId) {
    this.deliveryId = deliveryId;
    return this;
  }

 /**
   * Get eventId
   * @return eventId
  **/
  @JsonProperty("eventId")
  public String getEventId() {
    return eventId;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  public SubscriptionEventHistoryDTO eventId(String eventId) {
    this.eventId = eventId;
    return this;
  }

 /**
   * Get topic
   * @return topic
  **/
  @JsonProperty("topic")
  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public SubscriptionEventHistoryDTO topic(String topic) {
    this.topic = topic;
    return this;
  }

 /**
   * Get deliveryMode
   * @return deliveryMode
  **/
  @JsonProperty("deliveryMode")
  public String getDeliveryMode() {
    return deliveryMode;
  }

  public void setDeliveryMode(String deliveryMode) {
    this.deliveryMode = deliveryMode;
  }

  public SubscriptionEventHistoryDTO deliveryMode(String deliveryMode) {
    this.deliveryMode = deliveryMode;
    return this;
  }

 /**
   * Get currentStatus
   * @return currentStatus
  **/
  @JsonProperty("currentStatus")
  public String getCurrentStatus() {
    return currentStatus;
  }

  public void setCurrentStatus(String currentStatus) {
    this.currentStatus = currentStatus;
  }

  public SubscriptionEventHistoryDTO currentStatus(String currentStatus) {
    this.currentStatus = currentStatus;
    return this;
  }

 /**
   * Epoch milliseconds.
   * @return occurredAt
  **/
  @JsonProperty("occurredAt")
  public Long getOccurredAt() {
    return occurredAt;
  }

  public void setOccurredAt(Long occurredAt) {
    this.occurredAt = occurredAt;
  }

  public SubscriptionEventHistoryDTO occurredAt(Long occurredAt) {
    this.occurredAt = occurredAt;
    return this;
  }

 /**
   * Epoch milliseconds. Absent once there are no more retries scheduled.
   * @return nextRetryAt
  **/
  @JsonProperty("nextRetryAt")
  public Long getNextRetryAt() {
    return nextRetryAt;
  }

  public void setNextRetryAt(Long nextRetryAt) {
    this.nextRetryAt = nextRetryAt;
  }

  public SubscriptionEventHistoryDTO nextRetryAt(Long nextRetryAt) {
    this.nextRetryAt = nextRetryAt;
    return this;
  }

 /**
   * Get completionStatus
   * @return completionStatus
  **/
  @JsonProperty("completionStatus")
  public String getCompletionStatus() {
    return completionStatus;
  }

  public void setCompletionStatus(String completionStatus) {
    this.completionStatus = completionStatus;
  }

  public SubscriptionEventHistoryDTO completionStatus(String completionStatus) {
    this.completionStatus = completionStatus;
    return this;
  }

 /**
   * Get completionEvidence
   * @return completionEvidence
  **/
  @JsonProperty("completionEvidence")
  public String getCompletionEvidence() {
    return completionEvidence;
  }

  public void setCompletionEvidence(String completionEvidence) {
    this.completionEvidence = completionEvidence;
  }

  public SubscriptionEventHistoryDTO completionEvidence(String completionEvidence) {
    this.completionEvidence = completionEvidence;
    return this;
  }

 /**
   * Get history
   * @return history
  **/
  @JsonProperty("history")
  public List<SubscriptionDeliveryAttemptDTO> getHistory() {
    return history;
  }

  public void setHistory(List<SubscriptionDeliveryAttemptDTO> history) {
    this.history = history;
  }

  public SubscriptionEventHistoryDTO history(List<SubscriptionDeliveryAttemptDTO> history) {
    this.history = history;
    return this;
  }

  public SubscriptionEventHistoryDTO addHistoryItem(SubscriptionDeliveryAttemptDTO historyItem) {
    this.history.add(historyItem);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubscriptionEventHistoryDTO subscriptionEventHistoryDTO = (SubscriptionEventHistoryDTO) o;
    return Objects.equals(deliveryId, subscriptionEventHistoryDTO.deliveryId) &&
        Objects.equals(eventId, subscriptionEventHistoryDTO.eventId) &&
        Objects.equals(topic, subscriptionEventHistoryDTO.topic) &&
        Objects.equals(deliveryMode, subscriptionEventHistoryDTO.deliveryMode) &&
        Objects.equals(currentStatus, subscriptionEventHistoryDTO.currentStatus) &&
        Objects.equals(occurredAt, subscriptionEventHistoryDTO.occurredAt) &&
        Objects.equals(nextRetryAt, subscriptionEventHistoryDTO.nextRetryAt) &&
        Objects.equals(completionStatus, subscriptionEventHistoryDTO.completionStatus) &&
        Objects.equals(completionEvidence, subscriptionEventHistoryDTO.completionEvidence) &&
        Objects.equals(history, subscriptionEventHistoryDTO.history);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deliveryId, eventId, topic, deliveryMode, currentStatus, occurredAt, nextRetryAt, completionStatus, completionEvidence, history);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubscriptionEventHistoryDTO {\n");
    
    sb.append("    deliveryId: ").append(toIndentedString(deliveryId)).append("\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    topic: ").append(toIndentedString(topic)).append("\n");
    sb.append("    deliveryMode: ").append(toIndentedString(deliveryMode)).append("\n");
    sb.append("    currentStatus: ").append(toIndentedString(currentStatus)).append("\n");
    sb.append("    occurredAt: ").append(toIndentedString(occurredAt)).append("\n");
    sb.append("    nextRetryAt: ").append(toIndentedString(nextRetryAt)).append("\n");
    sb.append("    completionStatus: ").append(toIndentedString(completionStatus)).append("\n");
    sb.append("    completionEvidence: ").append(toIndentedString(completionEvidence)).append("\n");
    sb.append("    history: ").append(toIndentedString(history)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private static String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

