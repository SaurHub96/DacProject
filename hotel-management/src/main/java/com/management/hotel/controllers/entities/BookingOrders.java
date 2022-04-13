package com.management.hotel.controllers.entities;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="booking_order")
public class BookingOrders {
	@Id
	@GeneratedValue
	private BigInteger orderId;
	@ManyToOne
	@JoinColumn(name = "booking_id")
	private Booking booking;
	@OneToOne
	@JoinColumn(name = "item_id")
	private OrderItem orderItem;
	private int count;
	private double amount;
	private String remark;
	@OneToOne
	@JoinColumn(name="emp_id")
	private Employee employee;
	private Date entryDate=new Date();
}
