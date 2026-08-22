package org.wso2.dpdp.accelerator.event.notifications.endpoint.dto;

import java.util.ArrayList;
import java.util.List;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.SubscriptionDeliveryDTO;

import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)

public class PaginatedSubscriptionDeliveryResult  {
  
  @ApiModelProperty(value = "")
  private List<SubscriptionDeliveryDTO> items;

  @ApiModelProperty(value = "")
  private Integer total;
 /**
   * Get items
   * @return items
  **/
  @JsonProperty("items")
  public List<SubscriptionDeliveryDTO> getItems() {
    return items;
  }

  public void setItems(List<SubscriptionDeliveryDTO> items) {
    this.items = items;
  }

  public PaginatedSubscriptionDeliveryResult items(List<SubscriptionDeliveryDTO> items) {
    this.items = items;
    return this;
  }

  public PaginatedSubscriptionDeliveryResult addItemsItem(SubscriptionDeliveryDTO itemsItem) {
    this.items.add(itemsItem);
    return this;
  }

 /**
   * Get total
   * @return total
  **/
  @JsonProperty("total")
  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public PaginatedSubscriptionDeliveryResult total(Integer total) {
    this.total = total;
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
    PaginatedSubscriptionDeliveryResult paginatedSubscriptionDeliveryResult = (PaginatedSubscriptionDeliveryResult) o;
    return Objects.equals(items, paginatedSubscriptionDeliveryResult.items) &&
        Objects.equals(total, paginatedSubscriptionDeliveryResult.total);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, total);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaginatedSubscriptionDeliveryResult {\n");
    
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
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

