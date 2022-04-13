package com.management.hotel.controllers.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.management.hotel.controllers.entities.Booking;
import com.management.hotel.controllers.entities.User;

public interface BookingDAO extends JpaRepository<Booking, BigInteger> {

	@Query("SELECT book FROM Booking book WHERE book.bookingId=:bookingId AND book.room.user=:user")
	Optional<Booking> findByIdAndUser(BigInteger bookingId, User user);
	
	@Query("SELECT book FROM Booking book WHERE book.room.roomNumber=:roomNumber AND book.customer.user=:user ORDER BY checkIn desc,bookingId desc")
	List<Booking> findAllByRoomNumberAndUser(int roomNumber, User user);
	
	@Query("SELECT book FROM Booking book WHERE book.customer.name like :name AND book.customer.user=:user ORDER BY checkIn desc,bookingId desc")
	List<Booking> findAllByNameStartWithAndUser(String name, User user);

	@Query("SELECT book FROM Booking book WHERE book.customer.customerId = :customerId AND book.customer.user=:user ORDER BY checkIn desc,bookingId desc")
	List<Booking> findAllByCustomerIdAndUser(BigInteger customerId, User user);

	@Query("SELECT book FROM Booking book WHERE book.paidDetails is null AND book.customer.user=:user ORDER BY checkIn desc,bookingId desc")
	List<Booking> findAllByUserAndPaidDetailsIsNull(User user);
	
}
