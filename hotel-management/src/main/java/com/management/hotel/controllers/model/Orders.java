package com.management.hotel.controllers.model;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
	
	private BigInteger orderId;
	private BigInteger bookingId;
	private BigInteger itemId;
	private int count;
	private double amount;
	private BigInteger empId;
	private String remark;
}
