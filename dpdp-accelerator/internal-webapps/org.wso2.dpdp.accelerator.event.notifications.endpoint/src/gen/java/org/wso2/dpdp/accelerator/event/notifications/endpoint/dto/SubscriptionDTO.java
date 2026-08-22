package org.wso2.dpdp.accelerator.event.notifications.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.DeliveryConfigDTO;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.FilterDTO;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.SubscriptionStatus;

import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)

public class SubscriptionDTO  {
  
  @ApiModelProperty(value = "")
  private String subscriptionId;

  @ApiModelProperty(value = "")
  private String orgId;

  @ApiModelProperty(value = "")
  private String groupId;

  @ApiModelProperty(value = "")
  private String topic;

  @ApiModelProperty(value = "")
  private FilterDTO filter;

  @ApiModelProperty(value = "")
  private DeliveryConfigDTO delivery;

  @ApiModelProperty(value = "")
  private SubscriptionStatus status;

  @ApiModelProperty(value = "Epoch milliseconds.")
 /**
   * Epoch milliseconds.
  **/
  private Long createdAt;

  @ApiModelProperty(value = "Epoch milliseconds.")
 /**
   * Epoch milliseconds.
  **/
  private Long updatedAt;

  @ApiModelProperty(value = "")
  private Boolean alreadyExists;

  @ApiModelProperty(value = "")
  private String message;
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

  public SubscriptionDTO subscriptionId(String subscriptionId) {
    this.subscriptionId = subscriptionId;
    return this;
  }

 /**
   * Get orgId
   * @return orgId
  **/
  @JsonProperty("orgId")
  public String getOrgId() {
    return orgId;
  }

  public void setOrgId(String orgId) {
    this.orgId = orgId;
  }

  public SubscriptionDTO orgId(String orgId) {
    this.orgId = orgId;
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

  public SubscriptionDTO groupId(String groupId) {
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

  public SubscriptionDTO topic(String topic) {
    this.topic = topic;
    return this;
  }

 /**
   * Get filter
   * @return filter
  **/
  @JsonProperty("filter")
  public FilterDTO getFilter() {
    return filter;
  }

  public void setFilter(FilterDTO filter) {
    this.filter = filter;
  }

  public SubscriptionDTO filter(FilterDTO filter) {
    this.filter = filter;
    return this;
  }

 /**
   * Get delivery
   * @return delivery
  **/
  @JsonProperty("delivery")
  public DeliveryConfigDTO getDelivery() {
    return delivery;
  }

  public void setDelivery(DeliveryConfigDTO delivery) {
    this.delivery = delivery;
  }

  public SubscriptionDTO delivery(DeliveryConfigDTO delivery) {
    this.delivery = delivery;
    return this;
  }

 /**
   * Get status
   * @return status
  **/
  @JsonProperty("status")
  public SubscriptionStatus getStatus() {
    return status;
  }

  public void setStatus(SubscriptionStatus status) {
    this.status = status;
  }

  public SubscriptionDTO status(SubscriptionStatus status) {
    this.status = status;
    return this;
  }

 /**
   * Epoch milliseconds.
   * @return createdAt
  **/
  @JsonProperty("createdAt")
  public Long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Long createdAt) {
    this.createdAt = createdAt;
  }

  public SubscriptionDTO createdAt(Long createdAt) {
    this.createdAt = createdAt;
    return this;
  }

 /**
   * Epoch milliseconds.
   * @return updatedAt
  **/
  @JsonProperty("updatedAt")
  public Long getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Long updatedAt) {
    this.updatedAt = updatedAt;
  }

  public SubscriptionDTO updatedAt(Long updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

 /**
   * Get alreadyExists
   * @return alreadyExists
  **/
  @JsonProperty("alreadyExists")
  public Boolean getAlreadyExists() {
    return alreadyExists;
  }

  public void setAlreadyExists(Boolean alreadyExists) {
    this.alreadyExists = alreadyExists;
  }

  public SubscriptionDTO alreadyExists(Boolean alreadyExists) {
    this.alreadyExists = alreadyExists;
    return this;
  }

 /**
   * Get message
   * @return message
  **/
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public SubscriptionDTO message(String message) {
    this.message = message;
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
    SubscriptionDTO subscriptionDTO = (SubscriptionDTO) o;
    return Objects.equals(subscriptionId, subscriptionDTO.subscriptionId) &&
        Objects.equals(orgId, subscriptionDTO.orgId) &&
        Objects.equals(groupId, subscriptionDTO.groupId) &&
        Objects.equals(topic, subscriptionDTO.topic) &&
        Objects.equals(filter, subscriptionDTO.filter) &&
        Objects.equals(delivery, subscriptionDTO.delivery) &&
        Objects.equals(status, subscriptionDTO.status) &&
        Objects.equals(createdAt, subscriptionDTO.createdAt) &&
        Objects.equals(updatedAt, subscriptionDTO.updatedAt) &&
        Objects.equals(alreadyExists, subscriptionDTO.alreadyExists) &&
        Objects.equals(message, subscriptionDTO.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subscriptionId, orgId, groupId, topic, filter, delivery, status, createdAt, updatedAt, alreadyExists, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubscriptionDTO {\n");
    
    sb.append("    subscriptionId: ").append(toIndentedString(subscriptionId)).append("\n");
    sb.append("    orgId: ").append(toIndentedString(orgId)).append("\n");
    sb.append("    groupId: ").append(toIndentedString(groupId)).append("\n");
    sb.append("    topic: ").append(toIndentedString(topic)).append("\n");
    sb.append("    filter: ").append(toIndentedString(filter)).append("\n");
    sb.append("    delivery: ").append(toIndentedString(delivery)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    alreadyExists: ").append(toIndentedString(alreadyExists)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

