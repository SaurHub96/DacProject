package com.management.hotel.controllers.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import com.management.hotel.services.BookingService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCutomerRoom implements Serializable {
	private BigInteger roomId;
	private BigInteger customerId;
	private BigInteger bookingId;
	private int roomNumber=0;
	private String checkIn;
	private String checkOut;
	private String remark;
	private PaidBy paidBy=PaidBy.NotPaid;
	private String paidDetails;
	public Date getCheckInDate() {
		try {
			return BookingService.toDate(checkIn, BookingService.localDateTimeFormat);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public Date getCheckOutDate() {
		try {
			return BookingService.toDate(checkOut, BookingService.localDateTimeFormat);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
}
