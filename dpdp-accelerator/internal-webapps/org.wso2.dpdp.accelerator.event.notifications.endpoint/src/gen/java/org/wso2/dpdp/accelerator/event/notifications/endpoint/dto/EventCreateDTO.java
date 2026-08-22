package org.wso2.dpdp.accelerator.event.notifications.endpoint.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)

public class EventCreateDTO  {
  
  @ApiModelProperty(value = "")
  private String topicName;

  @ApiModelProperty(value = "")
  private List<String> purposes;

  @ApiModelProperty(value = "Arbitrary JSON, bound as-is and persisted as a JSON string on the event row.")
 /**
   * Arbitrary JSON, bound as-is and persisted as a JSON string on the event row.
  **/
  private Object payload;
 /**
   * Get topicName
   * @return topicName
  **/
  @JsonProperty("topicName")
  public String getTopicName() {
    return topicName;
  }

  public void setTopicName(String topicName) {
    this.topicName = topicName;
  }

  public EventCreateDTO topicName(String topicName) {
    this.topicName = topicName;
    return this;
  }

 /**
   * Get purposes
   * @return purposes
  **/
  @JsonProperty("purposes")
  public List<String> getPurposes() {
    return purposes;
  }

  public void setPurposes(List<String> purposes) {
    this.purposes = purposes;
  }

  public EventCreateDTO purposes(List<String> purposes) {
    this.purposes = purposes;
    return this;
  }

  public EventCreateDTO addPurposesItem(String purposesItem) {
    this.purposes.add(purposesItem);
    return this;
  }

 /**
   * Arbitrary JSON, bound as-is and persisted as a JSON string on the event row.
   * @return payload
  **/
  @JsonProperty("payload")
  public Object getPayload() {
    return payload;
  }

  public void setPayload(Object payload) {
    this.payload = payload;
  }

  public EventCreateDTO payload(Object payload) {
    this.payload = payload;
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
    EventCreateDTO eventCreateDTO = (EventCreateDTO) o;
    return Objects.equals(topicName, eventCreateDTO.topicName) &&
        Objects.equals(purposes, eventCreateDTO.purposes) &&
        Objects.equals(payload, eventCreateDTO.payload);
  }

  @Override
  public int hashCode() {
    return Objects.hash(topicName, purposes, payload);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventCreateDTO {\n");
    
    sb.append("    topicName: ").append(toIndentedString(topicName)).append("\n");
    sb.append("    purposes: ").append(toIndentedString(purposes)).append("\n");
    sb.append("    payload: ").append(toIndentedString(payload)).append("\n");
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

