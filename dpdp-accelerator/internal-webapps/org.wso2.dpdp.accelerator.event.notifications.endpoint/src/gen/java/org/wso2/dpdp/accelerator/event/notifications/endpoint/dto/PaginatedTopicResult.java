package org.wso2.dpdp.accelerator.event.notifications.endpoint.dto;

import java.util.ArrayList;
import java.util.List;
import org.wso2.dpdp.accelerator.event.notifications.endpoint.dto.TopicDTO;

import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)

public class PaginatedTopicResult  {
  
  @ApiModelProperty(value = "")
  private List<TopicDTO> items;

  @ApiModelProperty(value = "")
  private Integer total;
 /**
   * Get items
   * @return items
  **/
  @JsonProperty("items")
  public List<TopicDTO> getItems() {
    return items;
  }

  public void setItems(List<TopicDTO> items) {
    this.items = items;
  }

  public PaginatedTopicResult items(List<TopicDTO> items) {
    this.items = items;
    return this;
  }

  public PaginatedTopicResult addItemsItem(TopicDTO itemsItem) {
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

  public PaginatedTopicResult total(Integer total) {
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
    PaginatedTopicResult paginatedTopicResult = (PaginatedTopicResult) o;
    return Objects.equals(items, paginatedTopicResult.items) &&
        Objects.equals(total, paginatedTopicResult.total);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, total);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaginatedTopicResult {\n");
    
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

