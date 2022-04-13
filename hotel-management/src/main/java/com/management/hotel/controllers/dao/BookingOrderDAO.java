package com.management.hotel.controllers.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.management.hotel.controllers.entities.Booking;
import com.management.hotel.controllers.entities.BookingOrders;
@Repository
public interface BookingOrderDAO extends JpaRepository<BookingOrders, BigInteger>{

	List<BookingOrders> findAllByBooking(Booking booking);

}
