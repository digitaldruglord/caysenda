package com.nomi.caysenda.api.admin.model.order.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class AdminOrderSplitRequest {
	Integer orderId;
	List<Integer> ids;
}
