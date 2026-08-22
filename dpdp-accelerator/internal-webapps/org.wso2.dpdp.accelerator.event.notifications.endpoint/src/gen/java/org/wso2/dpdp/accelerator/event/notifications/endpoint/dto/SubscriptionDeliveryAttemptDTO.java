package org.wso2.dpdp.accelerator.event.notifications.endpoint.dto;


import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)

public class SubscriptionDeliveryAttemptDTO  {
  
  @ApiModelProperty(value = "")
  private Integer attempt;

  @ApiModelProperty(value = "")
  private String status;

  @ApiModelProperty(value = "Epoch milliseconds.")
 /**
   * Epoch milliseconds.
  **/
  private Long timestamp;

  @ApiModelProperty(value = "Absent for attempts that never got an HTTP response (e.g. connection failure).")
 /**
   * Absent for attempts that never got an HTTP response (e.g. connection failure).
  **/
  private Integer httpStatus;

  @ApiModelProperty(value = "")
  private String error;
 /**
   * Get attempt
   * @return attempt
  **/
  @JsonProperty("attempt")
  public Integer getAttempt() {
    return attempt;
  }

  public void setAttempt(Integer attempt) {
    this.attempt = attempt;
  }

  public SubscriptionDeliveryAttemptDTO attempt(Integer attempt) {
    this.attempt = attempt;
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

  public SubscriptionDeliveryAttemptDTO status(String status) {
    this.status = status;
    return this;
  }

 /**
   * Epoch milliseconds.
   * @return timestamp
  **/
  @JsonProperty("timestamp")
  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  public SubscriptionDeliveryAttemptDTO timestamp(Long timestamp) {
    this.timestamp = timestamp;
    return this;
  }

 /**
   * Absent for attempts that never got an HTTP response (e.g. connection failure).
   * @return httpStatus
  **/
  @JsonProperty("httpStatus")
  public Integer getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(Integer httpStatus) {
    this.httpStatus = httpStatus;
  }

  public SubscriptionDeliveryAttemptDTO httpStatus(Integer httpStatus) {
    this.httpStatus = httpStatus;
    return this;
  }

 /**
   * Get error
   * @return error
  **/
  @JsonProperty("error")
  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public SubscriptionDeliveryAttemptDTO error(String error) {
    this.error = error;
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
    SubscriptionDeliveryAttemptDTO subscriptionDeliveryAttemptDTO = (SubscriptionDeliveryAttemptDTO) o;
    return Objects.equals(attempt, subscriptionDeliveryAttemptDTO.attempt) &&
        Objects.equals(status, subscriptionDeliveryAttemptDTO.status) &&
        Objects.equals(timestamp, subscriptionDeliveryAttemptDTO.timestamp) &&
        Objects.equals(httpStatus, subscriptionDeliveryAttemptDTO.httpStatus) &&
        Objects.equals(error, subscriptionDeliveryAttemptDTO.error);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attempt, status, timestamp, httpStatus, error);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubscriptionDeliveryAttemptDTO {\n");
    
    sb.append("    attempt: ").append(toIndentedString(attempt)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    httpStatus: ").append(toIndentedString(httpStatus)).append("\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
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

