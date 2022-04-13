package com.management.hotel.controllers.entities;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.management.hotel.controllers.model.PaidBy;

import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="booking")
public class Booking {
	@Id
	@GeneratedValue
	private BigInteger bookingId;
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customer customer;
	@ManyToOne
	@JoinColumn(name="room_id")
	private Room room;
	private Date checkIn;
	private Date checkOut;
	private String remark;
	private Double amount;
	@Enumerated(value = EnumType.STRING)
	private PaidBy paidBy=PaidBy.NotPaid;
	private String paidDetails;
	private Date paymentDate;
	
}
