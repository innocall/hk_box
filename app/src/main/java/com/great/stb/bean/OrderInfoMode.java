package com.great.stb.bean;

import java.util.List;

public class OrderInfoMode {
  private String orderTypeId;
  private String ItemCount;
  private List<ItemDataMode> itemData;
public String getOrderTypeId() {
	return orderTypeId;
}
public void setOrderTypeId(String orderTypeId) {
	this.orderTypeId = orderTypeId;
}
public String getItemCount() {
	return ItemCount;
}
public void setItemCount(String itemCount) {
	ItemCount = itemCount;
}
public List<ItemDataMode> getItemData() {
	return itemData;
}
public void setItemData(List<ItemDataMode> itemData) {
	this.itemData = itemData;
}
  
}
