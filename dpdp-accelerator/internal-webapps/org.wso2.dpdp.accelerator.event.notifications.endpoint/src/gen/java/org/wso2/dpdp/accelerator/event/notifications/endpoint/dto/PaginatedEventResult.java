package org.wso2.dpdp.accelerator.event.notifications.endpoint.dto;

import java.util.ArrayList;
import java.util.List;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.EventDTO;

import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)

public class PaginatedEventResult  {
  
  @ApiModelProperty(value = "")
  private List<EventDTO> items;

  @ApiModelProperty(value = "")
  private Integer total;
 /**
   * Get items
   * @return items
  **/
  @JsonProperty("items")
  public List<EventDTO> getItems() {
    return items;
  }

  public void setItems(List<EventDTO> items) {
    this.items = items;
  }

  public PaginatedEventResult items(List<EventDTO> items) {
    this.items = items;
    return this;
  }

  public PaginatedEventResult addItemsItem(EventDTO itemsItem) {
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

  public PaginatedEventResult total(Integer total) {
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
    PaginatedEventResult paginatedEventResult = (PaginatedEventResult) o;
    return Objects.equals(items, paginatedEventResult.items) &&
        Objects.equals(total, paginatedEventResult.total);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, total);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaginatedEventResult {\n");
    
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

