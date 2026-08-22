package org.wso2.dpdp.accelerator.event.notifications.endpoint.dto;


import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)

public class SubscriptionDeliveryDTO  {
  
  @ApiModelProperty(value = "")
  private String deliveryId;

  @ApiModelProperty(value = "")
  private String eventId;

  @ApiModelProperty(value = "")
  private String subscriptionId;

  @ApiModelProperty(value = "")
  private String groupId;

  @ApiModelProperty(value = "")
  private String topic;

  @ApiModelProperty(value = "")
  private String currentStatus;

  @ApiModelProperty(value = "")
  private String deliveryMode;

  @ApiModelProperty(value = "Epoch milliseconds.")
 /**
   * Epoch milliseconds.
  **/
  private Long occurredAt;
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

  public SubscriptionDeliveryDTO deliveryId(String deliveryId) {
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

  public SubscriptionDeliveryDTO eventId(String eventId) {
    this.eventId = eventId;
    return this;
  }

 /**
   * Get subscriptionId
   * @return subscriptionId
  **/
  @JsonProperty("subscriptionId")
  public String getSubscriptionId() {
    return subscriptionId;
  }

  public void setSubscriptionId(String subscriptionId) {
    this.subscriptionId = subscriptionId;
  }

  public SubscriptionDeliveryDTO subscriptionId(String subscriptionId) {
    this.subscriptionId = subscriptionId;
    return this;
  }

 /**
   * Get groupId
   * @return groupId
  **/
  @JsonProperty("groupId")
  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public SubscriptionDeliveryDTO groupId(String groupId) {
    this.groupId = groupId;
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

  public SubscriptionDeliveryDTO topic(String topic) {
    this.topic = topic;
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

  public SubscriptionDeliveryDTO currentStatus(String currentStatus) {
    this.currentStatus = currentStatus;
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

  public SubscriptionDeliveryDTO deliveryMode(String deliveryMode) {
    this.deliveryMode = deliveryMode;
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

  public SubscriptionDeliveryDTO occurredAt(Long occurredAt) {
    this.occurredAt = occurredAt;
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
    SubscriptionDeliveryDTO subscriptionDeliveryDTO = (SubscriptionDeliveryDTO) o;
    return Objects.equals(deliveryId, subscriptionDeliveryDTO.deliveryId) &&
        Objects.equals(eventId, subscriptionDeliveryDTO.eventId) &&
        Objects.equals(subscriptionId, subscriptionDeliveryDTO.subscriptionId) &&
        Objects.equals(groupId, subscriptionDeliveryDTO.groupId) &&
        Objects.equals(topic, subscriptionDeliveryDTO.topic) &&
        Objects.equals(currentStatus, subscriptionDeliveryDTO.currentStatus) &&
        Objects.equals(deliveryMode, subscriptionDeliveryDTO.deliveryMode) &&
        Objects.equals(occurredAt, subscriptionDeliveryDTO.occurredAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deliveryId, eventId, subscriptionId, groupId, topic, currentStatus, deliveryMode, occurredAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubscriptionDeliveryDTO {\n");
    
    sb.append("    deliveryId: ").append(toIndentedString(deliveryId)).append("\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    subscriptionId: ").append(toIndentedString(subscriptionId)).append("\n");
    sb.append("    groupId: ").append(toIndentedString(groupId)).append("\n");
    sb.append("    topic: ").append(toIndentedString(topic)).append("\n");
    sb.append("    currentStatus: ").append(toIndentedString(currentStatus)).append("\n");
    sb.append("    deliveryMode: ").append(toIndentedString(deliveryMode)).append("\n");
    sb.append("    occurredAt: ").append(toIndentedString(occurredAt)).append("\n");
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

