package com.management.hotel.controllers.entities;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="order_items")
public class OrderItem {
	@Id
	@GeneratedValue
	private BigInteger itemId;
	private String itemName;
	private double price;
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
}
