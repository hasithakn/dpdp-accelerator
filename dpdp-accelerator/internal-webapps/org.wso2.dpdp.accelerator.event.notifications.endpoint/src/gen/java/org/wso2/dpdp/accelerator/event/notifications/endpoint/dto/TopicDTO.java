package org.wso2.dpdp.accelerator.event.notifications.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.Initiator;

import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)

public class TopicDTO  {
  
  @ApiModelProperty(value = "")
  private String topicId;

  @ApiModelProperty(value = "")
  private String name;

  @ApiModelProperty(value = "")
  private String description;

  @ApiModelProperty(value = "")
  private String status;

  @ApiModelProperty(value = "")
  private Initiator initiatedBy;
 /**
   * Get topicId
   * @return topicId
  **/
  @JsonProperty("topicId")
  public String getTopicId() {
    return topicId;
  }

  public void setTopicId(String topicId) {
    this.topicId = topicId;
  }

  public TopicDTO topicId(String topicId) {
    this.topicId = topicId;
    return this;
  }

 /**
   * Get name
   * @return name
  **/
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public TopicDTO name(String name) {
    this.name = name;
    return this;
  }

 /**
   * Get description
   * @return description
  **/
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public TopicDTO description(String description) {
    this.description = description;
    return this;
  }

 /**
   * Get status
   * @return status
  **/
  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public TopicDTO status(String status) {
    this.status = status;
    return this;
  }

 /**
   * Get initiatedBy
   * @return initiatedBy
  **/
  @JsonProperty("initiatedBy")
  public Initiator getInitiatedBy() {
    return initiatedBy;
  }

  public void setInitiatedBy(Initiator initiatedBy) {
    this.initiatedBy = initiatedBy;
  }

  public TopicDTO initiatedBy(Initiator initiatedBy) {
    this.initiatedBy = initiatedBy;
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
    TopicDTO topicDTO = (TopicDTO) o;
    return Objects.equals(topicId, topicDTO.topicId) &&
        Objects.equals(name, topicDTO.name) &&
        Objects.equals(description, topicDTO.description) &&
        Objects.equals(status, topicDTO.status) &&
        Objects.equals(initiatedBy, topicDTO.initiatedBy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(topicId, name, description, status, initiatedBy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TopicDTO {\n");
    
    sb.append("    topicId: ").append(toIndentedString(topicId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    initiatedBy: ").append(toIndentedString(initiatedBy)).append("\n");
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

