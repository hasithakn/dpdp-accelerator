package org.wso2.dpdp.accelerator.event.notifications.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.DeliveryMode;

import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)

public class DeliveryConfigDTO  {
  
  @ApiModelProperty(value = "")
  private DeliveryMode mode;

  @ApiModelProperty(value = "")
  private String callbackUrl;

  @ApiModelProperty(value = "")
  private String sharedSecret;
 /**
   * Get mode
   * @return mode
  **/
  @JsonProperty("mode")
  public DeliveryMode getMode() {
    return mode;
  }

  public void setMode(DeliveryMode mode) {
    this.mode = mode;
  }

  public DeliveryConfigDTO mode(DeliveryMode mode) {
    this.mode = mode;
    return this;
  }

 /**
   * Get callbackUrl
   * @return callbackUrl
  **/
  @JsonProperty("callbackUrl")
  public String getCallbackUrl() {
    return callbackUrl;
  }

  public void setCallbackUrl(String callbackUrl) {
    this.callbackUrl = callbackUrl;
  }

  public DeliveryConfigDTO callbackUrl(String callbackUrl) {
    this.callbackUrl = callbackUrl;
    return this;
  }

 /**
   * Get sharedSecret
   * @return sharedSecret
  **/
  @JsonProperty("sharedSecret")
  public String getSharedSecret() {
    return sharedSecret;
  }

  public void setSharedSecret(String sharedSecret) {
    this.sharedSecret = sharedSecret;
  }

  public DeliveryConfigDTO sharedSecret(String sharedSecret) {
    this.sharedSecret = sharedSecret;
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
    DeliveryConfigDTO deliveryConfigDTO = (DeliveryConfigDTO) o;
    return Objects.equals(mode, deliveryConfigDTO.mode) &&
        Objects.equals(callbackUrl, deliveryConfigDTO.callbackUrl) &&
        Objects.equals(sharedSecret, deliveryConfigDTO.sharedSecret);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mode, callbackUrl, sharedSecret);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeliveryConfigDTO {\n");
    
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    callbackUrl: ").append(toIndentedString(callbackUrl)).append("\n");
    sb.append("    sharedSecret: ").append(toIndentedString(sharedSecret)).append("\n");
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

