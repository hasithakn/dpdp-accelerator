package org.wso2.dpdp.accelerator.event.notifications.endpoint.dto;

import java.util.ArrayList;
import java.util.List;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.SubscriptionDTO;

import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)

public class PaginatedSubscriptionResult  {
  
  @ApiModelProperty(value = "")
  private List<SubscriptionDTO> items;

  @ApiModelProperty(value = "")
  private Integer total;
 /**
   * Get items
   * @return items
  **/
  @JsonProperty("items")
  public List<SubscriptionDTO> getItems() {
    return items;
  }

  public void setItems(List<SubscriptionDTO> items) {
    this.items = items;
  }

  public PaginatedSubscriptionResult items(List<SubscriptionDTO> items) {
    this.items = items;
    return this;
  }

  public PaginatedSubscriptionResult addItemsItem(SubscriptionDTO itemsItem) {
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

  public PaginatedSubscriptionResult total(Integer total) {
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
    PaginatedSubscriptionResult paginatedSubscriptionResult = (PaginatedSubscriptionResult) o;
    return Objects.equals(items, paginatedSubscriptionResult.items) &&
        Objects.equals(total, paginatedSubscriptionResult.total);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, total);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaginatedSubscriptionResult {\n");
    
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

