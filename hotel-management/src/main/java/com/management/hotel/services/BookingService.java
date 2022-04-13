package com.management.hotel.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.management.hotel.controllers.entities.Booking;
import com.management.hotel.controllers.entities.BookingOrders;
import com.management.hotel.controllers.model.RoomBilingTime;

public class BookingService {
	public static final String localDateTimeFormat="yyyy-MM-dd'T'HH:mm";
	public static Date toDate(String date,String format) throws ParseException {
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
		return simpleDateFormat.parse(date);
	}
	public static int getBilledAmonut(Booking booking, List<BookingOrders> orders) {
		int amount=0;
		try {
			long hours=getHours(booking);
			double pricePerDay=Double.valueOf(booking.getRoom().getPricePerDay());
			double pricePerHour=pricePerDay!=0?pricePerDay/24:0;
			double actualAmount=hours*pricePerHour;
			amount=(int) (actualAmount<pricePerDay?pricePerDay:actualAmount);
			amount+=getOrderTotal(orders);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return amount;
	}
	public static long getHours(Booking booking) {
		return TimeUnit.MILLISECONDS.toHours(booking.getCheckOut().getTime()-booking.getCheckIn().getTime());
	}
	public static RoomBilingTime getRoomBilingTime(Booking booking) {
		long hours= TimeUnit.MILLISECONDS.toHours(booking.getCheckOut().getTime()-booking.getCheckIn().getTime());
		long day = TimeUnit.HOURS.toDays(hours);
		if(day<1) {
			day=1;
			hours=0;
		}else {
			hours=hours-TimeUnit.DAYS.toHours(day);
		}
		return new RoomBilingTime(day, hours);
	}
	
	public static int getOrderTotal( List<BookingOrders> orders) {
		int amount=0;
		try {
			if(orders!=null && !orders.isEmpty()) {
				for(BookingOrders order:orders) {
					amount+=order.getAmount();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return amount;
	}
}
